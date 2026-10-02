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

        estacionamento.setPlaca(placa);
        estacionamento.setEntrada(entrada);

        return repository.salvar(estacionamento);

    }

    public boolean registrarSaida(String placa, LocalDateTime saida) {

        Estacionamento estacionamento = repository.buscarAtivo(placa);

        if (estacionamento == null) {
            return false;
        }

        estacionamento.setSaida(saida);
        estacionamento.calcularValor(new BigDecimal("0.1666666667"), new BigDecimal("20.00"));
        estacionamento.setStatus(Estacionamento.Status.FINALIZADO);
        estacionamento.setValorFinal(estacionamento.getValorCalculado());

        return repository.registrarSaida(placa, saida, estacionamento.getStatus(), estacionamento.getValorCalculado(),
                estacionamento.getValorFinal());

    }

    public List<Estacionamento> buscarHistory(String placa) {
        return repository.buscarHistory(placa);
    }

}
