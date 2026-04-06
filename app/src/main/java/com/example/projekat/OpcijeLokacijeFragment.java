package com.example.projekat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.HashSet;
import java.util.Set;

public class OpcijeLokacijeFragment extends Fragment {

    private String naziv;
    private double geoSirina;
    private double geoDuzina;

    TextView tvInfo;

    Button btnMapa, btnVremenskaPrognoza, btnOmiljeneLokacije, btnSkloni;

    private static final String PREFS_NAME = "OmiljeneLokacije";


    public OpcijeLokacijeFragment(String naziv, double geoSirina, double geoDuzina){
        this.naziv = naziv;
        this.geoSirina = geoSirina;
        this.geoDuzina = geoDuzina;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_opcije_lokacije, container, false);

        tvInfo = view.findViewById(R.id.textViewInfo);
        btnMapa = view.findViewById(R.id.btnMapa);
        btnVremenskaPrognoza = view.findViewById(R.id.btnVremenskaPrognoza);
        btnOmiljeneLokacije = view.findViewById(R.id.btnDodajOduzmiOmiljeneLokacije);
        btnSkloni = view.findViewById(R.id.btnSkloni);

        updateDugmeOmiljene();

        tvInfo.setText(naziv + "\n" + geoSirina + " " + geoDuzina);

        btnVremenskaPrognoza.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), VremenskaPrognozaActivity.class);

            intent.putExtra("naziv", naziv);
            intent.putExtra("geoDuzina", geoDuzina);
            intent.putExtra("geoSirina", geoSirina);

            startActivity(intent);

        });

        btnMapa.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), MapaActivity.class);

            intent.putExtra("naziv", naziv);
            intent.putExtra("geoDuzina", geoDuzina);
            intent.putExtra("geoSirina", geoSirina);

            startActivity(intent);
        });


        btnOmiljeneLokacije.setOnClickListener(v -> {

            if (jeOmiljena(naziv)) {
                ukloniIzOmiljenih(naziv);
                Toast.makeText(getActivity(), "Uklonjeno iz omiljenih: " + naziv, Toast.LENGTH_SHORT).show();
            } else {
                dodajUOmiljene(naziv);
                Toast.makeText(getActivity(), "Dodato u omiljene: " + naziv, Toast.LENGTH_SHORT).show();
            }
            updateDugmeOmiljene();

        });

        btnSkloni.setOnClickListener(v -> {

            requireActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        });

        return view;
    }

    private void dodajUOmiljene(String naziv) {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> omiljeneSet = prefs.getStringSet("omiljene", new HashSet<>());
        Set<String> noviSet = new HashSet<>(omiljeneSet);
        noviSet.add(naziv);
        prefs.edit().putStringSet("omiljene", noviSet).commit();
    }

    private void ukloniIzOmiljenih(String naziv) {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> omiljeneSet = prefs.getStringSet("omiljene", new HashSet<>());
        Set<String> noviSet = new HashSet<>(omiljeneSet);
        noviSet.remove(naziv);
        prefs.edit().putStringSet("omiljene", noviSet).commit();
    }

    private boolean jeOmiljena(String naziv) {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> omiljeneSet = prefs.getStringSet("omiljene", new HashSet<>());
        return omiljeneSet.contains(naziv);
    }

    private void updateDugmeOmiljene() {
        if (jeOmiljena(naziv)) {
            btnOmiljeneLokacije.setText("Ukloni iz omiljenih");
        } else {
            btnOmiljeneLokacije.setText("Dodaj u omiljene");
        }
    }

}
