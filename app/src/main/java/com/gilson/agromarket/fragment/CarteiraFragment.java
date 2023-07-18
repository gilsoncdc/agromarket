package com.gilson.agromarket.fragment;

import static com.gilson.agromarket.config.Helper.message;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gilson.agromarket.R;
import com.gilson.agromarket.adapter.CarinhoAdapter;
import com.gilson.agromarket.adapter.CarteiraAdapter;
import com.gilson.agromarket.config.FirebaseConfig;
import com.gilson.agromarket.config.Helper;
import com.gilson.agromarket.config.PerfilFirebase;
import com.gilson.agromarket.model.Carteira;
import com.gilson.agromarket.model.ItemCarinho;
import com.gilson.agromarket.model.Movimentacao;
import com.gilson.agromarket.model.Utilizador;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CarteiraFragment extends Fragment {

    private DatabaseReference databaseReference;
    private RecyclerView recyclerCarteira;
    private CarteiraAdapter carteiraAdapter;
    List<Movimentacao> listaMovimentacaoCarteiras = new ArrayList<>();
    private TextView saldoCarteira, contaCarteira;
    private Button botaoCarregar;
    private String idUtilizador;
    private Utilizador utilizador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_carteira, container, false);

        databaseReference = FirebaseConfig.getReference();
        idUtilizador = PerfilFirebase.getIdUtilizador();

        saldoCarteira = view.findViewById(R.id.saldoDisp);
        contaCarteira = view.findViewById(R.id.numConta);
        botaoCarregar = view.findViewById(R.id.btn_carteira);

        carteiraAdapter = new CarteiraAdapter(listaMovimentacaoCarteiras, getActivity());

        recyclerCarteira = view.findViewById(R.id.listaMovimento);
        recyclerCarteira.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerCarteira.setHasFixedSize(true);
        recyclerCarteira.setAdapter(carteiraAdapter);

        botaoCarregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Carregamento da sua conta");
                builder.setMessage("Digite a quantidade saldo a acresecentar");

                EditText saldoText = new EditText(view.getContext());
                saldoText.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(saldoText);

                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String saldoDigitado = saldoText.getText().toString();
                        String descricaoMovimento = "Carregamento saldo";

                        if (Integer.parseInt(saldoDigitado) < 500){

                            message(getContext(), "Mínimo 500$00 para adicionar");

                        }else{

                            Calendar calendar = Calendar.getInstance();
                            Date currentDate = calendar.getTime();

                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            String dataHoje = dateFormat.format(currentDate);

                            //************************************************************************************
                            DatabaseReference movRef = databaseReference
                                    .child("carteira")
                                    .child(idUtilizador)
                                    .child("movimentacao").push();

                            String ch = movRef.getKey();
                            movRef.child(ch);

                            Movimentacao movimentacao = new Movimentacao();
                            movimentacao.setIdMovimentacao(ch);
                            movimentacao.setDataMovimentacao(dataHoje);
                            movimentacao.setDescricaoMovimento(descricaoMovimento);
                            movimentacao.setSaldoMovimentacao(saldoDigitado);

                            movRef.setValue(movimentacao).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    int saldoCarteiraDisponivel = Integer.parseInt(saldoCarteira.getText().toString());
                                    int totalSaldoNovo = saldoCarteiraDisponivel + Integer.parseInt(saldoDigitado);

                                    DatabaseReference atualizarSaldoRef = databaseReference
                                            .child("carteira")
                                            .child(idUtilizador);

                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put("saldo", Integer.toString(totalSaldoNovo));

                                    atualizarSaldoRef.updateChildren(updates);

                                    message(getContext(), "Carregamento feito com sucesso");

                                }
                            });

                        }

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

        recuperarDadosCarteira();
        recuperarMovimentoCarteira();
        dataUtilizador();

        // Inflate the layout for this fragment
        return view;

    }

    private void recuperarDadosCarteira() {

        if (databaseReference != null) {

            DatabaseReference carteiraRef = databaseReference
                    .child("carteira")
                    .child(idUtilizador);

            carteiraRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {

                        String saldo = snapshot.child("saldo").getValue(String.class);
                        String numConta = snapshot.child("numeroConta").getValue(String.class);

                        saldoCarteira.setText(saldo);
                        contaCarteira.setText(numConta);

                    }else{
                        saldoCarteira.setText("0");
                        contaCarteira.setText("000000");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }
    }

    private void recuperarMovimentoCarteira() {

        if (databaseReference != null) {

            DatabaseReference movimentacaoRef = databaseReference
                    .child("carteira")
                    .child(idUtilizador)
                    .child("movimentacao");

            movimentacaoRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listaMovimentacaoCarteiras.clear();

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        listaMovimentacaoCarteiras.add(ds.getValue(Movimentacao.class));
                    }

                    carteiraAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }
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

}