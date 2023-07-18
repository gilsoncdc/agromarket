package com.gilson.agromarket.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gilson.agromarket.R;
import com.gilson.agromarket.config.FirebaseConfig;
import com.gilson.agromarket.config.PerfilFirebase;
import com.gilson.agromarket.model.Utilizador;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarPerfilActivity extends AppCompatActivity {

    private CircleImageView fotoPerfil;
    private TextView textFoto;
    private EditText atualizarNome, atualizarEmail;
    private Button botaoAtualizar;
    private Utilizador utilizadorLogado;
    private static final int SELECAO_GALERIA = 200;
    private StorageReference storageReference;
    private String idUtilizador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        //utilizador logado
        utilizadorLogado = PerfilFirebase.getDadosUtilizadorLogado();
        storageReference = FirebaseConfig.getStorageReference();
        idUtilizador = PerfilFirebase.getIdUtilizador();

        //configurar toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("AgroMarket");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_close);

        fotoPerfil = findViewById(R.id.editarFoto);
        textFoto = findViewById(R.id.textEditarFoto);
        atualizarNome = findViewById(R.id.editarNome);
        atualizarEmail = findViewById(R.id.edtitarEmail);
        textFoto = findViewById(R.id.textEditarFoto);
        botaoAtualizar = findViewById(R.id.btnEditarPerfil);
        atualizarEmail.setFocusable(false);

        //recuperar os dados
        FirebaseUser perfilUtilizador = PerfilFirebase.getUtilizadorAtual();
        atualizarNome.setText(perfilUtilizador.getDisplayName());
        atualizarEmail.setText(perfilUtilizador.getEmail());

        Uri url = perfilUtilizador.getPhotoUrl();
        if (url != null){

            Glide.with(EditarPerfilActivity.this)
                    .load(url)
                    .into(fotoPerfil);

        }else{

            fotoPerfil.setImageResource(R.drawable.avatar);

        }

        botaoAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nomeEditado = atualizarNome.getText().toString();

                //atualizar nome no perfil firebase
                PerfilFirebase.atualizarNomeUtilizador(nomeEditado);

                //atualizar nome no database real do firebase
                utilizadorLogado.setNome(nomeEditado);
                utilizadorLogado.atualizar();

                message("Dados Atualizado com sucesso");

            }
        });

        textFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (intent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(intent, SELECAO_GALERIA);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            Bitmap img = null;

            try {

                //seleção da foto da galeria
                switch (requestCode){
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        img = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
                }

                // se for escolheido uma imagem na galeria
                if (img != null){
                    //configurar imagem na tela
                    fotoPerfil.setImageBitmap(img);

                    //recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    img.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImg = baos.toByteArray();


                    //salvar imagem no firebase
                    StorageReference imageRef = storageReference
                            .child("imagens")
                            .child("perfil")
                            .child(idUtilizador + ".jpg");

                    UploadTask uploadTask = imageRef.putBytes(dadosImg);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("Erro", e.getMessage());
                            message("Erro ao fazer upload da imagem");
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //recuperar o local da foto
                            imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                   Uri url = task.getResult();

                                   atualizarFotoUtilizador(url);

                                }
                            });

                            message("Sucesso ao fazer upload da imagem");

                        }
                    });

                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void atualizarFotoUtilizador(Uri url) {

        //atualizar foto no perfil
        PerfilFirebase.atualizarFotoUtilizador(url);

        //atualizar foto no firebase
        utilizadorLogado.setFoto(url.toString());
        utilizadorLogado.atualizar();

        message("Sua foto foi atualizado com sucesso");

    }

    // subscrever a função voltar ao main pa simplesmente fechar a activity EditarPerfil
    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return false;

    }

    public void message(String sms){
        Toast.makeText(EditarPerfilActivity.this, sms, Toast.LENGTH_LONG).show();
    }

}