package com.example.projekat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class VremenskaPrognozaActivity extends AppCompatActivity {

    private TextView tvNazivLokacije, tvKordinate, tvPrognoza, tvPrognoza7Dana;
    private Button btnNazad;
    private ImageView imgIkonica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vremenska_prognoza);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String nazivLokacije = getIntent().getStringExtra("naziv");
        Double geoDuzina = getIntent().getDoubleExtra("geoDuzina", 0);
        Double geoSirina = getIntent().getDoubleExtra("geoSirina", 0);

        tvNazivLokacije = findViewById(R.id.tvNazivLokacije);
        tvKordinate = findViewById(R.id.tvKordinate);
        tvPrognoza = findViewById(R.id.tvPrognoza);
        btnNazad = findViewById(R.id.btnNazad3);
        imgIkonica = findViewById(R.id.imgIkonica);
        tvPrognoza7Dana = findViewById(R.id.tvPrognoza7Dana);

        btnNazad.setOnClickListener(v -> {
            finish();
        });

        tvNazivLokacije.setText(nazivLokacije);
        tvKordinate.setText("Geografska duzina: " + geoDuzina + "\nGeografska sirina: " + geoSirina);

        ucitajVreme(geoSirina, geoDuzina);

    }

    private void ucitajVreme(double lat, double lng) {

        String url =
                "https://api.open-meteo.com/v1/forecast?" +
                        "latitude=" + lat +
                        "&longitude=" + lng +
                        "&current_weather=true" +
                        "&daily=temperature_2m_max,temperature_2m_min,weathercode" +
                        "&timezone=auto";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.GET, url, null,
                        response -> {

                            try {
                                JSONObject cw = response.getJSONObject("current_weather");

                                double temperatura = cw.getDouble("temperature");
                                double vetar = cw.getDouble("windspeed");
                                int code = cw.getInt("weathercode");

                                String tekst =
                                        "Temperatura: " + temperatura + "°C\n" +
                                                "Brzina vetra: " + vetar + " km/h";

                                tvPrognoza.setText(tekst);
                                imgIkonica.setImageResource(ikonicaZaKod(code));

                                JSONObject daily = response.getJSONObject("daily");

                                var max = daily.getJSONArray("temperature_2m_max");
                                var min = daily.getJSONArray("temperature_2m_min");

                                StringBuilder sb = new StringBuilder();


                                for (int i = 0; i < max.length(); i++) {

                                    sb.append("Dan " + (i+1) + ": ")
                                            .append(min.getDouble(i))
                                            .append("°C - ")
                                            .append(max.getDouble(i))
                                            .append("°C\n");
                                }

                                tvPrognoza7Dana.setText(sb.toString());


                            } catch (Exception e) {
                                tvPrognoza.setText("Greška pri obradi podataka");
                            }

                        },
                        error -> {
                            error.printStackTrace();
                            Toast.makeText(this,
                                    "Greška pri učitavanju prognoze",
                                    Toast.LENGTH_SHORT).show();
                        });

        queue.add(request);
    }

    private int ikonicaZaKod(int code) {

        switch (code) {
            case 0:
                return R.drawable.sun;

            case 1:
            case 2:
            case 3:
                return R.drawable.cloud;

            case 61:
            case 63:
            case 65:
                return R.drawable.rain;

            case 95:
                return R.drawable.storm;

            default:
                return R.drawable.cloud;
        }
    }

}