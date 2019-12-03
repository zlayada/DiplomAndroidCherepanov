package com.netology.diplomandroidcherepanov.Activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.netology.diplomandroidcherepanov.App;
import com.netology.diplomandroidcherepanov.DataBase.Note;
import com.netology.diplomandroidcherepanov.AppClasses.NotesRepository;
import com.netology.diplomandroidcherepanov.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewNoteActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private EditText m_edit_text_title;
    private EditText m_edit_text_note;
    private EditText m_text_deadline_date;

    private CheckBox m_check_box_deadline;
    private LinearLayout m_field_deadline;

    private NotesRepository noteRepository;
    private int idNote;
    private Note currentNote;

    private SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        // Создание экземпляра ввода даты
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Чтение
        noteRepository = App.getInstance().getNotesRepository();

        // Инициализация элементов экрана
        initView();

        // Получение данных из предыдущей активити
        Intent intent = getIntent();
        idNote = intent.getIntExtra("id", -1);
        if (idNote >= 0) {
            outNote();
        }

    }

    // Инициализация элементов экрана
    private void initView() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        m_edit_text_title = findViewById(R.id.edit_text_title);
        m_edit_text_note = findViewById(R.id.edit_text_note);
        m_text_deadline_date = findViewById(R.id.text_deadline_date);
        m_field_deadline = findViewById(R.id.field_deadline);
        m_check_box_deadline = findViewById(R.id.check_box_deadline);

        // Обработка кнопки - возврат на предыдущее активити (ListNotes) при сохранении заметки
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(NewNoteActivity.this, NotesListActivity.class);
                startActivity(intent);

            }
        });

        // Обработка чек-бокса - при выборе сделать видимым календарь, иначе скрыть календарь
        m_check_box_deadline.setOnCheckedChangeListener (new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (compoundButton.isChecked()) {
                    Date date = Calendar.getInstance().getTime();
                    m_text_deadline_date.setText(simpleDateFormat.format(date));
                    m_field_deadline.setVisibility(View.VISIBLE);

                } else {

                    m_field_deadline.setVisibility(View.GONE);
                    m_text_deadline_date.setText("");
                }
            }
        });
    }

    // Чтение и вывод сохраненных данных во View
    private void outNote() {

        currentNote = noteRepository.getNoteById(idNote);
        m_edit_text_title.setText(currentNote.getNoteTitle());
        m_edit_text_note.setText(currentNote.getNoteDescription());

        // Вывод даты дедлана в поле, если она была создана
        Date dateDeadline = currentNote.getDateDeadline();

        if (dateDeadline != null) {
            m_check_box_deadline.setChecked(true);
            m_field_deadline.setVisibility(View.VISIBLE);
            m_text_deadline_date.setText(simpleDateFormat.format(dateDeadline));
        }
    }

    // Сохранение заметки
    public void saveNoteClick() {

        String nameStr = m_edit_text_title.getText().toString();
        String descStr = m_edit_text_note.getText().toString();
        String deadlineDateStr = m_text_deadline_date.getText().toString();
        Date date = Calendar.getInstance().getTime();

        Date dateDeadline = null;
        int isDeadline = 0;

        try {
            dateDeadline = simpleDateFormat.parse(deadlineDateStr);
            isDeadline = 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Проверка на ввод трех пустых полей и вывод ошибки
        if (nameStr.equals("") && descStr.equals("") && deadlineDateStr.equals("")) {
            Toast.makeText(this, R.string.tNote_enter_error, Toast.LENGTH_SHORT).show();

        } else {

            // Проверяем на наличие созданной заметки до ввода данных
            if (idNote >= 0) {

                // Заметка есть, обновляем данные
                currentNote.setNoteTitle(nameStr);
                currentNote.setNoteDescription(descStr);
                currentNote.setDatePub(date);
                currentNote.setDateDeadline(dateDeadline);
                currentNote.setIsDeadLine(isDeadline);
                noteRepository.updateNote(currentNote);

            } else {

                // Заметки нет, создаем заметку
                Note note = new Note(nameStr, date, dateDeadline, isDeadline, descStr);
                noteRepository.addNote(note);
            }

            finish();
        }
    }

    // Вызываем окно выбора даты в календаре
    public void dateDeadlineClick(View view) throws ParseException {
        showCalendarDialog();
    }

    // Работа с датой
    private void showCalendarDialog() throws ParseException {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.calendar_dialog, null);
        dialogBuilder.setView(dialogView);

        final DatePicker datePicker = dialogView.findViewById(R.id.date_windows);
        Calendar currentDateOfDeadline = Calendar.getInstance();

        // Добавление текущей даты, если дедлайна нет
        if (!m_text_deadline_date.getText().toString().equals("")) {
            Date currentDate = simpleDateFormat.parse(m_text_deadline_date.getText().toString());
            currentDateOfDeadline.setTime(currentDate);
        }

        datePicker.init(
                currentDateOfDeadline.get(Calendar.YEAR),
                currentDateOfDeadline.get(Calendar.MONTH),
                currentDateOfDeadline.get(Calendar.DATE),
                null);

        // Обработка кнопок календаря
        dialogBuilder.setPositiveButton(R.string. tAdd, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                m_text_deadline_date.setText(new StringBuilder()
                        .append(datePicker.getYear()).append("-")
                        .append(datePicker.getMonth() + 1).append("-")
                        .append(datePicker.getDayOfMonth())
                );
            }
        });

        dialogBuilder.setNegativeButton(R.string.tCancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();

            }
        });

        AlertDialog calendarDialog = dialogBuilder.create();
        calendarDialog.show();
    }


    // Управление меню в Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem settingsMenuItem = menu.findItem(R.id.btn_setting_pin);
        settingsMenuItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_save_note:
                saveNoteClick();
        }
        return super.onOptionsItemSelected(item);
    }

}
