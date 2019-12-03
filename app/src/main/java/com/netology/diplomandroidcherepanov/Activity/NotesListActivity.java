package com.netology.diplomandroidcherepanov.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.netology.diplomandroidcherepanov.App;
import com.netology.diplomandroidcherepanov.DataBase.Note;
import com.netology.diplomandroidcherepanov.AppClasses.NotesAdapter;
import com.netology.diplomandroidcherepanov.AppClasses.NotesRepository;
import com.netology.diplomandroidcherepanov.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesListActivity extends AppCompatActivity {

    private RecyclerView recycler_View_list_notes;
    private Toolbar toolbar;
    private Button btn_new_note;

    private NotesRepository noteRepository;
    private NotesAdapter recyclerAdapter;

    private List<Note> noteList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        initView();
    }

    // Вывод сущетвующих данных в лаяут через адаптер
    @Override
    protected void onResume() {
        super.onResume();
        noteRepository = App.getInstance().getNotesRepository();
        noteList = noteRepository.getNotes();
        recyclerAdapter = new NotesAdapter(this, noteRepository.getNotes());
        recycler_View_list_notes.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recycler_View_list_notes.setAdapter(recyclerAdapter);
    }

    // Инициализация элементов экрана
    private void initView() {

        recycler_View_list_notes = findViewById(R.id.recycler_view_list_notes);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn_new_note = findViewById(R.id.new_note_btn);
        btn_new_note.setOnClickListener(new View.OnClickListener() {

            // При клике на кнопку создания новой заметки - открытие активити новой заметки
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotesListActivity.this, NewNoteActivity.class));
            }
        });
    }


    // Управление кнопками меню в тулбаре
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem saveMenuItem = menu.findItem(R.id.btn_save_note);
        saveMenuItem.setVisible(false);
        return true;
    }

    // Обработка нажатия кнопки "настроки" в тулбаре и вызов акивити настроек
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent = null;
        switch (item.getItemId()) {

            case R.id.btn_setting_pin:
                Toast.makeText(this, "Маркер", Toast.LENGTH_SHORT).show(); // TODO Убрать после отладки
                intent = new Intent(NotesListActivity.this, SettingsActivityPin.class);
                break;
        }
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

}
