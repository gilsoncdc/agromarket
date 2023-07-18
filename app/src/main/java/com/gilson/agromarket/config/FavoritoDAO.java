package com.gilson.agromarket.config;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.core.database.sqlite.SQLiteDatabaseKt;

import com.gilson.agromarket.model.Favorito;

import java.util.ArrayList;
import java.util.List;

public class FavoritoDAO implements IFavoritoDAO {

    private SQLiteDatabase escreve;
    private SQLiteDatabase ler;

    public FavoritoDAO(Context context) {

        DBSqliteConfig db = new DBSqliteConfig(context);
        escreve = db.getWritableDatabase();
        ler = db.getReadableDatabase();

    }

    @Override
    public boolean salvar(Favorito favorito) {

        ContentValues cv = new ContentValues();
        cv.put("descricao", favorito.getDescricao());
        cv.put("foto", favorito.getFoto());
        cv.put("preco", favorito.getPreco());
        cv.put("idCliente", favorito.getIdUtilizador());

        try{

            escreve.insert(DBSqliteConfig.TABELA_FAVORITO, null, cv);
            Log.i("INFO", "Favorito salvo com sucesso");

        }catch (Exception e){
            Log.e("INFO", "Erro ao salvar os dados" + e.getMessage());
            return false;
        }

        return true;

    }

    @Override
    public boolean atualizar(Favorito favorito) {
        return false;
    }

    @Override
    public boolean deletar(Favorito favorito) {

        try{

            String[] args = {Integer.toString(favorito.getId())};
            escreve.delete(DBSqliteConfig.TABELA_FAVORITO, "id=?", args);
            Log.e("INFO", "Favorito deletado com sucesso");
        }catch (Exception e){
            Log.e("INFO", "Erro ao deletar favorito");
            return false;
        }

        return true;
    }

    @Override
    public List<Favorito> listar() {

        List<Favorito> favoritos = new ArrayList<>();

        String sql = "SELECT * FROM " + DBSqliteConfig.TABELA_FAVORITO + " ;";

        Cursor c = ler.rawQuery(sql, null);

        while (c.moveToNext()){

            Favorito favorito = new Favorito();


            int id = c.getInt(0);
            String descricaoFavorito = c.getString(1);
            String foto = c.getString(2);
            int preco = c.getInt(3);
            String idUtilizador = c.getString(4);

            favorito.setDescricao(descricaoFavorito);
            favorito.setPreco(Integer.valueOf(preco));
            favorito.setFoto(foto);
            favorito.setIdUtilizador(idUtilizador);
            favorito.setId(id);

            favoritos.add(favorito);

        }

        return favoritos;

    }

    public boolean verificarProdutoExiste(String produtoDescricao){

        boolean produtoExiste;
        String condicao = "descricao = ?";
        String[] args = {produtoDescricao};

        Cursor cursor = ler.query(DBSqliteConfig.TABELA_FAVORITO, null, condicao, args, null, null, null);

        if (cursor.getCount() > 0){
            return true;
        }else {
            return false;
        }

    }
}
