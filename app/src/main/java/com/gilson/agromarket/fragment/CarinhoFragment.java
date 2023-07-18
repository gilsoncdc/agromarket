package com.gilson.agromarket.fragment;

import static com.gilson.agromarket.config.Helper.message;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gilson.agromarket.Activity.MainActivity;
import com.gilson.agromarket.Activity.PagamentoActivity;
import com.gilson.agromarket.R;
import com.gilson.agromarket.adapter.CarinhoAdapter;
import com.gilson.agromarket.adapter.ProdutoAdapter;
import com.gilson.agromarket.config.FirebaseConfig;
import com.gilson.agromarket.config.PerfilFirebase;
import com.gilson.agromarket.config.RecyclerItemClickListener;
import com.gilson.agromarket.model.Carteira;
import com.gilson.agromarket.model.ItemCarinho;
import com.gilson.agromarket.model.Produto;
import com.gilson.agromarket.model.Utilizador;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CarinhoFragment extends Fragment {

    private RecyclerView recyclerItem;
    private CarinhoAdapter carinhoAdapter;
    List<ItemCarinho> itemCarinho = new ArrayList<>();
    private DatabaseReference databaseReference;
    private String idUtilizador;
    private TextView precoTotal;
    private Button botaoConfirma;
    MainActivity mainActivity;
    private Utilizador utilizador;
    private Carteira carteira;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_carinho, container, false);

        mainActivity = (MainActivity) getActivity();

        databaseReference = FirebaseConfig.getReference();
        idUtilizador = PerfilFirebase.getIdUtilizador();

        precoTotal = view.findViewById(R.id.totalCompra);
        botaoConfirma = view.findViewById(R.id.btnComprar);

        carinhoAdapter = new CarinhoAdapter(itemCarinho, getActivity(), precoTotal);

        recyclerItem = view.findViewById(R.id.listaCarinho);
        recyclerItem.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerItem.setHasFixedSize(true);
        recyclerItem.setAdapter(carinhoAdapter);

        botaoConfirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), PagamentoActivity.class);

                String t = precoTotal.getText().toString();
                String totalCheck = t.substring(0, t.length() - 3);

                if (Integer.parseInt(totalCheck) > 0 &&  !itemCarinho.isEmpty()){

                    intent.putExtra("nome_cliente",utilizador.getNome());
                    intent.putExtra("n_conta", carteira.getNumeroConta());
                    intent.putExtra("preco_total", totalCheck);
                    intent.putExtra("saldoTotalCarteira", carteira.getSaldo());
                    intent.putExtra("lista_itemProduto", (Serializable) itemCarinho);
                    startActivity(intent);

                }else
                    message(getActivity(), "O carinho está vazio");

            }
        });


        //recuperar os dados
        recuperarDadosItem();
        dataUtilizador();
        dataCarteira();

        // Inflate the layout for this fragment
        return view;

    }

    private void recuperarDadosItem() {

        if (databaseReference != null) {

            DatabaseReference produtoRef = databaseReference
                    .child("carinho")
                    .child(idUtilizador);

            produtoRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    itemCarinho.clear();

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        itemCarinho.add(ds.getValue(ItemCarinho.class));
                    }

                    carinhoAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public TextView getPrecoTotal() {
        return precoTotal;
    }

    private void dataUtilizador() {

        DatabaseReference utilizadorRef = databaseReference
                .child("utilizador")
                .child(idUtilizador);

        utilizadorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null){
                    utilizador = snapshot.getValue(Utilizador.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void dataCarteira() {

        DatabaseReference utilizadorRef = databaseReference
                .child("carteira")
                .child(idUtilizador);

        utilizadorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null){
                    carteira = snapshot.getValue(Carteira.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}