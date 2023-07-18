package com.gilson.agromarket.config;

import com.gilson.agromarket.model.Favorito;

import java.util.List;

public interface IFavoritoDAO {

    public boolean salvar(Favorito favorito);
    public boolean atualizar(Favorito favorito);
    public  boolean deletar(Favorito favorito);
    public List<Favorito> listar();

}
