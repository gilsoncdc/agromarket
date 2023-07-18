package com.gilson.agromarket.model;

import java.util.ArrayList;
import java.util.List;

public class Carteira {

    private String idUtilizador;
    private String saldo;
    private String numeroConta;

    public Carteira() {
    }

    public Carteira(String idUtilizador, String saldo, String numeroConta) {
        this.idUtilizador = idUtilizador;
        this.saldo = saldo;
        this.numeroConta = numeroConta;
    }

    public String getIdUtilizador() {
        return idUtilizador;
    }

    public void setIdUtilizador(String idUtilizador) {
        this.idUtilizador = idUtilizador;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldoCarregado) {
        this.saldo = saldoCarregado;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

}
