package br.com.estacionamento.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import br.com.estacionamento.database.DatabaseConnection;
import br.com.estacionamento.model.Estacionamento;

public class EstacionamentoRepository {

    public boolean existeAtivo(String placa) {

        String sql = """
                SELECT id
                FROM estacionamentos
                WHERE placa = ?
                AND status = 'ATIVO'
                """;

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, placa);

            var resultado = stmt.executeQuery();

            return resultado.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean salvar(Estacionamento estacionamento) {
        String sql = """
                INSERT INTO estacionamentos
                (placa, entrada, saida, status, valor_calculado, valor_final)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        if (existeAtivo(estacionamento.getPlaca()) == true) {
            return false;
        } else {
            try (Connection conn = DatabaseConnection.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, estacionamento.getPlaca());
                stmt.setObject(2, estacionamento.getEntrada());
                stmt.setObject(3, estacionamento.getSaida());
                stmt.setString(4, estacionamento.getStatus().name());
                stmt.setBigDecimal(5, estacionamento.getValorCalculado());
                stmt.setBigDecimal(6, estacionamento.getValorFinal());
                stmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public Estacionamento buscarAtivo(String placa) {
        String sql = """
                SELECT id, placa, entrada, saida, status,
                valor_calculado, valor_final
                FROM estacionamentos
                WHERE placa = ?
                AND status = 'ATIVO'
                """;
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, placa);
            var resultado = stmt.executeQuery();
            if (resultado.next()) {
                Estacionamento estacionamento = new Estacionamento();
                estacionamento.setPlaca(resultado.getString("placa"));

                String entrada = resultado.getString("entrada");
                estacionamento.setEntrada(LocalDateTime.parse(entrada));

                estacionamento.setId(resultado.getLong("id"));

                String status = resultado.getString("status");
                estacionamento.setStatus(Estacionamento.Status.valueOf(status));

                String saida = resultado.getString("saida");

                if (saida != null) {
                    estacionamento.setSaida(LocalDateTime.parse(saida));
                }

                String valorCalculado = resultado.getString("valor_calculado");

                if (valorCalculado != null) {
                    estacionamento.setValorCalculado(new BigDecimal(valorCalculado));
                }

                String valorFinal = resultado.getString("valor_final");
                if (valorFinal != null) {
                    estacionamento.setValorFinal(new BigDecimal(valorFinal));
                }

                return estacionamento;

            }
            return null;
        } catch (SQLException e) {
            return null;
        }

    }

    public boolean registrarSaida(String placa, LocalDateTime saida, Estacionamento.Status status,
            BigDecimal valorCalculado,
            BigDecimal valorFinal) {
        String sql = """
                UPDATE estacionamentos
                SET
                    saida = ?,
                    status = ?,
                    valor_calculado = ?,
                    valor_final = ?
                WHERE
                    placa = ?
                    AND status = 'ATIVO';
                """;
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setObject(1, saida);
            stmt.setString(2, status.name());
            stmt.setBigDecimal(3, valorCalculado);
            stmt.setBigDecimal(4, valorFinal);
            stmt.setString(5, placa);
            int linhasAlteradas = stmt.executeUpdate();

            return linhasAlteradas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Estacionamento> buscarHistory(String placa) {
        String sql = """
                SELECT *
                FROM estacionamentos
                WHERE placa = ?
                """;

        List<Estacionamento> historico = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, placa);
            var resultado = stmt.executeQuery();
            while (resultado.next()) {
                Estacionamento estacionamento = new Estacionamento();
                estacionamento.setPlaca(resultado.getString("placa"));

                String entrada = resultado.getString("entrada");
                estacionamento.setEntrada(LocalDateTime.parse(entrada));

                estacionamento.setId(resultado.getLong("id"));

                String status = resultado.getString("status");
                estacionamento.setStatus(Estacionamento.Status.valueOf(status));

                String saida = resultado.getString("saida");

                if (saida != null) {
                    estacionamento.setSaida(LocalDateTime.parse(saida));
                }

                String valorCalculado = resultado.getString("valor_calculado");

                if (valorCalculado != null) {
                    estacionamento.setValorCalculado(new BigDecimal(valorCalculado));
                }

                String valorFinal = resultado.getString("valor_final");
                if (valorFinal != null) {
                    estacionamento.setValorFinal(new BigDecimal(valorFinal));
                }

                historico.add(estacionamento);
            }
            return historico;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historico;

    }

    public List<Estacionamento> buscarTodosAtivos() {

        String sql = """
                SELECT *
                FROM estacionamentos
                WHERE status = 'ATIVO'
                """;
        List<Estacionamento> ativos = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            var resultado = stmt.executeQuery();
            while (resultado.next()) {
                Estacionamento estacionamento = new Estacionamento();
                estacionamento.setPlaca(resultado.getString("placa"));

                String entrada = resultado.getString("entrada");
                estacionamento.setEntrada(LocalDateTime.parse(entrada));

                estacionamento.setId(resultado.getLong("id"));

                String status = resultado.getString("status");
                estacionamento.setStatus(Estacionamento.Status.valueOf(status));

                String saida = resultado.getString("saida");
                if (saida != null){
                estacionamento.setSaida(LocalDateTime.parse(saida));
                }
                ativos.add(estacionamento);
            }

            return ativos;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ativos;
    }
}
