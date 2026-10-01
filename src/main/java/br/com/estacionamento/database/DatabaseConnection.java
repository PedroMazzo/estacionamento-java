package br.com.estacionamento.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:estacionamento.db";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL);
        }
        catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao BD: " + e.getMessage(), e);
        }
    }

}
