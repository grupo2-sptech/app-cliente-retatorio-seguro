package org.example.dao.Implementation;

import com.github.britooo.looca.api.core.Looca;
import org.example.database.ConexaoMysql;
import org.example.database.ConexaoSQLServer;
import org.example.entities.Acesso;
import org.example.entities.Maquina;
import org.example.entities.Usuario;
import org.example.entities.component.Registro;
import org.example.utilities.log.Log;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaoRegistroImple implements org.example.dao.DaoRegistro {

    Log logTeste = new Log();
    private Connection connMysl = null;
    private Connection connSql = null;
    private PreparedStatement stRamMysql = null;
    private PreparedStatement stCpuMysql = null;
    private PreparedStatement stRamSqlServer = null;
    private PreparedStatement stCpuSqlServer = null;

    Looca looca = new Looca();
    Registro registro = new Registro();

    public DaoRegistroImple() {
        try {
            connMysl = ConexaoMysql.getConection();
            connSql = ConexaoSQLServer.getConection();

            stRamMysql = connMysl.prepareStatement("INSERT INTO historico_hardware (ram_ocupada, data_hora, fk_maquina) VALUES (?, now(), ?);");
            stCpuMysql = connMysl.prepareStatement("INSERT INTO historico_hardware (cpu_ocupada, data_hora, fk_maquina) VALUES (?, now(), ?);");

            stRamSqlServer = connSql.prepareStatement("INSERT INTO historico_hardware (ram_ocupada, data_hora, fk_maquina) VALUES (?, GETDATE(), ?);");
            stCpuSqlServer = connSql.prepareStatement("INSERT INTO historico_hardware (cpu_ocupada, data_hora, fk_maquina) VALUES (?, GETDATE(), ?);");
        } catch (SQLException e) {
            try {
                logTeste.geradorLog("[" + logTeste.fomatarHora() + "] Erro: " + "Erro ao preparar statements: " + e.getMessage(), "erro de conexao registro");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void inserirRegistroTempoReal(Maquina maquina) {
        Double usoCpu = Math.round(looca.getProcessador().getUso() * 100.0) / 100.0;
        Double usoRam = registro.converterGB(looca.getMemoria().getEmUso());

        try {
            connMysl.setAutoCommit(false);
            connSql.setAutoCommit(false);

            stRamMysql.setDouble(1, usoRam);
            stRamMysql.setInt(2, maquina.getId());
            stRamMysql.addBatch();

            stCpuMysql.setDouble(1, usoCpu);
            stCpuMysql.setInt(2, maquina.getId());
            stCpuMysql.addBatch();

            stRamSqlServer.setDouble(1, usoRam);
            stRamSqlServer.setInt(2, maquina.getId());
            stRamSqlServer.addBatch();

            stCpuSqlServer.setDouble(1, usoCpu);
            stCpuSqlServer.setInt(2, maquina.getId());
            stCpuSqlServer.addBatch();

            stRamMysql.executeBatch();
            stCpuMysql.executeBatch();
            stRamSqlServer.executeBatch();
            stCpuSqlServer.executeBatch();

            connMysl.commit();
            connSql.commit();

        } catch (SQLException e) {
            try {
                logTeste.geradorLog("[" + logTeste.fomatarHora() + "] Erro: " + "Erro ao inserir registros: " + e.getMessage(), "erro de conexao registro");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            try {
                if (connMysl != null) connMysl.rollback();
                if (connSql != null) connSql.rollback();
            } catch (SQLException ex) {
                try {
                    logTeste.geradorLog("[" + logTeste.fomatarHora() + "] Erro: " + "Erro ao reverter transações: " + e.getMessage(), "erro de conexao registro");
                } catch (IOException exc) {
                    throw new RuntimeException(exc);
                }
            }
        } finally {
            try {
                if (connMysl != null) connMysl.setAutoCommit(true);
                if (connSql != null) connSql.setAutoCommit(true);
            } catch (SQLException e) {
                try {
                    logTeste.geradorLog("[" + logTeste.fomatarHora() + "] Erro: " + "Erro ao redefinir auto-commit: " + e.getMessage(), "erro de conexao registro");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }



    public Integer registroEntrada(Usuario usuario, Maquina maquina) {
        Integer idAcesso = 0;
        try (Connection conn = ConexaoSQLServer.getConection()) {
            // Preparar o statement com a opção para retornar as chaves geradas
            PreparedStatement st = conn.prepareStatement(
                    "INSERT INTO uso_maquina (fk_funcionario, fk_maquina, hora_data_entrada) VALUES (?, ?, GETDATE());",
                    Statement.RETURN_GENERATED_KEYS
            );
            st.setInt(1, usuario.getId());
            st.setInt(2, maquina.getId());
            st.executeUpdate();

            // Recuperar as chaves geradas
            ResultSet generatedKeys = st.getGeneratedKeys();
            if (generatedKeys.next()) {
                idAcesso = generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            try {
                logTeste.geradorLog("[" + logTeste.fomatarHora() + "] Erro: " + "Erro ao registrar entrada: " + e.getMessage(), "erro de conexao registro");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        return idAcesso;
    }

    public List<String> dadosRelatorio (Usuario usuario, Acesso acesso) {
        PreparedStatement st;
        ResultSet rs;
        List<String> relatorio = new ArrayList<>();

        Connection conn = ConexaoSQLServer.getConection();
        if (conn == null) {
            System.exit(0);
        } else {
            try {
                st = conn.prepareStatement("""
                                SELECT
                                    CONVERT(VARCHAR, DATEADD(SECOND, DATEDIFF(SECOND, hora_data_entrada, GETDATE()), 0), 108) AS tempo_total
                                FROM
                                    uso_maquina
                                WHERE
                                    fk_funcionario = ? AND id_acesso = ?;
                        """);
                st.setInt(1, usuario.getId());
                st.setInt(2, acesso.getIdAcesso());
                rs = st.executeQuery();
                if (rs.next()) {
                    relatorio.add(rs.getString("tempo_total"));
                }


                st = conn.prepareStatement("""
                                SELECT TOP 1 hora_data_entrada as primeiro_acesso FROM uso_maquina where fk_funcionario = ? ORDER BY id_acesso ASC;
                        """);
                st.setInt(1, usuario.getId());

                rs = st.executeQuery();
                if (rs.next()) {
                    relatorio.add(rs.getString("primeiro_acesso"));
                }

                st = conn.prepareStatement("""
                                SELECT CONCAT(FLOOR(AVG(DATEDIFF(SECOND, hora_data_entrada, hora_data_saida)) / 3600), ' Horas, ',
                                FLOOR((AVG(DATEDIFF(SECOND, hora_data_entrada, hora_data_saida)) % 3600) / 60), ' Minutos, ',
                                FLOOR(AVG(DATEDIFF(SECOND, hora_data_entrada, hora_data_saida)) % 60), ' Segundos') AS tempo_medio_formatado
                                FROM uso_maquina
                                WHERE fk_funcionario = ?;
                        """);
                st.setInt(1, usuario.getId());

                rs = st.executeQuery();
                if (rs.next()) {
                    relatorio.add(rs.getString("tempo_medio_formatado"));
                }

                st = conn.prepareStatement("""
                                SELECT TOP 1
                                    DATEADD(HOUR, -3, hora_data_saida) AS ultimo_acesso,
                                    hora_data_entrada
                                FROM
                                    uso_maquina
                                WHERE
                                    hora_data_saida IS NOT NULL
                                    AND fk_funcionario = ?
                                ORDER BY
                                    id_acesso DESC
                        """);
                st.setInt(1, usuario.getId());

                rs = st.executeQuery();
                if (rs.next()) {
                    relatorio.add(rs.getString("ultimo_acesso"));
                } else {
                    relatorio.add("Primeiro acesso ou ultima saída ainda não registrados.");
                }


            } catch (SQLException e) {
                try {
                    logTeste.geradorLog("[" + logTeste.fomatarHora() + "] Erro: " + "Erro ao validar usuario SQL SERVER: " + e.getMessage(), "erro de conexao usuario");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }


        return relatorio;
    }

    public void registroSaida(Acesso acesso) {
        try (Connection conn = ConexaoSQLServer.getConection()) {
            PreparedStatement st = conn.prepareStatement(
                    "UPDATE uso_maquina SET hora_data_saida = GETDATE() WHERE id_acesso = ?;"
            );
            st.setInt(1, acesso.getIdAcesso());
            st.executeUpdate();
        } catch (SQLException e) {
            try {
                logTeste.geradorLog("[" + logTeste.fomatarHora() + "] Erro: " + "Erro ao registrar saída: " + e.getMessage(), "erro de conexao registro");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public List<String> relatorioUsoDiario (Maquina maquina) {
        List<String> relatorioUso = new ArrayList<>();
        try (Connection conn = ConexaoSQLServer.getConection()) {
            PreparedStatement st = conn.prepareStatement("""
                    SELECT TOP 1
                        CONCAT(
                            CAST(MAX(ram_ocupada) AS VARCHAR(10)),
                            '% e foi às ',
                            CAST(DATEPART(HOUR, data_hora) AS VARCHAR(2)),
                            ':',
                            RIGHT('0' + CAST(DATEPART(MINUTE, data_hora) AS VARCHAR(2)), 2),
                            ':',
                            RIGHT('0' + CAST(DATEPART(SECOND, data_hora) AS VARCHAR(2)), 2)
                        ) AS uso_maximo_diario
                    FROM historico_hardware
                    WHERE fk_maquina = ?
                      AND data_hora >= CAST(GETDATE() AS DATE)
                      AND data_hora < DATEADD(DAY, 1, CAST(GETDATE() AS DATE))
                    GROUP BY data_hora
                    ORDER BY MAX(ram_ocupada) DESC;
                    """);
            st.setInt(1, maquina.getId());
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                relatorioUso.add(rs.getString("uso_maximo_diario"));
            }

            st = conn.prepareStatement("""
                    SELECT TOP 1
                        CONCAT(
                            CAST(MAX(cpu_ocupada) AS VARCHAR(10)),
                            '% e foi às ',
                            CAST(DATEPART(HOUR, data_hora) AS VARCHAR(2)),
                            ':',
                            RIGHT('0' + CAST(DATEPART(MINUTE, data_hora) AS VARCHAR(2)), 2),
                            ':',
                            RIGHT('0' + CAST(DATEPART(SECOND, data_hora) AS VARCHAR(2)), 2)
                        ) AS cpu_ocupada_data_hora
                    FROM historico_hardware
                    WHERE fk_maquina = ?
                      AND data_hora >= CAST(GETDATE() AS DATE)
                      AND data_hora < DATEADD(DAY, 1, CAST(GETDATE() AS DATE))
                    GROUP BY data_hora
                    ORDER BY MAX(cpu_ocupada) DESC;
                    """);
            st.setInt(1, maquina.getId());
            rs = st.executeQuery();

            if (rs.next()) {
                relatorioUso.add(rs.getString("cpu_ocupada_data_hora"));
            }

            st = conn.prepareStatement("""
                    SELECT
                        AVG(ram_ocupada) AS media_ram_diaria
                    FROM historico_hardware
                    WHERE fk_maquina = ?
                      AND data_hora >= CAST(GETDATE() AS DATE)
                      AND data_hora < DATEADD(DAY, 1, CAST(GETDATE() AS DATE));
                    """);

            st.setInt(1, maquina.getId());
            rs = st.executeQuery();

            if (rs.next()) {
                relatorioUso.add(rs.getString("media_ram_diaria"));
            }

            st = conn.prepareStatement("""
                    SELECT
                        AVG(cpu_ocupada) AS media_cpu_diaria
                    FROM historico_hardware
                    WHERE fk_maquina = ?
                      AND data_hora >= CAST(GETDATE() AS DATE)
                      AND data_hora < DATEADD(DAY, 1, CAST(GETDATE() AS DATE));
                    """);

            st.setInt(1, maquina.getId());

            rs = st.executeQuery();

            if (rs.next()) {
                relatorioUso.add(rs.getString("media_cpu_diaria"));
            }


        } catch (SQLException e) {
            try {
                logTeste.geradorLog("[" + logTeste.fomatarHora() + "] Erro: " + "Erro ao gerar relatório de uso diário: " + e.getMessage(), "erro de conexao registro");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        return relatorioUso;
    }


}
