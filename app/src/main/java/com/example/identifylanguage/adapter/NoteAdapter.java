package com.example.identifylanguage.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.identifylanguage.R;
import com.example.identifylanguage.model.Note;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    private ArrayList<Note> notes;

    public NoteAdapter(ArrayList<Note> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.note_view, viewGroup, false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder noteHolder, int i) {
        Note note = notes.get(i);
        noteHolder.text.setText(note.getText());
        noteHolder.resultOfIdentification.setText(note.getResultOfIdentification());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class NoteHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView text;
        public TextView resultOfIdentification;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            resultOfIdentification = itemView.findViewById(R.id.result_of_identification);
        }
    }
}
