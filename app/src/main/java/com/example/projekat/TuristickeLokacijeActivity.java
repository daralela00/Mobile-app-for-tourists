package com.example.projekat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Room;

public class TuristickeLokacijeActivity extends AppCompatActivity {

    private RecyclerView rvTuristickeLokacije;
    private LokacijaAdapter adapter;
    private Button btnNazad;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_turisticke_lokacije);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnNazad = findViewById(R.id.btnNazad2);

        btnNazad.setOnClickListener(v -> {
            finish();
        });

        rvTuristickeLokacije = findViewById(R.id.rvTuristickeLokacije);
        rvTuristickeLokacije.setLayoutManager(new LinearLayoutManager(this));

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "lokacije-db").allowMainThreadQueries().build();

        db.lokacijaDao().getAllLokacije().observe(this, lista -> {
            adapter.setLista(lista);
        });

//        if (db.lokacijaDao().getAllLokacije().getValue() == null || db.lokacijaDao().getAllLokacije().getValue().isEmpty()) {
//            db.lokacijaDao().insert(new Lokacija("Kalemegdan, Srbija", 44.8231, 20.4500));
//            db.lokacijaDao().insert(new Lokacija("Hram Svetog Save, Srbija", 44.8176, 20.4569));
//            db.lokacijaDao().insert(new Lokacija("Petrovaradinska tvrđava, Srbija", 45.2450, 19.8486));
//            db.lokacijaDao().insert(new Lokacija("Avalski toranj, Srbija", 44.7200, 20.5561));
//            db.lokacijaDao().insert(new Lokacija("Narodno pozorište, Srbija", 44.8172, 20.4560));
//            db.lokacijaDao().insert(new Lokacija("Norilsk, Rusija", 69.3550, 88.1893));
//            db.lokacijaDao().insert(new Lokacija("Karamay, Kina", 45.5900, 84.8500));
//            db.lokacijaDao().insert(new Lokacija("Mawsynram, Indija", 25.2967, 91.5850));
//            db.lokacijaDao().insert(new Lokacija("Malabo, Ekvatorijalna Gvineja", 3.7520, 8.7830));
//            db.lokacijaDao().insert(new Lokacija("Ulaanbaatar, Mongolija", 47.8864, 106.9057));
//            db.lokacijaDao().insert(new Lokacija("Ajfelov toranj, Francuska", 48.8584, 2.2945));
//            db.lokacijaDao().insert(new Lokacija("Statua slobode, SAD", 40.6892, -74.0445));
//            db.lokacijaDao().insert(new Lokacija("Piramide u Gizi, Egipat", 29.9792, 31.1342));
//            db.lokacijaDao().insert(new Lokacija("Table Mountain, Južna Afrika", -33.9628, 18.4098));
//            db.lokacijaDao().insert(new Lokacija("Serengeti National Park, Tanzanija", -2.3333, 34.8333));
//            db.lokacijaDao().insert(new Lokacija("Victoria Falls, Zimbabve/Zambija", -17.9243, 25.8567));
//            db.lokacijaDao().insert(new Lokacija("Mount Kilimanjaro, Tanzanija", -3.0674, 37.3556));
//        }

        adapter = new LokacijaAdapter(new ArrayList<>(), lokacija -> {

            OpcijeLokacijeFragment fragment =
                    new OpcijeLokacijeFragment(
                            lokacija.getNaziv(),
                            lokacija.getGeoSirina(),
                            lokacija.getGeoDuzina()
                    );

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.FragmentKontenjer, fragment)
                    .addToBackStack(null)
                    .commit();

        });

        rvTuristickeLokacije.setAdapter(adapter);
    }
}