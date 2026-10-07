package br.com.estacionamento.service;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import br.com.estacionamento.model.Estacionamento;
import br.com.estacionamento.repository.EstacionamentoRepository;

public class EstacionamentoService {

    private EstacionamentoRepository repository;

    public EstacionamentoService(EstacionamentoRepository repository) {
        this.repository = repository;
    }

    public boolean registrarEntrada(String placa, LocalDateTime entrada) {
        boolean existe = repository.existeAtivo(placa);

        if (existe) {
            return false;
        }
        Estacionamento estacionamento = new Estacionamento();

        if (!estacionamento.setPlaca(placa)) {
            return false;
        }
        if (!estacionamento.setEntrada(entrada)) {
            return false;
        }

        return repository.salvar(estacionamento);

    }

    public boolean registrarSaida(String placa, LocalDateTime saida, BigDecimal valorFinal) {

        Estacionamento estacionamento = repository.buscarAtivo(placa);

        if (estacionamento == null) {
            return false;
        }

        if (saida.isBefore(estacionamento.getEntrada())) {
            return false;
        }

        estacionamento.setSaida(saida);
        estacionamento.calcularValor(new BigDecimal("0.1666666667"), new BigDecimal("20.00"));
        estacionamento.setStatus(Estacionamento.Status.FINALIZADO);
        if (!estacionamento.setValorFinal(valorFinal)) {
            return false;
        }

        return repository.registrarSaida(placa, saida, estacionamento.getStatus(), estacionamento.getValorCalculado(),
                estacionamento.getValorFinal());

    }

    public List<Estacionamento> buscarHistory(String placa) {
        return repository.buscarHistory(placa);
    }

    public List<Estacionamento> buscarTodosAtivos() {
        return repository.buscarTodosAtivos();
    }

    public Estacionamento buscarAtivo(String placa) {
        return repository.buscarAtivo(placa);

    }

    public BigDecimal calcularValorSaida(String placa, LocalDateTime saida) {

        Estacionamento estacionamento = repository.buscarAtivo(placa);

        if (estacionamento == null) {
            return null;
        }

        if (saida.isBefore(estacionamento.getEntrada())) {
            return null;
        }

        estacionamento.setSaida(saida);

        return estacionamento.calcularValor(
                new BigDecimal("0.1666666667"),
                new BigDecimal("20.00"));
    }

}
