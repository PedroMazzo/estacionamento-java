package br.com.estacionamento.model;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

public class Estacionamento {
    private Long id;
    private String placa;
    private LocalDateTime entrada;
    private LocalDateTime saida;
    private BigDecimal valorPorMinuto;
    private BigDecimal valorDiaria;

    public enum Status {
        ATIVO,
        FINALIZADO,
        CANCELADO;

    }

    private Status status;
    private BigDecimal valorCalculado;
    private BigDecimal valorFinal;

    public Estacionamento() {
        this.status = Status.ATIVO;
    }

    public Long getId() {
        return this.id;
    }

    public boolean setId(Long id) {
        if (id != null) {
            this.id = id;
            return true;
        }
        return false;
    }

    public String getPlaca() {
        return this.placa;
    }

    public boolean setPlaca(String placa) {
        if (placa != null && placa.trim().length() == 7) {
            this.placa = placa.trim().toUpperCase();
            return true;
        }
        return false;
    }

    public LocalDateTime getEntrada() {
        return this.entrada;
    }

    public boolean setEntrada(LocalDateTime entrada) {
        if (entrada != null) {
            this.entrada = entrada;
            return true;
        }
        return false;
    }

    public LocalDateTime getSaida() {
        return this.saida;
    }

    public boolean setSaida(LocalDateTime saida) {
        if (saida != null) {
            this.saida = saida;
            return true;
        }
        return false;

    }

    public BigDecimal getValorPorMinuto() {
        return this.valorPorMinuto;
    }

    public boolean setValorPorMinuto(BigDecimal valorPorMinuto) {
        if (valorPorMinuto != null) {
            this.valorPorMinuto = valorPorMinuto;
            return true;
        }
        return false;
    }

    public BigDecimal getValorDiaria() {
        return valorDiaria;
    }

    public boolean setValorDiaria(BigDecimal valorDiaria) {
        if (valorDiaria != null) {
            this.valorDiaria = valorDiaria;
            return true;
        }
        return false;
    }

    public Status getStatus() {
        return this.status;
    }

    public boolean setStatus(Status status) {
        if (status != null) {
            this.status = status;
            return true;
        }
        return false;
    }

    public BigDecimal getValorCalculado() {
        return this.valorCalculado;
    }

    public boolean setValorCalculado(BigDecimal valorCalculado) {
        if (valorCalculado != null) {
            this.valorCalculado = valorCalculado;
            return true;
        }
        return false;
    }

    public BigDecimal getValorFinal() {
        return this.valorFinal;
    }

    public boolean setValorFinal(BigDecimal valorFinal) {
        if (valorFinal != null) {
            this.valorFinal = valorFinal;
            return true;
        }
        return false;
    }

    public BigDecimal calcularValor() {

        Duration duracao = Duration.between(entrada, saida);
        long minutos = duracao.toMinutes();

        BigDecimal minutosDecimal = BigDecimal.valueOf(minutos);

        this.valorCalculado = valorPorMinuto.multiply(minutosDecimal);

        if (this.valorCalculado.compareTo(valorDiaria) > 0) {
            this.valorCalculado = valorDiaria;
        }

        return this.valorCalculado;
    }
}