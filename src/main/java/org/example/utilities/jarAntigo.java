//papackage org.example;
//import com.github.britooo.looca.api.core.Looca;
//import org.example.Db.Db;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.sql.*;
//import java.text.DecimalFormat;
//public class Funcionalidades {
//    Looca looca = new Looca();
//    Utilitarios utilitarios = new Utilitarios();
//    public static void matarProcessos() {
//        Utilitarios utilitarios = new Utilitarios();
//        try {
//            while (true) {
//                String os = System.getProperty("os.name");
//                if (os.contains("Windows")) {
//                    // Se for Windows, mantenha o código existente
//                    limparConsole();
//                    System.out.println("""
//
//                                               Monitoramento ativo!
//
//                            Este computador é monitorado em tempo real, incluindo o hardware, para
//                            assegurar conformidade com as políticas da empresa.
//                            Todas as atividades serão verificadas e, se necessário, medidas serão
//                            tomadas automaticamente pelo sistema.
//
//                            """);
//                    if (isProcessRunning("whatsApp.exe")) {
//                        Thread.sleep(3000);
//                        limparConsole();
//                        utilitarios.centralizaTelaVertical(5);
//                        utilitarios.centralizaTelaHorizontal(15);
//                        System.out.println("Programa indevido localizado");
//                        utilitarios.centralizaTelaHorizontal(15);
//                        System.out.println("Encerrando programa indevido!");
//                        Thread.sleep(3000);
//                        limparConsole();
//                        utilitarios.barraLoad(1);
//                        new ProcessBuilder("cmd", "/c", "taskkill", "/f", "/im", "whatsApp.exe").inheritIO().start().waitFor();
//                        Thread.sleep(3000);
//                        limparConsole();
//                        utilitarios.centralizaTelaVertical(2);
//                        utilitarios.centralizaTelaHorizontal(7);
//                        System.out.println("Programa indevido foi encerrado com sucesso!");
//                    }
//                } else {
//                    // Se for Linux, execute o comando para matar o processo
//                    try {
//                        Process process = Runtime.getRuntime().exec("ps -e");
//                        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//                        String line;
//                        while ((line = reader.readLine()) != null) {
//                            if (line.contains("whatsApp")) {
//                                String[] parts = line.trim().split("\\s+");
//                                String pid = parts[0];
//                                Process killProcess = Runtime.getRuntime().exec("kill -9 " + pid);
//                                killProcess.waitFor();
//                                // Adicione aqui a lógica para notificar que o processo foi encerrado
//                                if (killProcess.waitFor() == 0) {
//                                    System.out.println("Processo encerrado com sucesso!");
//                                } else {
//                                    System.out.println("Erro ao encerrar o processo!");
//                                }
//                            }
//                        }
//                    } catch (IOException | InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                Thread.sleep(5000);
//            }
//        } catch (final Exception exception) {
//            System.out.println("Erro ao Limpar o console!");
//        }
//    }
//    public static void limparConsole() {
//        try {
//            final String os = System.getProperty("os.name");
//            if (os.contains("Windows")) {
//                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
//            } else {
//                System.out.print("\033\143");
//            }
//        } catch (final Exception exception) {
//            System.out.println("Erro ao Limpar o console!");
//        }
//    }
//    public static boolean isProcessRunning(String processName) throws IOException {
//        Process process = new ProcessBuilder("tasklist", "/fi", "imagename eq " + processName).start();
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                if (line.contains(processName)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//    public void encerraProcesso(Integer pid) {
//        try {
//            Runtime.getRuntime().exec("taskkill /F /PID " + pid);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    public void usoHardware() {
//        Double porcentagemRam = ((double) looca.getMemoria().getEmUso() / looca.getMemoria().getTotal()) * 100;
//        Double porcentagemProcessador = (looca.getProcessador().getUso() * 100.0) / 100;
//        utilitarios.centralizaTelaHorizontal(15);
//        System.out.println("Memoria RAM em uso: %.2f  GB".formatted(Math.round((double) looca.getMemoria().getEmUso() / Math.pow(1024, 3) * 100.0) / 100.0));
//        utilitarios.centralizaTelaHorizontal(15);
//        System.out.println("Porcentagem de uso da Memoria RAM: %.2f".formatted(porcentagemRam) + "%");
//        utilitarios.centralizaTelaHorizontal(15);
//        System.out.println("Porcentagem de uso do Processador: %.2f".formatted(porcentagemProcessador) + "%");
//        utilitarios.centralizaTelaHorizontal(15);
//        System.out.println("Volume de Disco em uso: %.2f  GB".formatted(Math.round((double) looca.getGrupoDeDiscos().getDiscos().get(0).getTamanho() / Math.pow(1024, 3) * 100.0) / 100.0));
//        utilitarios.centralizaTelaHorizontal(15);
//        System.out.println();
//        System.out.println();
//
//
//    }
//    public void informaçoesHardware() {
//        System.out.println();
//        utilitarios.centralizaTelaHorizontal(15);
//        System.out.println("Minha Máquina");
//        utilitarios.centralizaTelaHorizontal(15);
//        System.out.println("Sistema Operacional: " + System.getProperty("os.name"));
//        utilitarios.centralizaTelaHorizontal(15);
//        System.out.println("Arquitetura do sistema: " + System.getProperty("os.arch"));
//        utilitarios.centralizaTelaHorizontal(15);
//        System.out.println("Processador: " + looca.getProcessador().getNome());
//        utilitarios.centralizaTelaHorizontal(15);
//        System.out.println("Volume Total da Ram: " + Math.round((double) looca.getMemoria().getTotal() / Math.pow(1024, 3) * 100.0) / 100.0 + "GB");
//        utilitarios.centralizaTelaHorizontal(15);
//        System.out.println("Modelo do disco: " + looca.getGrupoDeDiscos().getDiscos().get(0).getModelo());
//        utilitarios.centralizaTelaHorizontal(15);
//        System.out.println("Volume Total do disco: " + Math.round((double) looca.getGrupoDeDiscos().getDiscos().get(0).getTamanho() / Math.pow(1024, 3) * 100.0) / 100.0 + "GB");
//    }
//}
//
//package org.example;
//
//import com.github.britooo.looca.api.core.Looca;
//import org.example.Db.Db;
//
//import java.sql.*;
//        import java.text.DecimalFormat;
//import java.util.Scanner;
//
//import static org.example.Funcionalidades.limparConsole;
//
//
//public class Main {
//    public static void main(String[] args) throws SQLException, InterruptedException {
//        Utilitarios utils = new Utilitarios();
//        Scanner sc = new Scanner(System.in);
//        DecimalFormat decimalFormat = new DecimalFormat("#.##");
//
//        boolean isAuthenticated = false;
//        Looca looca = new Looca();
//        Funcionalidades funcionalidades = new Funcionalidades();
//
//
//        // Limpa o console e exibe as mensagens iniciais
//        limparConsole();
//        utils.exibirLogo();
//        utils.exibirMenu();
//        utils.exibirMensagem();
//
//        // Constantes para consultas SQL
//        final String authQuery = """
//            SELECT funcionario_id, nome_funcionario FROM funcionario
//            WHERE (email_funcionario = ? AND senha_acesso = ?) OR
//            (login_acesso = ? AND senha_acesso = ?);
//        """;
//
//        final String insertAccessQuery = "INSERT INTO acesso_usuario (fkAcessoUsuario, data_entrada) VALUES (?, now())";
//
//        final String getAccessIdQuery = """
//            SELECT IdAcessoUsuario FROM acesso_usuario
//            WHERE fkAcessoUsuario = ? AND data_saida IS NULL
//            ORDER BY data_entrada DESC LIMIT 1;
//        """;
//
//        final String updateExitQuery = """
//            UPDATE acesso_usuario SET data_saida = now()
//            WHERE fkAcessoUsuario = ? AND IdAcessoUsuario = ?;
//        """;
//
//        do {
//
//            utils.centralizaTelaHorizontal(22);
//            System.out.println("Email:");
//            utils.centralizaTelaHorizontal(22);
//            String email = sc.next();
//            System.out.println();
//
//            utils.centralizaTelaHorizontal(22);
//            System.out.println("Senha:");
//            utils.centralizaTelaHorizontal(22);
//            String senha = sc.next();
//
//            try (
//
//                    Connection conn = Db.getConection();
//                    PreparedStatement pstmtAuth = conn.prepareStatement(authQuery);
//                    PreparedStatement pstmtInsertAccess = conn.prepareStatement(insertAccessQuery);
//                    PreparedStatement pstmtGetAccessId = conn.prepareStatement(getAccessIdQuery);
//                    PreparedStatement pstmtUpdateExit = conn.prepareStatement(updateExitQuery);
//            ) {
//
//                pstmtAuth.setString(1, email);
//                pstmtAuth.setString(2, senha);
//                pstmtAuth.setString(3, email);
//                pstmtAuth.setString(4, senha);
//
//                try (ResultSet rt = pstmtAuth.executeQuery()) {
//                    if (rt.next()) {
//                        isAuthenticated = true;
//                        System.out.println("""
//
//
//
//                                         __________________________________________
//                                         |            ACESSO VALIDO !             |
//                                         |________________________________________|
//                        """);
//                        limparConsole();
//
//
//
//
//                        Integer funcionarioId = rt.getInt("funcionario_id");
//
//
//                        // Insere acesso do usuário
//                        pstmtInsertAccess.setInt(1, funcionarioId);
//                        pstmtInsertAccess.executeUpdate();
//
//                        // Obtém o ID do acesso do usuário
//                        pstmtGetAccessId.setInt(1, funcionarioId);
//                        Integer acessoUsuarioId = null;
//                        try (ResultSet rtGetAccessId = pstmtGetAccessId.executeQuery()) {
//                            if (rtGetAccessId.next()) {
//                                acessoUsuarioId = rtGetAccessId.getInt("IdAcessoUsuario");
//                            }
//                        }
//
//
//
//                        utils.mensagemInformativa();
//
//                        do {
//
//
//                            System.out.println();
//                            System.out.println("1 - Informações sobre minha máquina");
//                            System.out.println("2 - Uso de Hardware atual");
//                            System.out.println("3 - Relatório de Acesso");
//                            System.out.println("4 - Relatório de uso diário");
//                            System.out.println("5 - Sair");
//                            System.out.println();
//                            String response = sc.next();
//
//                            if (response.equalsIgnoreCase("1")) {
//                                funcionalidades.informaçoesHardware();
//                            } else if (response.equalsIgnoreCase("2")) {
//                                funcionalidades.usoHardware();
//                            } else if (response.equalsIgnoreCase("3")) {
//
//                                final String calcularTempoAcessoDia = "SELECT TIMEDIFF(NOW(), data_entrada) AS tempo_total\n" +
//                                        "FROM acesso_usuario\n" +
//                                        "WHERE fkAcessoUsuario = ? AND idAcessoUsuario = ?;";
//
//                                final String primeiroAcesso = "SELECT data_entrada\n" +
//                                        "FROM acesso_usuario\n" +
//                                        "ORDER BY idAcessoUsuario ASC\n" +
//                                        "LIMIT 1;";
//
//                                final String totalDiasAcesso = "SELECT COUNT(DISTINCT DATE(data_entrada))\n" +
//                                        "FROM acesso_usuario\n" +
//                                        "WHERE fkAcessoUsuario = ?; ";
//
//                                final String ultimooAcesso = "SELECT data_entrada, data_saida\n" +
//                                        "FROM acesso_usuario where data_saida IS NOT NULL\n" +
//                                        "ORDER BY idAcessoUsuario desc\n" +
//                                        "LIMIT 1;";
//                                final String tempoMedioAcesso = "SELECT CONCAT(\n" +
//                                        "    FLOOR(AVG(TIME_TO_SEC(TIMEDIFF(data_saida, data_entrada))) / 3600), ' Horas, ',\n" +
//                                        "    FLOOR(MOD(AVG(TIME_TO_SEC(TIMEDIFF(data_saida, data_entrada))), 3600) / 60), ' Minutos, ',\n" +
//                                        "    Floor(MOD(AVG(TIME_TO_SEC(TIMEDIFF(data_saida, data_entrada))), 60)), ' Segundos'\n" +
//                                        ") AS tempo_medio_formatado\n" +
//                                        "FROM acesso_usuario\n" +
//                                        "WHERE fkAcessoUsuario = 303;";
//
//                                try (
//                                        PreparedStatement pstmtCalcularTempoAcessoDia = conn.prepareStatement(calcularTempoAcessoDia);
//                                        PreparedStatement pstmtPrimeiroAcesso = conn.prepareStatement(primeiroAcesso);
//                                        PreparedStatement pstmtTotalDiasAcesso = conn.prepareStatement(totalDiasAcesso);
//                                        PreparedStatement pstmtUltimoAcesso = conn.prepareStatement(ultimooAcesso);
//                                        PreparedStatement pstmtTempoMedioAcesso = conn.prepareStatement(tempoMedioAcesso);
//                                )
//                                {
//
//
//                                    pstmtCalcularTempoAcessoDia.setInt(1, funcionarioId);
//                                    pstmtCalcularTempoAcessoDia.setInt(2, acessoUsuarioId);
//
//
//                                    try (ResultSet rsTempoAcesso = pstmtCalcularTempoAcessoDia.executeQuery()) {
//                                        if (rsTempoAcesso.next()) {
//                                            String tempoTotal = rsTempoAcesso.getString("tempo_total");
//                                            utils.centralizaTelaHorizontal(15);
//                                            System.out.printf("Tempo Conectado: %s%n", tempoTotal);
//                                        } else {
//                                            System.out.println("Nenhum dado disponível para o usuário fornecido.");
//                                        }
//                                    }
//
//
//
//                                    try (ResultSet rsPrimeiroAcesso = pstmtPrimeiroAcesso.executeQuery()) {
//                                        if (rsPrimeiroAcesso.next()) {
//                                            String dataEntrada = rsPrimeiroAcesso.getString("data_entrada");
//                                            utils.centralizaTelaHorizontal(15);
//                                            System.out.printf("Primeiro acesso: %s%n", dataEntrada);
//                                        } else {
//                                            System.out.println("Nenhum dado disponível para o primeiro acesso.");
//                                        }
//
//                                    }
//
//                                    pstmtTotalDiasAcesso.setInt(1, funcionarioId);
//
//                                    try(ResultSet rsTotalDiasAceso = pstmtTotalDiasAcesso.executeQuery()) {
//                                        if (rsTotalDiasAceso.next()) {
//                                            Integer totalDias = rsTotalDiasAceso.getInt(1);
//                                            utils.centralizaTelaHorizontal(15);
//                                            System.out.printf("Total de dias de acesso: %d%n", totalDias);
//                                        } else {
//                                            System.out.println("Nenhum dado disponível para o total de dias conectado.");
//                                        }
//                                    }
//
//                                    try (ResultSet rsUltimoAcesso = pstmtUltimoAcesso.executeQuery()) {
//                                        if (rsUltimoAcesso.next()) {
//                                            String dataSaida= rsUltimoAcesso.getString("data_saida");
//                                            utils.centralizaTelaHorizontal(15);
//                                            System.out.printf("Último acesso: %s%n", dataSaida);
//
//                                        } else {
//                                            System.out.println("Nenhum dado disponível para o último acesso.");
//                                        }
//                                    }
//
//                                    try(ResultSet rsTempoMedioAcesso = pstmtTempoMedioAcesso.executeQuery()) {
//                                        if (rsTempoMedioAcesso.next()) {
//                                            String tempoMedioFormatado = rsTempoMedioAcesso.getString("tempo_medio_formatado");
//                                            utils.centralizaTelaHorizontal(15);
//                                            System.out.printf("Tempo médio de acesso: %s%n", tempoMedioFormatado);
//                                            System.out.println();
//                                        } else {
//                                            System.out.println("Nenhum dado disponível para o tempo médio de acesso.");
//                                        }
//                                    }
//
//
//                                } catch (SQLException e) {
//                                    System.err.println("Erro durante a execução da query: " + e.getMessage());
//                                } catch (Exception e) {
//                                    System.err.println("Erro inesperado: " + e.getMessage());
//                                }
//                            } else if (response.equalsIgnoreCase("4")) {
//
//                                final String sqlVerificarId = "SELECT processador_id, maquina_id FROM maquina WHERE processador_id = '%s'".formatted(looca.getProcessador().getId());
//                                final String usoMedioRamDia = "select avg(ram_ocupada) as ram_ocupada from historico_hardware where fk_maquina = ? and data_hora >= current_date; ";
//                                final String usoMedioCpuDia = "select avg(cpu_ocupada) as cpu_ocupada from historico_hardware where fk_maquina = ? and data_hora >= current_date; ";
//                                final String maximoRamDia = "SELECT MAX(ram_ocupada) AS ram_ocupada, data_hora\n" +
//                                        "FROM historico_hardware\n" +
//                                        "WHERE fk_maquina = ?\n" +
//                                        "  AND data_hora >= CURRENT_DATE()\n" +
//                                        "  AND data_hora < CURRENT_DATE() + INTERVAL 1 DAY\n" +
//                                        "GROUP BY data_hora\n" +
//                                        "ORDER BY MAX(ram_ocupada) DESC\n" +
//                                        "LIMIT 1;";
//
//                                final String maximoCpuDia = "SELECT MAX(cpu_ocupada) AS cpu_ocupada, data_hora\n" +
//                                        "FROM historico_hardware\n" +
//                                        "WHERE fk_maquina = ?\n" +
//                                        "  AND data_hora >= CURRENT_DATE()\n" +
//                                        "  AND data_hora < CURRENT_DATE() + INTERVAL 1 DAY\n" +
//                                        "GROUP BY data_hora\n" +
//                                        "ORDER BY MAX(cpu_ocupada) DESC\n" +
//                                        "LIMIT 1;";
//
//
//                                try (   PreparedStatement pstmtVerificarId = conn.prepareStatement(sqlVerificarId);
//                                        PreparedStatement pstmUsoMedioRamDia = conn.prepareStatement(usoMedioRamDia);
//                                        PreparedStatement pstmUsoMedioCpuDia = conn.prepareStatement(usoMedioCpuDia);
//                                        PreparedStatement pstmMaximoRamDia = conn.prepareStatement(maximoRamDia);
//                                        PreparedStatement pstmMaximoCpuDia = conn.prepareStatement(maximoCpuDia)
//                                ) {
//
//                                    try (ResultSet rsVerificarId = pstmtVerificarId.executeQuery()) {
//                                        if (rsVerificarId.next()) {
//                                            Integer maquinaId = rsVerificarId.getInt("maquina_id");
//                                            pstmUsoMedioRamDia.setInt(1, maquinaId);
//                                            pstmUsoMedioCpuDia.setInt(1, maquinaId);
//                                            pstmMaximoRamDia.setInt(1, maquinaId);
//                                            pstmMaximoCpuDia.setInt(1, maquinaId);
//                                        } else {
//                                            System.out.println("Nenhum dado disponível para a máquina fornecida.");
//                                        }
//                                    }
//
//
//
//                                    try (ResultSet rsUsoMedioRamDia = pstmUsoMedioRamDia.executeQuery()) {
//                                        if (rsUsoMedioRamDia.next()) {
//                                            Double usoMedioRam = rsUsoMedioRamDia.getDouble("ram_ocupada");
//                                            utils.centralizaTelaHorizontal(15);
//                                            System.out.printf("Uso médio de RAM: %.2f GB%n", usoMedioRam);
//                                        } else {
//                                            utils.centralizaTelaHorizontal(15);
//                                            System.out.println("Nenhum dado disponível para o uso médio de RAM.");
//                                        }
//                                    }
//
//
//                                    try (ResultSet rsUsoMedioCpuDia = pstmUsoMedioCpuDia.executeQuery()) {
//                                        if (rsUsoMedioCpuDia.next()) {
//                                            Double usoMedioCpu = rsUsoMedioCpuDia.getDouble("cpu_ocupada");
//                                            utils.centralizaTelaHorizontal(15);
//                                            System.out.printf("Uso médio de CPU: %.2f %%%n", usoMedioCpu);
//                                        } else {
//                                            utils.centralizaTelaHorizontal(15);
//                                            System.out.println("Nenhum dado disponível para o uso médio de CPU.");
//                                        }
//                                    }
//
//
//                                    try (ResultSet rsMaximoRamDia = pstmMaximoRamDia.executeQuery()) {
//                                        if (rsMaximoRamDia.next()) {
//                                            Double maximoRam = rsMaximoRamDia.getDouble("ram_ocupada");
//                                            String dataHoraMaximoRam = rsMaximoRamDia.getString("data_hora");
//                                            utils.centralizaTelaHorizontal(15);
//                                            System.out.printf("Máximo de RAM: %.2f GB em %s%n", maximoRam, dataHoraMaximoRam);
//                                        } else {
//                                            utils.centralizaTelaHorizontal(15);
//                                            System.out.println("Nenhum dado disponível para o máximo de RAM.");
//                                        }
//                                    }
//
//
//                                    try (ResultSet rsMaximoCpuDia = pstmMaximoCpuDia.executeQuery()) {
//                                        if (rsMaximoCpuDia.next()) {
//                                            Double maximoCpu = rsMaximoCpuDia.getDouble("cpu_ocupada");
//                                            String dataHoraMaximoCpu = rsMaximoCpuDia.getString("data_hora");
//                                            utils.centralizaTelaHorizontal(15);
//                                            System.out.printf("Máximo de CPU: %.2f %% em %s%n", maximoCpu, dataHoraMaximoCpu);
//                                            System.out.println();
//                                        } else {
//                                            utils.centralizaTelaHorizontal(15);
//                                            System.out.println("Nenhum dado disponível para o máximo de CPU.");
//                                        }
//                                    }
//                                } catch (SQLException e) {
//                                    System.err.println("Erro durante a execução das queries: " + e.getMessage());
//                                }
//
//
//
//
//                            } else if (response.equalsIgnoreCase("5")) {
//                                System.out.println("Você tem certeza que deseja sair? (S/N)");
//                                String confirm = sc.next();
//
//                                if (confirm.equalsIgnoreCase("S")) {
//                                    // Atualiza dados de saída
//                                    pstmtUpdateExit.setInt(1, funcionarioId);
//                                    pstmtUpdateExit.setInt(2, acessoUsuarioId);
//                                    int rowsUpdated = pstmtUpdateExit.executeUpdate();
//
//                                    if (rowsUpdated > 0) {
//                                        System.out.println("Data de saída atualizada com sucesso.");
//                                        System.out.println("Sessão encerrada. Obrigado por usar o sistema.");
//                                        isAuthenticated = false;
//                                    } else {
//                                        System.out.println("Nenhum registro atualizado.");
//                                    }
//
//
//
//                                } else {
//                                    System.out.println("Sessão mantida.");
//                                }
//                            }
//                            else {
//                                System.out.println("Opção inválida.");
//                            }
//                        } while (isAuthenticated);
//
//                    } else {
//                        System.out.println("Usuário inválido.");
//                    }
//                }
//            } catch (SQLException e) {
//                System.err.println("Erro durante a execução: " + e.getMessage());
//            }
//        } while (isAuthenticated);
//    }
//}
