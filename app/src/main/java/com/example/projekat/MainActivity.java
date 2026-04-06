package com.example.projekat;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnTuristickeLokacije;
    private Button btnOmiljeneLokacije;
    private Button btnManipulacijaLokacijama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTuristickeLokacije = findViewById(R.id.btnTuristickeLokacije);
        btnOmiljeneLokacije = findViewById(R.id.btnOmiljeneLokacije);
        btnManipulacijaLokacijama = findViewById(R.id.btnManipulacijaLokacija);

        btnTuristickeLokacije.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, TuristickeLokacijeActivity.class);
            startActivity(i);
        });

        btnOmiljeneLokacije.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, OmiljeneLokacijeActivity.class);
            startActivity(i);
        });

        btnManipulacijaLokacijama.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, ManipulacijaLokacijamaActivity.class);
            startActivity(i);
        });

    }
}