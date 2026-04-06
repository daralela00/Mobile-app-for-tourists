package com.example.projekat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.Set;

public class OmiljeneLokacijeActivity extends AppCompatActivity {

    private TextView tvOmiljeneLokacije;
    private Button btnNazad;
    private static final String PREFS_NAME = "OmiljeneLokacije";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_omiljene_lokacije);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvOmiljeneLokacije = findViewById(R.id.tvOmiljeneLokacije);
        btnNazad = findViewById(R.id.btnNazad4);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Set<String> omiljeneSet = prefs.getStringSet("omiljene", new HashSet<>());

        StringBuilder sb = new StringBuilder();
        for (String naziv : omiljeneSet) {
            sb.append("• ").append(naziv).append("\n\n\n");
        }
        tvOmiljeneLokacije.setText(sb.toString());

        btnNazad.setOnClickListener(v -> {
            finish();
        });
    }
}