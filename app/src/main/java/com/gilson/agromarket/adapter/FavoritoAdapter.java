package com.gilson.agromarket.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gilson.agromarket.R;
import com.gilson.agromarket.model.Favorito;

import java.util.List;

public class FavoritoAdapter extends RecyclerView.Adapter<FavoritoAdapter.MyViewHolder> {

    private List<Favorito> listaFavorito;
    private Context context;

    public FavoritoAdapter(List<Favorito> favoritoList, Context context) {
        this.listaFavorito = favoritoList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorito, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Favorito favorito = listaFavorito.get(position);
        holder.descricao.setText(favorito.getDescricao());
        holder.preco.setText(Integer.toString(favorito.getPreco()));

        Uri url = Uri.parse(favorito.getFoto());
        Glide.with(context).load(url).into(holder.img);

    }

    @Override
    public int getItemCount() {
        return this.listaFavorito.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView descricao, preco;
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            descricao = itemView.findViewById(R.id.descricaoFavorito);
            preco = itemView.findViewById(R.id.precoFavorito);
            img = itemView.findViewById(R.id.fotoFavorito);

        }
    }


}
