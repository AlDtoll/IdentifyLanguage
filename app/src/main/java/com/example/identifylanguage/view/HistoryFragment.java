package com.example.identifylanguage.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.identifylanguage.R;
import com.example.identifylanguage.adapter.NoteAdapter;
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
        RecyclerView recyclerView = fragment.findViewById(R.id.history_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        NoteAdapter adapter = new NoteAdapter(notes);
        recyclerView.setAdapter(adapter);


        return fragment;
    }

}
