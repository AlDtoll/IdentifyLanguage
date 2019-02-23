package com.example.identifylanguage.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.identifylanguage.presenter.Presenter;
import com.example.identifylanguage.R;

import static com.example.identifylanguage.presenter.Presenter.PRESENTER;

public class NewTextFragment extends Fragment {

    Presenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        presenter = (Presenter) getArguments().getSerializable(PRESENTER);
        View fragment = inflater.inflate(R.layout.fragment_newtext, container, false);
        EditText editText = fragment.findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.onTextChanged(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return fragment;
    }
}
