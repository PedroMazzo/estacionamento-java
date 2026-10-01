package br.com.estacionamento;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;

import br.com.estacionamento.database.DatabaseConnection;
import br.com.estacionamento.database.DatabaseInitializer;
import br.com.estacionamento.model.Estacionamento;
import br.com.estacionamento.repository.EstacionamentoRepository;

public class Main {
    public static void main(String[] args) {
        DatabaseInitializer.initialize();
        // Cara, basicamente, estamos testando erro, vamos tentar abrir uma conexão;
        // caso dê erro, dei um print no erro com o caminho dele.
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Deu certo com o SQL, BOA SODO!!");
        } catch (Exception e) {
            System.err.println("Falha ao conectar ;-;");
            e.printStackTrace();
        }

        Estacionamento estacionamento = new Estacionamento();

        EstacionamentoRepository repository = new EstacionamentoRepository();
        Estacionamento encontrado = repository.buscarAtivo("ABC1283");

        System.out.println("Placa encontrada " + encontrado.getPlaca());

        boolean existe = repository.existeAtivo("ABC1283");
        System.out.println(existe + " sim");

        estacionamento.setPlaca("ABC1283");

        estacionamento.setEntrada(LocalDateTime.of(2026, 9, 30, 20, 0));

        boolean salvo = repository.salvar(estacionamento);
        System.out.println("Ta salvo sodo clown? " + salvo);

        encontrado.setSaida(LocalDateTime.of(2026, 9, 30, 22, 0));

        encontrado.setValorPorMinuto(new BigDecimal(0.166666666666));
        encontrado.setValorDiaria(new BigDecimal(20));


        BigDecimal valorCalculado =  encontrado.calcularValor();

        System.out.println("Valor? " + valorCalculado);

        
    }
}