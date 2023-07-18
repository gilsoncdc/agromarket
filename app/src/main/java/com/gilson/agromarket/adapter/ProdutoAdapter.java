package com.gilson.agromarket.adapter;

import static com.gilson.agromarket.config.Helper.message;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gilson.agromarket.Activity.DetalheProdutoActivity;
import com.gilson.agromarket.Activity.MainActivity;
import com.gilson.agromarket.R;
import com.gilson.agromarket.config.DBSqliteConfig;
import com.gilson.agromarket.config.FavoritoDAO;
import com.gilson.agromarket.config.PerfilFirebase;
import com.gilson.agromarket.model.Favorito;
import com.gilson.agromarket.model.Produto;

import java.util.List;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.MyViewHolder> {

    private List<Produto> produtos;
    private Context context;

    private ClickAddCart clickAddCart;

    private String idUtilizador;

    public interface  ClickAddCart{
        void onClickAddCart(Button botaoAddCart, Produto produto);
    }

    public ProdutoAdapter(List<Produto> produtos, Context context, ClickAddCart clickAddCart) {
        this.produtos = produtos;
        this.context = context;
        this.clickAddCart = clickAddCart;
    }

    @NonNull
    @Override
    public ProdutoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemListaProduto = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produto, parent, false);
        return new MyViewHolder(itemListaProduto);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoAdapter.MyViewHolder holder, int position) {

        //recuperando os dados do firebase
        Produto produto = produtos.get(position);
        holder.descricaoProduto.setText(produto.getDescricaoProduto());
        holder.descricaTextoProduto.setText(produto.getTextoProduto());
        holder.precoProduto.setText(produto.getPreco() + "$00 / KG");

        Uri url = Uri.parse(produto.getImgProduto());
        Glide.with(context).load(url).into(holder.imgProduto);

        holder.imgProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DetalheProdutoActivity.class);
                intent.putExtra("descricaoProduto", produto.getDescricaoProduto());
                intent.putExtra("precoProduto", produto.getPreco());
                intent.putExtra("imgProduto", produto.getImgProduto());
                intent.putExtra("textoProduto", produto.getTextoProduto());

                context.startActivity(intent);

            }
        });

        FavoritoDAO favoritoDAO = new FavoritoDAO(context.getApplicationContext());
        if(favoritoDAO.verificarProdutoExiste(produto.getDescricaoProduto())){
            holder.imageFavorito.setImageResource(R.drawable.icon_favorite);
        }else{
            holder.imageFavorito.setImageResource(R.drawable.icon_favorite_border);
        }

        holder.imageFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                idUtilizador = PerfilFirebase.getIdUtilizador();

                FavoritoDAO favoritoDAO = new FavoritoDAO(context.getApplicationContext());

                Favorito favorito = new Favorito();
                favorito.setDescricao(produto.getDescricaoProduto());
                favorito.setFoto(produto.getImgProduto());
                favorito.setPreco(Integer.parseInt(produto.getPreco()));
                favorito.setIdUtilizador(idUtilizador);

                favoritoDAO.salvar(favorito);

                message(context.getApplicationContext(), "Adicionado ao favorito");
                Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
                context.startActivity(intent);

            }
        });


        holder.botaoAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clickAddCart.onClickAddCart(holder.botaoAddCart, produto);

            }
        });

    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView descricaoProduto;
        TextView descricaTextoProduto;
        TextView precoProduto;
        ImageView imgProduto;
        ImageView imageFavorito;
        Button botaoAddCart;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            descricaoProduto = itemView.findViewById(R.id.descricao);
            descricaTextoProduto = itemView.findViewById(R.id.descricaoTexto);
            precoProduto = itemView.findViewById(R.id.preco);
            imgProduto = itemView.findViewById(R.id.imagemProduto);
            botaoAddCart = itemView.findViewById(R.id.addCarinho);
            imageFavorito = itemView.findViewById(R.id.addFavorito);

        }
    }

}
