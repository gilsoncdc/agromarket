package com.gilson.agromarket.model;

public class Produto {

    private String id;
    private String descricaoProduto;
    private String textoProduto;
    private String preco;
    private String imgProduto;
    private String tipo;
    private boolean isAddCart;

    public Produto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricaoProduto() {
        return descricaoProduto;
    }

    public void setDescricaoProduto(String descricaoProduto) {
        this.descricaoProduto = descricaoProduto;
    }

    public String getTextoProduto() {
        return textoProduto;
    }

    public void setTextoProduto(String textoProduto) {
        this.textoProduto = textoProduto;
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

    public boolean isAddCart() {
        return isAddCart;
    }

    public void setAddCart(boolean addCart) {
        isAddCart = addCart;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
