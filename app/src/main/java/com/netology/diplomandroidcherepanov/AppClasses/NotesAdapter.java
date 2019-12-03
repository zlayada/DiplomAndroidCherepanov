package com.netology.diplomandroidcherepanov.AppClasses;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.netology.diplomandroidcherepanov.Activity.NewNoteActivity;
import com.netology.diplomandroidcherepanov.App;
import com.netology.diplomandroidcherepanov.DataBase.Note;
import com.netology.diplomandroidcherepanov.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    private List<Note> notesModels;
    private Context context;

    // Конструктор
    public NotesAdapter(Context context, List<Note> notesModels) {
        this.context = context;
        this.notesModels = notesModels;
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card, parent, false);
        return new ViewHolder(view);
    }

    //Заполнение элементов списка данными
    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder holder, int position) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");

        String noteTitle = notesModels.get(position).getNoteTitle();
        String noteDescription = notesModels.get(position).getNoteDescription();
        Date dateDeadline = notesModels.get(position).getDateDeadline();

        if (!noteTitle.equals("")) {
            holder.tvTitle.setText(noteTitle);
            holder.tvTitle.setVisibility(View.VISIBLE);
        }
        if (!noteDescription.equals("")) {
            holder.tvDescription.setText(noteDescription);
            holder.tvDescription.setVisibility(View.VISIBLE);
        }
        if (dateDeadline != null) {
            holder.tvDate.setText(formatter.format(dateDeadline));
            holder.tvDate.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return notesModels.size();
    }

    public Note getById(int position) {
        return notesModels.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvDescription;
        TextView tvDate;


        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.text_view_title);
            tvDescription = itemView.findViewById(R.id.text_view_note);
            tvDate = itemView.findViewById(R.id.text_view_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editNote(getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    removeNote(getAdapterPosition());
                    return true;
                }
            });
        }
    }

    //Переход на экран редактирования сужествующей заметки
    private void editNote(int position) {
        Note note = notesModels.get(position);
        Intent intent = new Intent(context, NewNoteActivity.class);
        intent.putExtra("id", note.getId());
        context.startActivity(intent);
    }

    //Удаление заметки при долгом нажатии
    private void removeNote(final int position) {
        final NotesRepository noteRepository = App.getInstance().getNotesRepository();
        final Note note = notesModels.get(position);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setMessage(R.string.tQuestion_delete_note);
        dialogBuilder.setTitle(R.string.tQuestion_note_title_delete);


        // Удаление данных из базы
        dialogBuilder.setPositiveButton(R.string.tDelete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                noteRepository.deleteNote(note);
                notesModels.remove(position);
                notifyDataSetChanged();
            }
        });

        dialogBuilder.setNegativeButton(R.string.tCancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }
}
