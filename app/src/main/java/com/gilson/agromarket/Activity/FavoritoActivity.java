package com.gilson.agromarket.Activity;

import static com.gilson.agromarket.config.Helper.message;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.gilson.agromarket.R;
import com.gilson.agromarket.adapter.FavoritoAdapter;
import com.gilson.agromarket.config.FavoritoDAO;
import com.gilson.agromarket.config.RecyclerItemClickListener;
import com.gilson.agromarket.model.Favorito;

import java.util.ArrayList;
import java.util.List;

public class FavoritoActivity extends AppCompatActivity {

    private RecyclerView favoritoRecyclerView;
    private FavoritoAdapter favoritoAdapter;
    private List<Favorito> listaFavorito = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorito);

        //configurar toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("AgroMarket");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_close);

        favoritoRecyclerView = findViewById(R.id.listaFavorito);

        // evento de click
        favoritoRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                getApplicationContext(),
                favoritoRecyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                        Favorito favortoSelecionado = listaFavorito.get(position);

                        AlertDialog.Builder dialog = new AlertDialog.Builder(FavoritoActivity.this);
                        dialog.setTitle("Confirmar remoção");
                        dialog.setMessage("Deseja excluir o favorito: " + favortoSelecionado.getDescricao() );

                        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                FavoritoDAO favoritoDAO = new FavoritoDAO(getApplicationContext());
                                if (favoritoDAO.deletar(favortoSelecionado)){

                                    listaFavorito();
                                    message(getApplicationContext(), "Sucesso ao eliminar favorito");

                                }else{
                                    message(getApplicationContext(), "Erro ao deletar favorito");
                                }
                            }
                        });

                        dialog.setNegativeButton("Não", null);

                        dialog.create();
                        dialog.show();

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }
        ));


    }

    @Override
    protected void onStart() {
        listaFavorito();
        super.onStart();

    }

    public void listaFavorito(){

        //Listar favorito
        FavoritoDAO favoritoDAO = new FavoritoDAO(getApplicationContext());
        listaFavorito = favoritoDAO.listar();

        //config adapter
        favoritoAdapter = new FavoritoAdapter(listaFavorito, getApplicationContext());

        //config recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        favoritoRecyclerView.setLayoutManager(layoutManager);
        favoritoRecyclerView.setHasFixedSize(true);
        favoritoRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        favoritoRecyclerView.setAdapter(favoritoAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {

        Intent intent = new Intent(FavoritoActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        return false;

    }

}