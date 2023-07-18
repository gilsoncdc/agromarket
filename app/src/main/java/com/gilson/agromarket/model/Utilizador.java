package com.gilson.agromarket.model;

import com.gilson.agromarket.config.FirebaseConfig;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Utilizador {

    private String id;
    private String nome;
    private String email;
    private String password;
    private String foto;

    public Utilizador() {
    }

    public void salvar(){

        DatabaseReference databaseReference = FirebaseConfig.getReference();
        DatabaseReference utilizadorReference = databaseReference.child("utilizador").child(getId());

        utilizadorReference.setValue(this);

    }

    public void atualizar(){

        DatabaseReference databaseReference = FirebaseConfig.getReference();
        DatabaseReference utilizadorRef = databaseReference.child("utilizador").child(getId());

        //realizar atualização de todos os inputs de uma so vez com o objecto
        Map<String, Object> dataUtilizador = converterMap();

        utilizadorRef.updateChildren(dataUtilizador);

    }

    //converter uma converter obleto utilizador em hash map
    public Map<String, Object> converterMap(){

        HashMap<String, Object> utilizadorMap = new HashMap<>();
        utilizadorMap.put("email", getEmail());
        utilizadorMap.put("nome", getNome());
        utilizadorMap.put("id", getId());
        utilizadorMap.put("foto", getFoto());

        return  utilizadorMap;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude // excluir a senha na hora de cadastrar no BD
    public String getPassword() {
        return password;
    }

    public void setPassword(String senha) {
        this.password = senha;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

}
