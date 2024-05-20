package org.example.entities;

import org.example.dao.Implementation.DaoUsuarioImple;
import org.example.utilities.Utilitarios;

import java.io.Console;
import java.sql.SQLException;
import java.util.Scanner;

public class Usuario {
    private String nome;
    private String email;
    private String senha;
    private String login;
    private String dataEntrada;
    private String dataSaida;
    private String idUsuario;

    Console console = System.console();

    public Usuario() {
    }

    public Usuario(String nome, String email, String senha, String login, String dataEntrada, String dataSaida, String idUsuario) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.login = login;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.idUsuario = idUsuario;
    }

    public Usuario(String email, String senha, String login) {
        this.email = email;
        this.senha = senha;
        this.login = login;
    }

    public Boolean validarUsuario() throws SQLException {
        Scanner sc = new Scanner(System.in);
        Utilitarios utilitarios = new Utilitarios();
        utilitarios.centralizaTelaVertical(2);
        utilitarios.exibirMensagem();
        utilitarios.centralizaTelaHorizontal(22);
        System.out.print("Login: ");
        String login = sc.nextLine();
        utilitarios.centralizaTelaHorizontal(22);
        System.out.print("Senha: ");
        String senha;
        if (console != null) {
            char[] passwordArray = console.readPassword();
            senha = new String(passwordArray);
        } else {
            senha = sc.next();
        }

        DaoUsuarioImple daoUsuario = new DaoUsuarioImple();

        if (!login.equals(null) && !login.equals("") && !senha.equals(null) && !senha.equals("")) {
            Usuario usuario = daoUsuario.validarUsuarioSql(login, senha);
            if (usuario.getNome() == null) {
                return false;
            }
        }
        return true;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", login='" + login + '\'' +
                '}';
    }
}
