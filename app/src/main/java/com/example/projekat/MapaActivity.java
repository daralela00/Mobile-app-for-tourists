package com.example.projekat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

public class MapaActivity extends AppCompatActivity {

    private MapView map;
    private IMapController mapController;
    private FusedLocationProviderClient fusedLocationClient;
    private GeoPoint mojaLokacija;
    private Button btnNazad, btnRuta;
    private GeoPoint ciljnaLokacija;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mapa);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Double geoDuzina = getIntent().getDoubleExtra("geoDuzina", 0);
        Double geoSirina = getIntent().getDoubleExtra("geoSirina", 0);

        ciljnaLokacija = new GeoPoint(geoSirina, geoDuzina);

        map = findViewById(R.id.mapView);
        btnNazad = findViewById(R.id.btnNazad6);
        btnRuta = findViewById(R.id.btnRuta);

        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        mapController = map.getController();
        mapController.setZoom(15.0);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        } else {
            ucitajLokaciju();
        }

        btnRuta.setOnClickListener(v -> {
            if (mojaLokacija != null) {
                ucitajRutu(mojaLokacija, ciljnaLokacija);
            } else {
                Toast.makeText(this, "Lokacija nije dostupna", Toast.LENGTH_SHORT).show();
            }
        });

        btnNazad.setOnClickListener(v -> {
            finish();
        });

    }

    private void ucitajLokaciju() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {

                        mojaLokacija = new GeoPoint(
                                location.getLatitude(),
                                location.getLongitude()
                        );

                        mapController.setCenter(ciljnaLokacija);

                        Marker marker = new Marker(map);
                        marker.setPosition(mojaLokacija);
                        marker.setTitle("Moja lokacija");
                        map.getOverlays().add(marker);

                        String nazivLokacije = getIntent().getStringExtra("naziv");
                        Marker ciljMarker = new Marker(map);
                        ciljMarker.setPosition(ciljnaLokacija);
                        ciljMarker.setTitle(nazivLokacije);
                        map.getOverlays().add(ciljMarker);

                        map.invalidate();
                    }
                });
    }

    private void ucitajRutu(GeoPoint start, GeoPoint end) {

        String url = "https://router.project-osrm.org/route/v1/driving/"
                + start.getLongitude() + "," + start.getLatitude() + ";"
                + end.getLongitude() + "," + end.getLatitude()
                + "?overview=full&geometries=geojson";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null,
                response -> {
                    try {
                        JSONArray coords = response.getJSONArray("routes")
                                .getJSONObject(0)
                                .getJSONObject("geometry")
                                .getJSONArray("coordinates");

                        List<GeoPoint> points = new ArrayList<>();

                        for (int i = 0; i < coords.length(); i++) {
                            JSONArray coord = coords.getJSONArray(i);
                            double lon = coord.getDouble(0);
                            double lat = coord.getDouble(1);
                            points.add(new GeoPoint(lat, lon));
                        }

                        Polyline lineRoute = new Polyline();
                        lineRoute.setPoints(points);
                        map.getOverlays().add(lineRoute);
                        map.invalidate();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },

                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Greška pri učitavanju rute", Toast.LENGTH_SHORT).show();
                }
        );

        queue.add(request);
    }
}

