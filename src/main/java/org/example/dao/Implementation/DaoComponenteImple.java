package org.example.dao.Implementation;

import org.example.database.ConexaoMysql;
import org.example.database.ConexaoSQLServer;
import org.example.entities.Componente;
import org.example.entities.Maquina;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoComponenteImple implements org.example.dao.DaoComponente {

    public Integer cadastrarComponenteMysql(Componente componente, Integer idMaquina) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Integer idInserido = 0;
        try {
            conn = ConexaoMysql.getConection();

            st = conn.prepareStatement("""
                            INSERT INTO componente (tipo_componente, tamanho_total_gb, tamanho_disponivel_gb, modelo, frequencia, fabricante, fk_maquina) VALUES (?,?,?,?,?,?,?);
                    """, st.RETURN_GENERATED_KEYS);

            st.setString(1, componente.getTipo() == null ? "" : componente.getTipo());
            st.setDouble(2, componente.getTamanho_total_gb() == null ? 0.0 : componente.getTamanho_total_gb());
            st.setDouble(3, componente.getTamanho_livre_gb() == null ? 0.0 : componente.getTamanho_livre_gb());
            st.setString(4, componente.getModelo() == null ? "" : componente.getModelo());
            st.setLong(5, componente.getFrequencia() == null ? 0 : componente.getFrequencia());
            st.setString(6, componente.getFabricante() == null ? "" : componente.getFabricante());
            st.setInt(7, idMaquina);
            st.executeUpdate();

            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    idInserido = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Falha ao obter o ID inserido.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar componente: " + e.getMessage());
        }
        return idInserido;
    }

    public List<Componente> buscarComponenteMysql(Maquina maquina) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        List<Componente> componentes = new ArrayList<>();

        try {
            conn = ConexaoMysql.getConection();
            st = conn.prepareStatement("SELECT * FROM componente join maquina on maquina_id = fk_maquina WHERE processador_id = ?;");
            st.setString(1, maquina.getIdPorcessador());
            rs = st.executeQuery();
            while (rs.next()) {
                Componente componente = new Componente();
                componente.setIdComponente(rs.getInt("id_componente"));
                componente.setTipo(rs.getString("tipo_componente"));
                componente.setTamanho_total_gb(rs.getDouble("tamanho_total_gb"));
                componente.setTamanho_livre_gb(rs.getDouble("tamanho_disponivel_gb"));
                componente.setModelo(rs.getString("modelo"));
                componente.setFrequencia(rs.getLong("frequencia"));
                componente.setFabricante(rs.getString("fabricante"));
                componentes.add(componente);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar componente: " + e.getMessage());
        }
        return componentes;
    }

    public void cadastrarComponenteSqlServer(Maquina maquina) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            conn = ConexaoSQLServer.getConection();

            st = conn.prepareStatement("""
                            INSERT INTO componente (tipo_componente, tamanho_total_gb, tamanho_disponivel_gb, modelo, frequencia, fabricante, fk_maquina) VALUES (?,?,?,?,?,?,?);
                    """, st.RETURN_GENERATED_KEYS);

            for (int i = 0; i < maquina.listarComponentes().size(); i++) {
                st.setString(1, maquina.listarComponentes().get(i).getTipo() == null ? "" : maquina.listarComponentes().get(i).getTipo());
                st.setDouble(2, maquina.listarComponentes().get(i).getTamanho_total_gb() == null ? 0.0 : maquina.listarComponentes().get(i).getTamanho_total_gb());
                st.setDouble(3, maquina.listarComponentes().get(i).getTamanho_livre_gb() == null ? 0.0 : maquina.listarComponentes().get(i).getTamanho_livre_gb());
                st.setString(4, maquina.listarComponentes().get(i).getModelo() == null ? "" : maquina.listarComponentes().get(i).getModelo());
                st.setLong(5, maquina.listarComponentes().get(i).getFrequencia() == null ? 0 : maquina.listarComponentes().get(i).getFrequencia());
                st.setString(6, maquina.listarComponentes().get(i).getFabricante() == null ? "" : maquina.listarComponentes().get(i).getFabricante());
                st.setInt(7, maquina.getId());
                st.executeUpdate();

                try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idInserido = generatedKeys.getInt(1);
                        System.out.println("ID inserido: " + idInserido);
                    } else {
                        throw new SQLException("Falha ao obter o ID inserido.");
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar componente: " + e.getMessage());
        }
    }

}
