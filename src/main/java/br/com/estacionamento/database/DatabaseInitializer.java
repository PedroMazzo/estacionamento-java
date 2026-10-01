package br.com.estacionamento.database;

import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Connection;

public class DatabaseInitializer {
    public static void initialize() {
        String sqlVeiculo = """
                    CREATE TABLE IF NOT EXISTS estacionamentos (
                    id INTEGER PRIMARY KEY,
                    placa TEXT NOT NULL,
                    entrada TEXT NOT NULL,
                    saida TEXT,
                    status TEXT NOT NULL,
                    valor_calculado REAL,
                    valor_final REAL
                );
            """;
        try (Connection conn = DatabaseConnection.getConnection();
        Statement stmt = conn.createStatement()) {
            stmt.execute(sqlVeiculo);
            System.out.println("BD inicializado com sucess bitch sodo clown");
        } catch (SQLException e) {
            System.out.println("Deu erro no " + e.getMessage());
        }

    }

}
