package br.com.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class FormLogin extends AppCompatActivity {

    private TextView text_tela_cadastro;
    private EditText edit_email, edit_password;
    private Button bt_entrar, bt_biometric;
    private ProgressBar progressBar;
    String[] messages = {"Preencha todos os campos!"};

    private boolean useBiometric = false; // Variável para rastrear se o usuário optou por usar a autenticação biométrica

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        StartComponents();

        text_tela_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormLogin.this, FormCadastro.class);
                startActivity(intent);
            }
        });

        bt_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edit_email.getText().toString();
                String password = edit_password.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(v, messages[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else {
                    AuthenticateUser(v);
                }
            }
        });

        bt_biometric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Alternar entre usar ou não a autenticação biométrica
                useBiometric = !useBiometric;
                // Atualizar a interface do usuário para refletir a escolha do usuário (por exemplo, alterar a cor do botão)
                updateBiometricButtonState();
            }
        });

        // Configurar autenticação biométrica
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(FormLogin.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                // Tratar erros de autenticação biométrica, se necessário
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                if (useBiometric) {
                    // A autenticação biométrica foi bem-sucedida e o usuário optou por usá-la
                    MainActivity();
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                if (useBiometric) {
                    // A autenticação biométrica falhou e o usuário optou por usá-la
                    // Trate conforme necessário
                }
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login biométrico para meu aplicativo")
                .setSubtitle("Faça login usando sua credencial biométrica")
                .setNegativeButtonText("Cancelar")
                .build();
    }

    private void AuthenticateUser(View view) {
        String email = edit_email.getText().toString();
        String password = edit_password.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressBar.setVisibility(View.VISIBLE);

                new Handler().postDelayed(() -> {
                    if (useBiometric) {
                        // Autenticação bem-sucedida com email/senha e o usuário optou por usar a autenticação biométrica
                        biometricPrompt.authenticate(promptInfo);
                    } else {
                        // Autenticação bem-sucedida com email/senha e o usuário optou por não usar a autenticação biométrica
                        MainActivity();
                    }
                }, 3000);
            } else {
                String error;

                try {
                    throw task.getException();
                } catch (Exception e) {
                    error = "Erro ao logar usuário";
                }
                Snackbar snackbar = Snackbar.make(view, error, Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // O usuário já está autenticado com email/senha
            if (useBiometric) {
                // O usuário optou por usar a autenticação biométrica
                biometricPrompt.authenticate(promptInfo);
            } else {
                MainActivity();
            }
        }
    }

    private void MainActivity() {
        Intent intent = new Intent(FormLogin.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void StartComponents() {
        text_tela_cadastro = findViewById(R.id.text_tela_cadastro);
        edit_email = findViewById(R.id.edit_email);
        edit_password = findViewById(R.id.edit_senha);
        bt_entrar = findViewById(R.id.bt_entrar);
        bt_biometric = findViewById(R.id.bt_biometric); // Botão para alternar a autenticação biométrica
        progressBar = findViewById(R.id.progressbar);
        updateBiometricButtonState(); // Atualize o estado do botão biométrico
    }

    private void updateBiometricButtonState() {
        // Atualize o texto ou a cor do botão biométrico com base na escolha do usuário
        if (useBiometric) {
            bt_biometric.setText("Desativar Autenticação Biométrica");
            bt_biometric.setBackgroundColor(Color.GREEN);
        } else {
            bt_biometric.setText("Ativar Autenticação Biométrica");
            bt_biometric.setBackgroundColor(Color.RED);
        }
    }
}





