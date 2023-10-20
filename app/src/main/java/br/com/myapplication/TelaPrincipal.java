package br.com.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import de.hdodenhof.circleimageview.CircleImageView;

public class TelaPrincipal extends AppCompatActivity {

    private TextView nomeUsuario, emailUsuario;
    private Button bt_deslogar, bt_alterarFoto;
    private CircleImageView imgPerfil;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;

    public void voltarTelaAnterio(View view) {
        // Crie um Intent para iniciar a tela principal
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        // Finalize a atividade atual (tela de configurações) se você não deseja que ela permaneça na pilha de atividades
        finish();
    }

    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private static final int GALLERY_INTENT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        IniciarComponentes();

        // Solicita permissão de leitura de armazenamento em tempo de execução
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_INTENT);
        }

        bt_deslogar.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(TelaPrincipal.this, FormLogin.class);
            startActivity(intent);
            finish();
        });

        // Configura o botão "bt_alterarFoto" para abrir a galeria de imagens
        bt_alterarFoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_INTENT);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri imagemSelecionada = data.getData();
                Log.d("DEBUG", "Imagem selecionada: " + imagemSelecionada.toString());

                // Use o storageReference criado para carregar a imagem
                StorageReference profileImageRef = storageReference.child("profileImages/" + usuarioID);

                profileImageRef.putFile(imagemSelecionada)
                        .addOnSuccessListener(taskSnapshot -> {
                            profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                String fotoPerfilUrl = uri.toString();
                                DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
                                documentReference.update("fotoPerfilUrl", fotoPerfilUrl)
                                        .addOnSuccessListener(aVoid -> {
                                            carregarImagemPerfil(fotoPerfilUrl);
                                            Toast.makeText(this, "Foto de perfil atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(this, "Erro ao atualizar a foto de perfil.", Toast.LENGTH_SHORT).show();
                                        });
                            });
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Erro ao carregar a foto de perfil.", Toast.LENGTH_SHORT).show();
                        });
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String email = currentUser.getEmail();
            usuarioID = currentUser.getUid();

            DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    if (documentSnapshot != null) {
                        nomeUsuario.setText(documentSnapshot.getString("nome"));
                        emailUsuario.setText(email);

                        // Suponhamos que você tenha uma URL de imagem no Firestore
                        String fotoPerfilUrl = documentSnapshot.getString("fotoPerfilUrl");

                        // Verifica se o usuário tem uma foto de perfil
                        if (fotoPerfilUrl != null && !fotoPerfilUrl.isEmpty()) {
                            // Use o Glide para carregar a imagem do Firebase Storage
                            StorageReference profileImageRef = storageReference.child(fotoPerfilUrl);

                            // Use o método .into() do Glide para carregar a imagem no CircleImageView
                            Glide.with(TelaPrincipal.this)
                                    .load(profileImageRef)
                                    .placeholder(R.drawable.ic_user) // Exibe um ícone padrão enquanto a imagem é carregada
                                    .into(imgPerfil);
                        } else {
                            // Se não houver uma foto de perfil, você pode exibir uma imagem padrão ou ocultar a ImageView
                            imgPerfil.setImageResource(R.drawable.ic_user);
                        }
                    }
                }
            });
        }
    }

    private void IniciarComponentes() {
        nomeUsuario = findViewById(R.id.textNomeUsuario);
        emailUsuario = findViewById(R.id.textEmailUsuario);
        bt_deslogar = findViewById(R.id.bt_deslogar);
        imgPerfil = findViewById(R.id.imgPerfil);
        bt_alterarFoto = findViewById(R.id.bt_alterarFoto);
    }

    private void carregarImagemPerfil(String fotoPerfilUrl) {
        Glide.with(this)
                .load(fotoPerfilUrl)
                .placeholder(R.drawable.ic_user) // Exibe um ícone padrão enquanto a imagem é carregada
                .into(imgPerfil);
    }
}

