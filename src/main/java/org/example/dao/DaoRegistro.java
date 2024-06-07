package org.example.dao;

import org.example.database.DatabaseExeption;
import org.example.entities.Acesso;
import org.example.entities.Maquina;
import org.example.entities.Usuario;

import java.sql.SQLException;
import java.util.List;

public interface DaoRegistro {
    void inserirRegistroTempoReal(Maquina maquina);

    Integer registroEntrada(Usuario usuario, Maquina maquina) throws DatabaseExeption, SQLException;

    List<String> dadosRelatorio (Usuario usuario, Acesso acesso);

    void registroSaida(Acesso acesso) throws DatabaseExeption, SQLException;

    List<String> relatorioUsoDiario (Maquina maquina) throws DatabaseExeption, SQLException;

}
