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
import com.gilson.agromarket.model.Utilizador;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button botaoLogin;
    private EditText email, password;

    private Utilizador utilizador;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarLogado();

        botaoLogin = findViewById(R.id.btnLogin);

        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPass);

        botaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textEmail = email.getText().toString();
                String textPass = password.getText().toString();

                if (!textEmail.isEmpty()){

                    if (!textPass.isEmpty()){

                        utilizador = new Utilizador();
                        utilizador.setEmail(textEmail);
                        utilizador.setPassword(textPass);

                        validarLogin(utilizador);

                    }else{
                        message(getApplicationContext(),"O campo password não pode ser nulo");
                    }

                }else {
                    message(getApplicationContext(),"O campo email não pode ser nulo");
                }

            }
        });

    }

    public void verificarLogado(){
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        if (firebaseAuth.getCurrentUser() != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void validarLogin(Utilizador utilizador) {

        firebaseAuth = FirebaseConfig.getFirebaseAuth();

        firebaseAuth.signInWithEmailAndPassword(
                utilizador.getEmail(),
                utilizador.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                    message(getApplicationContext(), "Erro ao fazer login");

                }
            }
        });

    }


    public void registar(View view){

        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);

    }
}