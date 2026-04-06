package com.example.projekat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class ManipulacijaLokacijamaActivity extends AppCompatActivity {

    private Spinner spinner;
    private Button btnNazad, btnUnesi, btnObrisi;
    private AppDatabase db;
    private EditText etNaziv, etGSirina, etGDuzina;
    private ArrayAdapter<String> spinnerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manipulacija_lokacijama);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spinner = findViewById(R.id.spinner);
        etNaziv = findViewById(R.id.etNaziv);
        etGSirina = findViewById(R.id.etGSirina);
        etGDuzina = findViewById(R.id.etGDuzina);
        btnUnesi = findViewById(R.id.btnUnesi);
        btnObrisi = findViewById(R.id.btnObrisi);
        btnNazad = findViewById(R.id.btnNazad5);

        btnNazad.setOnClickListener(v -> {
            finish();
        });


        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "lokacije-db").allowMainThreadQueries().build();


        spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new ArrayList<String>()
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);


        db.lokacijaDao().getAllLokacije().observe(this, lista -> {
            spinnerAdapter.clear();
            for (Lokacija l : lista) {
                spinnerAdapter.add(l.getNaziv());
            }
            spinnerAdapter.notifyDataSetChanged();
        });

        btnUnesi.setOnClickListener(v -> {

            String naziv = etNaziv.getText().toString().trim();
            String sSirina = etGSirina.getText().toString().trim();
            String sDuzina = etGDuzina.getText().toString().trim();

            if(naziv.isEmpty() || sSirina.isEmpty() || sDuzina.isEmpty()) {
                Toast.makeText(this, "Sva polja moraju biti popunjena", Toast.LENGTH_SHORT).show();
                return;
            }

            double gSirina = Double.valueOf(sSirina);
            double gDuzina = Double.valueOf(sDuzina);

            Lokacija novaLokacija = new Lokacija(naziv, gSirina, gDuzina);
            db.lokacijaDao().insert(novaLokacija);
            Toast.makeText(this, "Dodato u listu lokacija: " + naziv, Toast.LENGTH_SHORT).show();
        });

        btnObrisi.setOnClickListener(v -> {
            String selektovaniNaziv = (String) spinner.getSelectedItem();
            if (selektovaniNaziv != null) {
                db.lokacijaDao().deleteByNaziv(selektovaniNaziv);
                spinnerAdapter.remove(selektovaniNaziv);
                spinnerAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Obrisano iz lista lokacija: " + selektovaniNaziv, Toast.LENGTH_SHORT).show();
            }
        });
    }
}