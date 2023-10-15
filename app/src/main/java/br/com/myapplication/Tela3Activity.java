package br.com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Tela3Activity extends AppCompatActivity {

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navegationbar3);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_option1) {
                // Navegue para MainActivity
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (item.getItemId() == R.id.menu_option2) {
                // Navegue para Tela2Activity
                Intent intent = new Intent(this, Tela2Activity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (item.getItemId() == R.id.menu_option3) {
                // Já está na Tela3Activity, não é necessário fazer nada
                return true;
            }
            return false;
        });
    }
}



