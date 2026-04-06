package com.example.projekat;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LokacijaDao {

    @Insert
    void insert(Lokacija lokacija);

    @Query("SELECT * FROM lokacije")
    LiveData<List<Lokacija>> getAllLokacije();

    @Update
    void update(Lokacija lokacija);

    @Query("DELETE FROM lokacije")
    void deleteAll();

    @Query("DELETE FROM lokacije WHERE naziv = :naziv")
    void deleteByNaziv(String naziv);
}
