package com.example.identifylanguage.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.identifylanguage.R;
import com.example.identifylanguage.database.DataBase;
import com.example.identifylanguage.model.Note;
import com.example.identifylanguage.task.IdentityTask;
import com.example.identifylanguage.view.HistoryFragment;
import com.example.identifylanguage.view.NewTextFragment;

import java.io.Serializable;
import java.util.ArrayList;

public class Presenter implements Serializable {

    public static final String PRESENTER = "presenter";

    private Activity activity;
    private NewTextFragment newTextFragment;
    private HistoryFragment historyFragment;
    private ProgressDialog progressDialog;

    private boolean isActive = true;
    private String text;
    private String resultOfIdentification = "пока не известен";

    public Presenter(Activity activity) {
        attachView(activity);
    }

    private void attachView(Activity activity) {
        this.activity = activity;
    }

    public void onStart() {
        createFragments();
        showFragment(newTextFragment);
        isActive = true;
    }


    public void createFragments() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(PRESENTER, this);
        newTextFragment = new NewTextFragment();
        newTextFragment.setArguments(bundle);
        historyFragment = new HistoryFragment();
        historyFragment.setArguments(bundle);
    }

    public void fabWasClicked() {
        startIdentity();
    }

    private void startIdentity() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Идет распознавание языка. Пожалуйста подождите...");
        progressDialog.show();

        IdentityTask identityTask = new IdentityTask(this);
        identityTask.execute(text);
    }

    public void showResultOfIdentification() {
        if (isActive) {
            progressDialog.dismiss();
            final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder
                    .setTitle("Результат идентификации")
                    .setMessage(resultOfIdentification)
                    .setPositiveButton("Ok", (dialog, which) -> clearEditText())
                    .create();
            builder.show();
        }
    }

    private void clearEditText() {
        EditText editText = activity.findViewById(R.id.editText);
        if (editText != null) {
            editText.setText(null);
        }
    }

    public void itemWasSelected(MenuItem item) {
        Fragment fragment = null;
        int id = item.getItemId();

        if (id == R.id.new_text) {
            fragment = newTextFragment;
        } else if (id == R.id.history) {
            fragment = historyFragment;
        }

        showFragment(fragment);
        item.setChecked(true);
        activity.setTitle(item.getTitle());

        DrawerLayout drawer = activity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
        fragmentManager.popBackStack();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }

    public void onTextChanged(CharSequence charSequence) {
        //todo убрать кавычки
        text = charSequence.toString();
    }


    public ArrayList<Note> getNotes() {
        return DataBase.loadNotes(activity);
    }

    public void makeNote() {
        DataBase.makeNote(activity, text, resultOfIdentification);
    }

    public String getText() {
        return text;
    }

    public void setResultOfIdentification(String resultOfIdentification) {
        this.resultOfIdentification = resultOfIdentification;
    }

    public void onStop() {
        newTextFragment = null;
        historyFragment = null;
        progressDialog = null;
        isActive = false;
    }

    public void setText(String text) {
        this.text = text;
    }
}
