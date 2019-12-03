package com.netology.diplomandroidcherepanov.DataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class DatabaseHelper extends RoomDatabase {

    public abstract NotesDao NotesDao();

}
