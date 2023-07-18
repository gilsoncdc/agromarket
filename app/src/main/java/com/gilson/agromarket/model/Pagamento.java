package com.gilson.agromarket.model;

import static com.gilson.agromarket.config.Helper.message;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.gilson.agromarket.Activity.PagamentoActivity;
import com.gilson.agromarket.config.FirebaseConfig;
import com.gilson.agromarket.fragment.CarinhoFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Pagamento {

    private String idPagamento;
    private String idUtilizador;
    private String clinete;
    private String endereco;
    private String telefone;
    private String precoTotal;
    private List<ItemCarinho> itemCompra;

    public Pagamento() {
    }

    public Pagamento(String idUtilizador) {

        setIdUtilizador(idUtilizador);

        DatabaseReference firebaseRef = FirebaseConfig.getReference();
        DatabaseReference pagamentoRef = firebaseRef
                .child("pagamento")
                .child(idUtilizador);

        setIdPagamento(pagamentoRef.push().getKey());

    }

    public void salvar(String p, int saldo){

        DatabaseReference firebaseRef = FirebaseConfig.getReference();
        DatabaseReference pagamentoRef = firebaseRef
                .child("pagamento")
                .child(getIdUtilizador());

        DatabaseReference novoPag = pagamentoRef.child(getIdPagamento());

        novoPag.setValue(this).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                DatabaseReference itemCarinhoRef = firebaseRef
                        .child("carinho")
                        .child(getIdUtilizador());

                itemCarinhoRef.removeValue();

                movimentacaoCompra(p, saldo);

            }
        });

    }

    public void movimentacaoCompra (String totalDebitar, int saldoCarteira){

        String descricaoMovimento = "Retirada do saldo";

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dataHoje = dateFormat.format(currentDate);

        //************************************************************************************
        DatabaseReference firebaseRef = FirebaseConfig.getReference();
        DatabaseReference movRef = firebaseRef
                .child("carteira")
                .child(idUtilizador)
                .child("movimentacao").push();

        String ch = movRef.getKey();
        movRef.child(ch);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setIdMovimentacao(ch);
        movimentacao.setDataMovimentacao(dataHoje);
        movimentacao.setDescricaoMovimento(descricaoMovimento);
        movimentacao.setSaldoMovimentacao(totalDebitar);

        movRef.setValue(movimentacao).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                int totalSaldoNovo = saldoCarteira - Integer.parseInt(totalDebitar);

                DatabaseReference atualizarSaldoRef = firebaseRef
                        .child("carteira")
                        .child(idUtilizador);

                Map<String, Object> updates = new HashMap<>();
                updates.put("saldo", Integer.toString(totalSaldoNovo));

                atualizarSaldoRef.updateChildren(updates);

            }
        });

    }

    public String getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(String idPagamento) {
        this.idPagamento = idPagamento;
    }

    public String getIdUtilizador() {
        return idUtilizador;
    }

    public void setIdUtilizador(String idUtilizador) {
        this.idUtilizador = idUtilizador;
    }

    public String getClinete() {
        return clinete;
    }

    public void setClinete(String clinete) {
        this.clinete = clinete;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(String precoTotal) {
        this.precoTotal = precoTotal;
    }

    public List<ItemCarinho> getItemCompra() {
        return itemCompra;
    }

    public void setItemCompra(List<ItemCarinho> itemCompra) {
        this.itemCompra = itemCompra;
    }
}
