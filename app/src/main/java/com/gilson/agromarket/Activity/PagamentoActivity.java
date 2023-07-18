package com.gilson.agromarket.Activity;

import static com.gilson.agromarket.config.Helper.message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gilson.agromarket.R;
import com.gilson.agromarket.config.FirebaseConfig;
import com.gilson.agromarket.config.PerfilFirebase;
import com.gilson.agromarket.databinding.FragmentCarteiraBinding;
import com.gilson.agromarket.model.Carteira;
import com.gilson.agromarket.model.ItemCarinho;
import com.gilson.agromarket.model.Pagamento;
import com.gilson.agromarket.model.Utilizador;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PagamentoActivity extends AppCompatActivity {

    private EditText nomeC, enderecoC, telefoneC, precoTotalC, contaC;
    private Button botaoCompra;
    private String idUtilizador;
    private  List<ItemCarinho> itemRecebido = new ArrayList<>();
    private Pagamento pagamento;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento);

        nomeC = findViewById(R.id.nomeCliente);
        enderecoC = findViewById(R.id.enderecoCliente);
        telefoneC = findViewById(R.id.telefoneCliente);
        precoTotalC = findViewById(R.id.precoTotalCliente);
        contaC = findViewById(R.id.contaCliente);
        botaoCompra = findViewById(R.id.btnRealizarCompra);

        nomeC.setFocusable(false);
        precoTotalC.setFocusable(false);
        contaC.setFocusable(false);
        telefoneC.setInputType(InputType.TYPE_CLASS_NUMBER);

        databaseReference = FirebaseConfig.getReference();
        idUtilizador = PerfilFirebase.getIdUtilizador();

        //configurar toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("AgroMarket");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_close);

        //receber dados vindo do carinho compra
        nomeC.setText(getIntent().getExtras().getString("nome_cliente"));
        precoTotalC.setText(getIntent().getExtras().getString("preco_total"));
        contaC.setText(getIntent().getExtras().getString("n_conta"));
        ArrayList<ItemCarinho> itemCarinho = (ArrayList<ItemCarinho>) getIntent().getSerializableExtra("lista_itemProduto");


        botaoCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String enderecoText = enderecoC.getText().toString();
                String telefoneText = telefoneC.getText().toString();
                String nomeText = nomeC.getText().toString();
                String precoTotalCompra = precoTotalC.getText().toString();

                int totalSaldoConta = Integer.parseInt(getIntent().getExtras().getString("saldoTotalCarteira"));

                if (Integer.parseInt(precoTotalCompra) < totalSaldoConta) {

                    if (!enderecoText.isEmpty()) {

                        if (!telefoneText.isEmpty()) {

                            ItemCarinho itenC = new ItemCarinho();
                            for (ItemCarinho item : itemCarinho) {

                                itenC.setDescricaoProduto(item.getDescricaoProduto());
                                itenC.setQuantidade(item.getQuantidade());
                                itenC.setPreco(item.getPreco());
                                itemRecebido.add(itenC);

                            }

                            if (pagamento == null) {
                                pagamento = new Pagamento(idUtilizador);
                            }

                            pagamento.setClinete(nomeText);
                            pagamento.setEndereco(enderecoText);
                            pagamento.setTelefone(telefoneText);
                            pagamento.setPrecoTotal(precoTotalCompra);
                            pagamento.setItemCompra(itemRecebido);
                            pagamento.salvar(precoTotalCompra, totalSaldoConta);

                            message(getApplicationContext(), "Compra realizada com sucesso");

                            finish();

                        } else {
                            message(PagamentoActivity.this, "Telefone não pode ser nulo");
                        }

                    } else {
                        message(PagamentoActivity.this, "Endereco não pode ser nulo");
                    }

                }else{
                    message(PagamentoActivity.this, "Não tens saldo suficiente para realizar a compra");
                }

            }

        });

    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return false;

    }

}