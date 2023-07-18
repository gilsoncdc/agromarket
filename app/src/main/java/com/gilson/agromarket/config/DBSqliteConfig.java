package com.gilson.agromarket.config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBSqliteConfig extends SQLiteOpenHelper {

    public static int VERSION = 1;
    public static String NOME_DB = "DB_AGRO";
    public static String TABELA_FAVORITO = "favorito";

    public DBSqliteConfig(@Nullable Context context) {
        super(context, NOME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE IF NOT EXISTS " + TABELA_FAVORITO
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " descricao TEXT NOT NULL, " +
                " foto TEXT NOT NULL, " +
                " preco TEXT NOT NULL, " +
                " idCliente TEXT NOT NULL ); ";

        try{
            db.execSQL(sql);
            Log.i("INFO DB", "Sucesso ao criar a tabela");

        }catch (Exception e){
            Log.i("INFO DB", "Erro ao criar a tabela" + e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        String sql = "DROP TABLE IF EXISTS " + TABELA_FAVORITO + " ;";

        try{
            db.execSQL(sql);
            onCreate(db);
            Log.i("INFO DB", "Sucesso ao atualizar app");

        }catch (Exception e){
            Log.i("INFO DB", "Erro ao atualizar app" + e.getMessage());
        }
    }
}
