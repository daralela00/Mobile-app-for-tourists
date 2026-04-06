package com.example.projekat;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Lokacija.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LokacijaDao lokacijaDao();
}
