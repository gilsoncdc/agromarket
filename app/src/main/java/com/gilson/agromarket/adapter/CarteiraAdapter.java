package com.gilson.agromarket.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gilson.agromarket.R;
import com.gilson.agromarket.model.Carteira;
import com.gilson.agromarket.model.Movimentacao;

import java.util.List;

public class CarteiraAdapter extends RecyclerView.Adapter<CarteiraAdapter.MyViewHolder> {

    private List<Movimentacao> carteiras;
    private Context context;

    public CarteiraAdapter(List<Movimentacao> carteiras, Context context) {
        this.carteiras = carteiras;
        this.context = context;
    }

    @NonNull
    @Override
    public CarteiraAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemListaCarteira = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carteira, parent, false);
        return new CarteiraAdapter.MyViewHolder(itemListaCarteira);
    }

    @Override
    public void onBindViewHolder(@NonNull CarteiraAdapter.MyViewHolder holder, int position) {

        Movimentacao carteira = carteiras.get(position);
        holder.descricaoMov.setText(carteira.getDescricaoMovimento());
        holder.saldoMov.setText(carteira.getSaldoMovimentacao());
        holder.dataMov.setText(carteira.getDataMovimentacao());

        int corVerde = Color.parseColor("#008000");
        int corVermelho = Color.parseColor("#ff0000");

        if (carteira.getDescricaoMovimento().equals("Carregamento saldo")){
            holder.descricaoMov.setTextColor(corVerde);
            holder.saldoMov.setTextColor(corVerde);
            holder.dataMov.setTextColor(corVerde);
        }else{
            holder.descricaoMov.setTextColor(corVermelho);
            holder.saldoMov.setTextColor(corVermelho);
            holder.dataMov.setTextColor(corVermelho);
        }

    }

    @Override
    public int getItemCount() {
        return carteiras.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView descricaoMov, saldoMov, dataMov;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            descricaoMov = itemView.findViewById(R.id.descricaoM);
            saldoMov = itemView.findViewById(R.id.saldoM);
            dataMov = itemView.findViewById(R.id.data);

        }
    }

}
