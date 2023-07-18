package com.gilson.agromarket.config;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.gilson.agromarket.model.Utilizador;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class PerfilFirebase {

    public static FirebaseUser getUtilizadorAtual(){

        FirebaseAuth firebaseAuth = FirebaseConfig.getFirebaseAuth();
        return firebaseAuth.getCurrentUser();
    }

    public static void atualizarNomeUtilizador(String nome){

        try {

            // utilizador logado
            FirebaseUser utilizadorLogado = getUtilizadorAtual();

            // configuração do ojeto para a alteração do perfil
            UserProfileChangeRequest perfil = new UserProfileChangeRequest.Builder().setDisplayName(nome).build();

            utilizadorLogado.updateProfile(perfil).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar o nome perfil");
                    }
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void atualizarFotoUtilizador(Uri url){

        try {

            // utilizador logado
            FirebaseUser utilizadorLogado = getUtilizadorAtual();

            // configuração do ojeto para a alteração do perfil
            UserProfileChangeRequest perfil = new UserProfileChangeRequest.Builder().setPhotoUri(url).build();

            utilizadorLogado.updateProfile(perfil).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar a foto do perfil");
                    }
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static String getIdUtilizador(){
        return getUtilizadorAtual().getUid();
    }

    public static Utilizador getDadosUtilizadorLogado(){

        FirebaseUser firebaseUser = getUtilizadorAtual();

        Utilizador utilizador = new Utilizador();
        utilizador.setId(firebaseUser.getUid());
        utilizador.setEmail(firebaseUser.getEmail());
        utilizador.setNome(firebaseUser.getDisplayName());

        if (firebaseUser.getPhotoUrl() == null){
            utilizador.setFoto("");
        }else{
            utilizador.setFoto(firebaseUser.getPhotoUrl().toString());
        }

        return utilizador;

    }

}
