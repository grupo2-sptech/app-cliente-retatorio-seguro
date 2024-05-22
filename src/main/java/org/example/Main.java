package org.example;

import com.github.britooo.looca.api.core.Looca;
import org.example.dao.DaoMaquina;
import org.example.dao.Implementation.DaoMaquinaImple;
import org.example.entities.Maquina;
import org.example.entities.Usuario;
import org.example.entities.component.Registro;
import org.example.utilities.Utilitarios;
import org.example.utilities.console.FucionalidadeConsole;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, InterruptedException {
        Registro registro = new Registro();
        Utilitarios utilitarios = new Utilitarios();
        FucionalidadeConsole fucionalidadeConsole = new FucionalidadeConsole();
        Usuario usuario = new Usuario();
        DaoMaquina daoMaquina = new DaoMaquinaImple();
        Looca looca = new Looca();
        Maquina maquina = new Maquina(
                null,
                looca.getProcessador().getId(),
                null,
                null,
                registro.converterGB(looca.getGrupoDeDiscos().getTamanhoTotal()),
                looca.getSistema().getSistemaOperacional(),
                looca.getSistema().getArquitetura()
        );


        fucionalidadeConsole.limparConsole();
        utilitarios.exibirLogo();
        usuario = usuario.validarUsuario();
        while (true) {
            if (usuario == null) {
                utilitarios.senhaIncorreta();
                Thread.sleep(2000);
                fucionalidadeConsole.limparConsole();
                utilitarios.exibirLogo();
                usuario = new Usuario();
                usuario = usuario.validarUsuario();
            } else {
                fucionalidadeConsole.limparConsole();
                utilitarios.exibirBemVindo();
                Thread.sleep(2000);
                break;
            }
        }

        if (daoMaquina.validarMaquinaSqlServer(maquina.getIdPorcessador()) == null) {
            maquina.cadastrarMaquina(maquina);
        }

        Usuario finalUsuario = usuario;


        Thread thread01 = new Thread() {
            public void run() {
                try {
                    maquina.monitoramento(maquina, finalUsuario);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Thread thread02 = new Thread() {
            public void run() {
                utilitarios.exibirMenuSelecao();
            }

        };

        thread01.start();
        thread02.start();

    }
}
