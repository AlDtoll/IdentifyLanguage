package com.example.identifylanguage.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.identifylanguage.R;
import com.example.identifylanguage.common.ConstantEnum;
import com.example.identifylanguage.model.Note;
import com.example.identifylanguage.presenter.Presenter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Фрагмент для отображения экрана истории, на котором показываются предыдущие тексты и результат их индетификации
 */
public class HistoryFragment extends Fragment implements Serializable {

    Presenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        presenter = (Presenter) getArguments().getSerializable(ConstantEnum.PRESENTER.getCode());
        View fragment = inflater.inflate(R.layout.fragment_history, container, false);
        ArrayList<Note> notes = presenter.getNotes();
        LinearLayout list = fragment.findViewById(R.id.history_recycler_view);
        LayoutInflater ltInflater = getLayoutInflater();
        for (int i = 0; i < notes.size(); i++) {
            View vi = ltInflater.inflate(R.layout.note_view, null);
            ((TextView) vi.findViewById(R.id.text)).setText(notes.get(i).getText());
            ((TextView) vi.findViewById(R.id.result_of_identification)).setText(notes.get(i).getResultOfIdentification());
            list.addView(vi);
        }

        //todo разобраться почему остается пустым
//        RecyclerView recyclerView = fragment.findViewById(R.id.history_recycler_view);
//        NoteAdapter adapter = new NoteAdapter(notes);
//        recyclerView.setAdapter(adapter);
//        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(llm);

        return fragment;
    }

}
