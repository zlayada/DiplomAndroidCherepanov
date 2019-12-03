package com.netology.diplomandroidcherepanov;

import android.app.Application;

import androidx.room.Room;

import com.netology.diplomandroidcherepanov.DataBase.DatabaseHelper;
import com.netology.diplomandroidcherepanov.AppClasses.NotesRepository;
import com.netology.diplomandroidcherepanov.DataBase.dbNotesRepository;

public class App extends Application {

    private static App instance;
    private static NotesRepository notesRepository;
    private DatabaseHelper db;
    public static final String NAME_BASE_NOTE = "data_base_note_app";


    public static App getInstance() {

        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        db = Room.databaseBuilder(getApplicationContext(), DatabaseHelper.class, NAME_BASE_NOTE)
                .allowMainThreadQueries()
                .build();

        notesRepository = new dbNotesRepository(this);

    }

    public static NotesRepository getNotesRepository() {

        return notesRepository;
    }


    public DatabaseHelper getDatabaseInstance() {

        return db;
    }
}
