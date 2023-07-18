package com.gilson.agromarket.fragment;

import static com.gilson.agromarket.config.Helper.message;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gilson.agromarket.Activity.MainActivity;
import com.gilson.agromarket.R;
import com.gilson.agromarket.adapter.ProdutoAdapter;
import com.gilson.agromarket.config.FirebaseConfig;
import com.gilson.agromarket.config.PerfilFirebase;
import com.gilson.agromarket.model.ItemCarinho;
import com.gilson.agromarket.model.Produto;
import com.gilson.agromarket.model.Utilizador;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerProduto;
    private ProdutoAdapter produtoAdapter;
    List<Produto> produtos = new ArrayList<>();
    private DatabaseReference databaseReference;
    private String idUtilizador;
    private String idProduto;
    private Utilizador utilizador;
    Button clickHortalica, clickFruta, clickGrao, clickAll;
    private Button botaoSelecionado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        clickHortalica = view.findViewById(R.id.hortalica);
        clickFruta = view.findViewById(R.id.fruta);
        clickGrao = view.findViewById(R.id.grao);
        clickAll =  view.findViewById(R.id.all);

        clickHortalica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selecionarTexto(clickHortalica);
                recuperarDadosProduto("1");
            }
        });

        clickFruta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selecionarTexto(clickFruta);
                recuperarDadosProduto("2");

            }
        });

        clickGrao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selecionarTexto(clickGrao);
                recuperarDadosProduto("3");

            }
        });

        clickAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selecionarTexto(clickAll);
                recuperarDadosProduto("0");

            }
        });

        //********************************
        databaseReference = FirebaseConfig.getReference();
        idUtilizador = PerfilFirebase.getIdUtilizador();
        
        //recuperar dados do utilizador
        dataUtilizador();

        recyclerProduto = view.findViewById(R.id.listaProduto);
        recyclerProduto.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerProduto.setHasFixedSize(true);
        produtoAdapter = new ProdutoAdapter(produtos, getActivity(), new ProdutoAdapter.ClickAddCart() {
            @Override
            public void onClickAddCart(Button botaoAddCart, Produto produto) {
                produto.setAddCart(true);
                //botaoAddCart.setBackgroundColor(0xFFFF0000);
                produtoAdapter.notifyDataSetChanged();

                String quantidade = "1";

                int total = Integer.parseInt(produto.getPreco());

                ItemCarinho itemCarinho = new ItemCarinho();
                itemCarinho.setIdProduto(produto.getId());
                itemCarinho.setDescricaoProduto(produto.getDescricaoProduto());
                itemCarinho.setPreco(produto.getPreco());
                itemCarinho.setImgProduto(produto.getImgProduto());
                itemCarinho.setQuantidade(quantidade);

                gravar(itemCarinho, idUtilizador, produto.getId());
                message(getActivity(), "Item Adicionado");

            }
        });

        recyclerProduto.setAdapter(produtoAdapter);

        //recuperar produto
        recuperarDadosProduto("0");

        return view;

    }

    private void gravar(ItemCarinho itemCarinho, String idUser, String idProduto) {

        DatabaseReference databaseReference = FirebaseConfig.getReference();
        DatabaseReference itemCarinhoRef = databaseReference
                .child("carinho")
                .child(idUser);

        DatabaseReference novoItemRef = itemCarinhoRef.child(idProduto);
        novoItemRef.setValue(itemCarinho);

    }

    //**********************************************************************************************************
    //
    //**********************************************************************************************************
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


    //**********************************************************************************************************
    //
    //**********************************************************************************************************
    private void recuperarDadosProduto(String categoria) {

        DatabaseReference produtoRef = databaseReference
                .child("produto");

        produtoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                produtos.clear();

                for (DataSnapshot ds: snapshot.getChildren()){

                   Produto p = ds.getValue(Produto.class);

                    if (Integer.parseInt(categoria) != 0){

                        if (p.getTipo().equals(categoria)){

                            produtos.add(p);

                        }

                    }else{

                        produtos.add(ds.getValue(Produto.class));

                    }

                }


                produtoAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void selecionarTexto(Button button) {

        int branco = Color.parseColor("#FFFFFF");
        int verde = Color.parseColor("#CCCCCC");

        if (botaoSelecionado != null) {
            botaoSelecionado.setSelected(false);
            botaoSelecionado.setTextColor(branco);
        }

        button.setSelected(true);
        botaoSelecionado = button;
        botaoSelecionado.setTextColor(verde);

    }

}