package com.example.projekat;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "lokacije")
public class Lokacija {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String naziv;
    private double geoDuzina;
    private double geoSirina;
    private boolean omiljena;

    public Lokacija(String naziv, double geoSirina, double geoDuzina) {
        this.naziv = naziv;
        this.geoDuzina = geoDuzina;
        this.geoSirina = geoSirina;
        this.omiljena = false;
    }

    public String getNaziv() {
        return naziv;
    }

    public boolean isOmiljena() {
        return omiljena;
    }

    public void setOmiljena(boolean omiljena) {
        this.omiljena = omiljena;
    }

    public double getGeoDuzina() {
        return geoDuzina;
    }

    public double getGeoSirina() {
        return geoSirina;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public void setGeoDuzina(double geoDuzina) {
        this.geoDuzina = geoDuzina;
    }

    public void setGeoSirina(double geoSirina) {
        this.geoSirina = geoSirina;
    }
}
