package br.com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Tela2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navegationbar2);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_option1) {
                // Navegue para MainActivity
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (item.getItemId() == R.id.menu_option2) {
                // Já está na Tela2Activity, não é necessário fazer nada
                return true;
            } else if (item.getItemId() == R.id.menu_option3) {
                // Navegue para Tela3Activity
                Intent intent = new Intent(this, Tela3Activity.class);
                startActivity(intent);
                finish();
                return true;
            }
            return false;
        });
    }
}
