package com.gilson.agromarket.Activity;

import static com.gilson.agromarket.config.Helper.message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gilson.agromarket.R;
import com.gilson.agromarket.config.FirebaseConfig;
import com.gilson.agromarket.config.Helper;
import com.gilson.agromarket.config.PerfilFirebase;
import com.gilson.agromarket.model.Carteira;
import com.gilson.agromarket.model.Utilizador;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;

import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    private Button botaoRegistrar;
    private EditText nome, email, pass, passConfirm;

    private Utilizador utilizador;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        verificarLogado();

        botaoRegistrar = findViewById(R.id.btnRegister);
        nome = findViewById(R.id.registerNome);
        email = findViewById(R.id.registerEmail);
        pass = findViewById(R.id.registerPass);
        passConfirm = findViewById(R.id.registerPassConfirm);

        botaoRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textNome = nome.getText().toString();
                String textEmail = email.getText().toString();
                String textPass = pass.getText().toString();
                String textConfirmPass = passConfirm.getText().toString();

                if (!textNome.isEmpty()){

                    if (!textEmail.isEmpty()){

                        if (!textPass.isEmpty()){

                            if (textPass.equals(textConfirmPass)){

                                utilizador = new Utilizador();
                                utilizador.setNome(textNome);
                                utilizador.setEmail(textEmail);
                                utilizador.setPassword(textPass);

                                cadastrar(utilizador);


                            }else{
                                message(getApplicationContext(),"O campo password e Confirmar password não são iguais");
                            }

                        }else {
                            message(getApplicationContext(),"O campo password não pode ser nulo");
                        }

                    }else{
                        message(getApplicationContext(),"O campo email não pode ser nulo");
                    }

                }else{
                    message(getApplicationContext(),"O campo nome não pode ser nulo");
                }
            }
        });

    }

    public void cadastrar(Utilizador utilizador){

        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        firebaseAuth.createUserWithEmailAndPassword(
                utilizador.getEmail(),
                utilizador.getPassword()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    try {

                        //salvar dados do utilizador no firebase
                        String idUtilizador = task.getResult().getUser().getUid();
                        utilizador.setId(idUtilizador);
                        utilizador.salvar();

                        // salvar dados no profil do firebase
                        PerfilFirebase.atualizarNomeUtilizador(utilizador.getNome());

                        //criar conta e associar ao utilizador
                        criarCarteira(idUtilizador);

                        message(getApplicationContext(),"Cadastro com sucesso");

                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else{

                    String erroException = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        erroException = "Entra com uma senha mais forte";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erroException = "Entra com um email válido";
                    }catch (FirebaseAuthUserCollisionException e){
                        erroException = "Esta conta já existe no BD";
                    }catch (Exception e){
                        erroException = "ao cadastrar utitlizador: " +e.getMessage();
                        e.printStackTrace();
                    }

                    message(getApplicationContext(), "Erro: " + erroException);

                }
            }
        });

    }

    private void criarCarteira(String idUtilizador) {

        String conta = gerarNumeroConta();
        String saldo = "0";

        Carteira carteira = new Carteira();
        carteira.setSaldo(saldo);
        carteira.setNumeroConta(conta);
        carteira.setIdUtilizador(idUtilizador);

        DatabaseReference firebaseRef = FirebaseConfig.getReference();
        DatabaseReference carteiraRef = firebaseRef
                .child("carteira")
                .child(idUtilizador);

        carteiraRef.setValue(carteira);

    }

    public void verificarLogado(){
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        if (firebaseAuth.getCurrentUser() != null){
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public static String gerarNumeroConta() {

        // Gerar um número aleatório de 6 dígitos
        Random random = new Random();
        int numero = random.nextInt(900000) + 100000;

        String prefixo = "AC";
        String sufixo = "";

        String numeroConta = prefixo + numero + sufixo;

        return numeroConta;

    }

    public void login(View view){

        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);

    }


}