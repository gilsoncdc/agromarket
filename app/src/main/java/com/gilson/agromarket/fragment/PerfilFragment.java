package com.gilson.agromarket.fragment;

import static com.gilson.agromarket.config.Helper.message;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gilson.agromarket.Activity.EditarPerfilActivity;
import com.gilson.agromarket.R;
import com.gilson.agromarket.adapter.PerfilAdapter;
import com.gilson.agromarket.config.FirebaseConfig;
import com.gilson.agromarket.config.PerfilFirebase;
import com.gilson.agromarket.config.RecyclerItemClickListener;
import com.gilson.agromarket.model.Utilizador;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;


public class PerfilFragment extends Fragment {

    private TextView nomePerfil, emailPerfil, compraPerfil, totalGastoPerfil;
    ImageView fotoPerfil;
    private Button botaoPerfil;

    private String idUtilizador;
    private DatabaseReference databaseReference;
    private Utilizador utilizador;

    String descricao [] = new String[] {"Informações do Utilizador", "Carinho de Compras", "Meus Favoritos", "Carteira Eletronica", "Sair"};
    int icon [] = { R.drawable.icon_perfil, R.drawable.icon_cart, R.drawable.icon_favorite, R.drawable.icon_carteira, R.drawable.icon_logout };

    ListView listPerfilView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        databaseReference = FirebaseConfig.getReference();
        idUtilizador = PerfilFirebase.getIdUtilizador();

        listPerfilView = view.findViewById(R.id.listViewPerfil);
        PerfilAdapter perfilAdapter = new PerfilAdapter(getActivity(), descricao, icon);
        listPerfilView.setAdapter(perfilAdapter);

        listPerfilView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                message(getContext(), descricao[i]);

            }
        });

        nomePerfil = view.findViewById(R.id.nomeUtilizador);
        emailPerfil = view.findViewById(R.id.emailUtilizador);
        fotoPerfil = view.findViewById(R.id.fotoUtilizador);
        compraPerfil = view.findViewById(R.id.compra);
        totalGastoPerfil = view.findViewById(R.id.totalGasto);
        botaoPerfil = view.findViewById(R.id.btnEditarPerfil);

        FirebaseUser perfilUtilizador = PerfilFirebase.getUtilizadorAtual();
        nomePerfil.setText(perfilUtilizador.getDisplayName());
        emailPerfil.setText(perfilUtilizador.getEmail());

        //saldoPerfil.setText(utilizador.getSaldo() + "$00");

        Uri url = perfilUtilizador.getPhotoUrl();
        if (url != null){

            Glide.with(PerfilFragment.this)
                    .load(url)
                    .into(fotoPerfil);

        }else{
            fotoPerfil.setImageResource(R.drawable.avatar);
        }

        botaoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), EditarPerfilActivity.class);
                startActivity(intent);

            }
        });

        return  view;

    }

    /*private void dataUtilizador() {

        DatabaseReference utilizadorRef = databaseReference
                .child("utilizador")
                .child(idUtilizador)
                .child("saldo");

        utilizadorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getValue() != null){
                    saldoPerfil.setText(snapshot.getValue()+"$00");
                }else{
                    saldoPerfil.setText("0$00");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

}