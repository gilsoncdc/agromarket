package com.gilson.agromarket.model;

import com.gilson.agromarket.config.FirebaseConfig;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class ItemCarinho implements Serializable {

    private String idProduto;
    private String descricaoProduto;
    private String quantidade;
    private String preco;
    private String imgProduto;

    public ItemCarinho() {
    }

    public String getIdProduto() {
        return idProduto;
    }
    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getDescricaoProduto() {
        return descricaoProduto;
    }

    public void setDescricaoProduto(String descricaoProduto) {
        this.descricaoProduto = descricaoProduto;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getImgProduto() {
        return imgProduto;
    }

    public void setImgProduto(String imgProduto) {
        this.imgProduto = imgProduto;
    }

}

