package com.gilson.agromarket.adapter;

import static com.gilson.agromarket.config.Helper.message;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gilson.agromarket.Activity.MainActivity;
import com.gilson.agromarket.R;
import com.gilson.agromarket.config.FirebaseConfig;
import com.gilson.agromarket.config.PerfilFirebase;
import com.gilson.agromarket.model.ItemCarinho;
import com.gilson.agromarket.model.Produto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class CarinhoAdapter extends RecyclerView.Adapter<CarinhoAdapter.MyViewHolder> {

    private List<ItemCarinho> itemCarinhos;
    private Context context;
    private String idUtilizador;
    private TextView total;

    private int qtdItensCarinho;
    private int totalPrecoCarinho;

    public CarinhoAdapter(List<ItemCarinho> itemCarinhos, Context context, TextView total) {
        this.itemCarinhos = itemCarinhos;
        this.context = context;
        this.total = total;
    }

    @NonNull
    @Override
    public CarinhoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemListaCarinho = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carinho, parent, false);
        return new MyViewHolder(itemListaCarinho);
    }

    @Override
    public void onBindViewHolder(@NonNull CarinhoAdapter.MyViewHolder holder, int position) {

        idUtilizador = PerfilFirebase.getIdUtilizador();

        ItemCarinho itemCarinho = itemCarinhos.get(position);
        holder.carinhoDescricao.setText(itemCarinho.getDescricaoProduto());
        holder.carinhoPreco.setText(itemCarinho.getPreco() + "$00");
        holder.carinhoQuantidade.setText(itemCarinho.getQuantidade());
        holder.carinhoQuantidade.setFocusable(false);
        holder.iconClose.setBackgroundColor(0xFFFF0000);

        Uri url = Uri.parse(itemCarinho.getImgProduto());
        Glide.with(context).load(url).into(holder.carinhoImg);

        holder.carinhoQuantidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Quantidade");
                builder.setMessage("Digite a quantidade");

                EditText quantidadeEdit = new EditText(view.getContext());
                quantidadeEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
                quantidadeEdit.setText(itemCarinho.getQuantidade());

                builder.setView(quantidadeEdit);

                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String quantidade = quantidadeEdit.getText().toString();

                        DatabaseReference databaseReference = FirebaseConfig.getReference();
                        DatabaseReference itemCarinhoRef = databaseReference
                                .child("carinho")
                                .child(idUtilizador)
                                .child(itemCarinho.getIdProduto());

                        itemCarinhoRef.child("quantidade").setValue(quantidade);

                        message(builder.getContext(), "Quantidade aumentado");

                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        holder.iconClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                eliminarItemCarinho(idUtilizador, itemCarinho.getIdProduto());

                message(view.getContext(), "Item removido");

            }
        });

        qtdItensCarinho = 0;
        totalPrecoCarinho = 0;

        for (ItemCarinho itensCarinho : itemCarinhos) {

            int quantidade = Integer.parseInt(itensCarinho.getQuantidade());
            int preco = Integer.parseInt(itensCarinho.getPreco());

            totalPrecoCarinho += (quantidade * preco);
            qtdItensCarinho += quantidade;

        }

        total.setText(Integer.toString(totalPrecoCarinho) + "$00");

    }

    @Override
    public int getItemCount() {
        return itemCarinhos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView carinhoDescricao;
        TextView carinhoPreco;
        ImageView carinhoImg;
        ImageButton iconClose;
        EditText carinhoQuantidade;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            carinhoDescricao = itemView.findViewById(R.id.itemProdutoDescricao);
            carinhoPreco = itemView.findViewById(R.id.itemProdutoPreco);
            carinhoImg = itemView.findViewById(R.id.itemProdutoImg);
            carinhoQuantidade = itemView.findViewById(R.id.itemProdutoQt);
            iconClose = itemView.findViewById(R.id.iconClose);

        }
    }

    private void eliminarItemCarinho(String idUser, String idProduto) {

        DatabaseReference databaseReference = FirebaseConfig.getReference();
        DatabaseReference itemCarinhoRef = databaseReference
                .child("carinho")
                .child(idUser)
                .child(idProduto);

        // Chame o método removeValue() para eliminar todos os itens do carrinho
        itemCarinhoRef.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        int t = itemCarinhos.size();

                        if(t == 0){
                            total.setText("0$00");
                        }

                        notifyDataSetChanged();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}
