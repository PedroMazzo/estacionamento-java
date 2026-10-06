package br.com.estacionamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.List;
import br.com.estacionamento.model.Estacionamento;
import br.com.estacionamento.database.DatabaseInitializer;
import br.com.estacionamento.repository.EstacionamentoRepository;
import br.com.estacionamento.service.EstacionamentoService;

public class Main {

    public static void main(String[] args) {

        DatabaseInitializer.initialize();

        EstacionamentoRepository repository = new EstacionamentoRepository();

        EstacionamentoService service = new EstacionamentoService(repository);

        // boolean entrou = service.registrarEntrada(
        // "ZZZ9995",
        // LocalDateTime.of(2026, 10, 1, 20, 0));

        // System.out.println("Entrada registrada? " + entrou);

        boolean saiu = service.registrarSaida(
        "ZZZ9995",
        LocalDateTime.of(2026, 10, 1, 22, 0), new BigDecimal("15.00"));

        System.out.println("Saída registrada? " + saiu);

    //     List<Estacionamento> historico = service.buscarHistory("ZZZ9995");
    //     System.out.println(historico);
    //     for (Estacionamento estacionamento : historico) {
    //         System.out.println(estacionamento.getPlaca());
    //        System.out.println(estacionamento.getEntrada());
    //        System.out.println(estacionamento.getSaida());
    //        System.out.println(estacionamento.getStatus());
    //        System.out.println(estacionamento.getValorCalculado());
    //        System.out.println(estacionamento.getValorFinal());
    //    }

       List<Estacionamento> ativos = service.buscarTodosAtivos();
        System.out.println(ativos);
        for (Estacionamento estacionamento : ativos) {
            System.out.println(estacionamento.getPlaca());
           System.out.println(estacionamento.getEntrada());
           System.out.println(estacionamento.getStatus());


    }

}
}