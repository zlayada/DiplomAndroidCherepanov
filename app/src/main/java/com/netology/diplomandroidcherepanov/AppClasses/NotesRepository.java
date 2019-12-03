package com.netology.diplomandroidcherepanov.AppClasses;

import com.netology.diplomandroidcherepanov.DataBase.Note;

import java.util.List;

public interface NotesRepository {

    Note getNoteById(int id);

    List<Note> getNotes();

    void addNote(Note note);

    void deleteNote(Note note);

    void updateNote(Note note);
}