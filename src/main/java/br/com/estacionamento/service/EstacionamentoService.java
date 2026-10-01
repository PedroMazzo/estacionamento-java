package br.com.estacionamento.service;

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

}
