package br.com.estacionamento;

import java.time.LocalDateTime;

import br.com.estacionamento.database.DatabaseInitializer;
import br.com.estacionamento.repository.EstacionamentoRepository;
import br.com.estacionamento.service.EstacionamentoService;

public class Main {

    public static void main(String[] args) {

        DatabaseInitializer.initialize();

        EstacionamentoRepository repository = new EstacionamentoRepository();

        EstacionamentoService service = new EstacionamentoService(repository);

        boolean entrou = service.registrarEntrada(
                "ABC9999",
                LocalDateTime.of(2026, 10, 1, 20, 0));

        System.out.println("Entrada registrada? " + entrou);
    }

}