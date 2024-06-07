package org.example.entities;

import com.github.britooo.looca.api.core.Looca;
import org.example.dao.DaoRegistro;
import org.example.dao.Implementation.DaoRegistroImple;
import org.example.utilities.Utilitarios;

import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.List;

public class Acesso {
    private Integer idAcesso;
    private Integer fkFuncionario;
    private Integer fkMaquina;

    Utilitarios utilitarios = new Utilitarios();
    Looca looca = new Looca();

    DaoRegistro daoRegistro = new DaoRegistroImple();

    public Acesso() {
    }

    public Acesso(Integer idAcesso, Integer fkFuncionario, Integer fkMaquina) {
        this.idAcesso = idAcesso;
        this.fkFuncionario = fkFuncionario;
        this.fkMaquina = fkMaquina;
    }

    public void usoHardware() {
        Double porcentagemRam = ((double) looca.getMemoria().getEmUso() / looca.getMemoria().getTotal()) * 100;
        Double porcentagemProcessador = (looca.getProcessador().getUso() * 100.0) / 100;
        utilitarios.centralizaTelaHorizontal(15);
        System.out.println("Memoria RAM em uso: %.2f  GB".formatted(Math.round((double) looca.getMemoria().getEmUso() / Math.pow(1024, 3) * 100.0) / 100.0));
        utilitarios.centralizaTelaHorizontal(15);
        System.out.println("Porcentagem de uso da Memoria RAM: %.2f".formatted(porcentagemRam) + "%");
        utilitarios.centralizaTelaHorizontal(15);
        System.out.println("Porcentagem de uso do Processador: %.2f".formatted(porcentagemProcessador) + "%");
        utilitarios.centralizaTelaHorizontal(15);
        System.out.println("Volume de Disco em uso: %.2f  GB".formatted(Math.round((double) looca.getGrupoDeDiscos().getDiscos().get(0).getTamanho() / Math.pow(1024, 3) * 100.0) / 100.0));
        utilitarios.centralizaTelaHorizontal(15);
        System.out.println();


    }
    public void informaçoesHardware() {
        System.out.println();
        utilitarios.centralizaTelaHorizontal(15);
        System.out.println("Minha Máquina");
        utilitarios.centralizaTelaHorizontal(15);
        System.out.println("Sistema Operacional: " + System.getProperty("os.name"));
        utilitarios.centralizaTelaHorizontal(15);
        System.out.println("Arquitetura do sistema: " + System.getProperty("os.arch"));
        utilitarios.centralizaTelaHorizontal(15);
        System.out.println("Processador: " + looca.getProcessador().getNome());
        utilitarios.centralizaTelaHorizontal(15);
        System.out.println("Volume Total da Ram: " + Math.round((double) looca.getMemoria().getTotal() / Math.pow(1024, 3) * 100.0) / 100.0 + "GB");
        utilitarios.centralizaTelaHorizontal(15);
        System.out.println("Modelo do disco: " + looca.getGrupoDeDiscos().getDiscos().get(0).getModelo());
        utilitarios.centralizaTelaHorizontal(15);
        System.out.println("Volume Total do disco: " + Math.round((double) looca.getGrupoDeDiscos().getDiscos().get(0).getTamanho() / Math.pow(1024, 3) * 100.0) / 100.0 + "GB");
    }

    public void relatorioAcesso(Usuario usuario, Acesso acesso) {

        List<String> relatorio = daoRegistro.dadosRelatorio(usuario, acesso);

        System.out.println();
        utilitarios.centralizaTelaHorizontal(15);
        System.out.println("Relatório de Acesso");
        utilitarios.centralizaTelaHorizontal(15);
        System.out.println("Tempo de acesso: %s" + relatorio.get(0));
        utilitarios.centralizaTelaHorizontal(15);
        System.out.println("Seu primeiro acesso foi em: " + relatorio.get(1));
        utilitarios.centralizaTelaHorizontal(15);
        System.out.println("Seu último acesso foi em:" + relatorio.get(3));
        utilitarios.centralizaTelaHorizontal(15);
        System.out.println("Seu tempo médio de acesso é de: " + relatorio.get(2));

    }

    public void relatorioUsoDiario(Maquina maquina) throws SQLException {
        List<String> relatorio = daoRegistro.relatorioUsoDiario(maquina);

        System.out.println();
        utilitarios.centralizaTelaHorizontal(15);
        System.out.println("Relatório de Uso Diário");
        utilitarios.centralizaTelaHorizontal(15);
        System.out.println("O seu uso máximo de RAM foi de: " + relatorio.get(0));
        utilitarios.centralizaTelaHorizontal(15);
        System.out.println("O seu uso máximo de CPU foi de: " + relatorio.get(1));
        utilitarios.centralizaTelaHorizontal(15);
        System.out.println("Seu uso médio de RAM foi de: " + relatorio.get(2) + "GB");
        utilitarios.centralizaTelaHorizontal(15);
        System.out.println("Seu uso médio de CPU foi de: " + relatorio.get(3) + "%");

    }

    public void selecaoMenu(Integer opcao, Usuario usuario, Acesso acesso, Maquina maquina) throws SQLException {
        if (opcao == 1) {
            informaçoesHardware();
        } else if (opcao == 2) {
            usoHardware();
        } else if (opcao == 3) {
            relatorioAcesso(usuario, acesso);
        } else if (opcao == 4) {
            relatorioUsoDiario(maquina);
        } else if (opcao == 5) {
            daoRegistro.registroSaida(acesso);
            System.exit(0);
        }
    }


    public Integer getIdAcesso() {
        return idAcesso;
    }

    public void setIdAcesso(Integer idAcesso) {
        this.idAcesso = idAcesso;
    }

    public Integer getFkFuncionario() {
        return fkFuncionario;
    }

    public void setFkFuncionario(Integer fkFuncionario) {
        this.fkFuncionario = fkFuncionario;
    }

    public Integer getFkMaquina() {
        return fkMaquina;
    }

    public void setFkMaquina(Integer fkMaquina) {
        this.fkMaquina = fkMaquina;
    }
}
