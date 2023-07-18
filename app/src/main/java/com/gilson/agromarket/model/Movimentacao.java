package com.gilson.agromarket.model;

public class Movimentacao {

    private String idMovimentacao;
    private String dataMovimentacao;
    private String descricaoMovimento;
    private String saldoMovimentacao;

    public Movimentacao() {
    }

    public String getIdMovimentacao() {
        return idMovimentacao;
    }

    public void setIdMovimentacao(String idMovimentacao) {
        this.idMovimentacao = idMovimentacao;
    }

    public String getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(String dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    public String getDescricaoMovimento() {
        return descricaoMovimento;
    }

    public void setDescricaoMovimento(String descricaoMovimento) {
        this.descricaoMovimento = descricaoMovimento;
    }

    public String getSaldoMovimentacao() {
        return saldoMovimentacao;
    }

    public void setSaldoMovimentacao(String saldoMovimentacao) {
        this.saldoMovimentacao = saldoMovimentacao;
    }

}
