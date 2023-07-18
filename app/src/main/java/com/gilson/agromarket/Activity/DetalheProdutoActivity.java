package com.gilson.agromarket.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gilson.agromarket.R;

public class DetalheProdutoActivity extends AppCompatActivity {

    private TextView descProd, textoProd, precoProd;
    private ImageView imgProd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_produto);

        //configurar toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("AgroMarket");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_close);

        descProd = findViewById(R.id.d_produto);
        textoProd = findViewById(R.id.t_produto);
        precoProd = findViewById(R.id.p_produto);
        imgProd = findViewById(R.id.p_image);

        //receber dados
        String descricaoProduto = getIntent().getExtras().getString("descricaoProduto");
        String precoProduto = getIntent().getExtras().getString("precoProduto");
        String textoProduto = getIntent().getExtras().getString("textoProduto");
        String imgProduto = getIntent().getExtras().getString("imgProduto");

        descProd.setText(descricaoProduto);
        textoProd.setText(textoProduto);
        precoProd.setText(precoProduto+"$00");

        Uri url = Uri.parse(imgProduto);
        Glide.with(DetalheProdutoActivity.this)
                .load(url)
                .into(imgProd);


    }

    // subscrever a função voltar ao main pa simplesmente fechar a activity EditarPerfil
    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return false;

    }

}