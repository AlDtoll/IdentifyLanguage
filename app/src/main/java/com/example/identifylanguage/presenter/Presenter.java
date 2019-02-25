package com.example.identifylanguage.presenter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.identifylanguage.R;
import com.example.identifylanguage.common.ConstantEnum;
import com.example.identifylanguage.database.DataBase;
import com.example.identifylanguage.model.Note;
import com.example.identifylanguage.task.GetIdentifiableLanguagesTask;
import com.example.identifylanguage.task.IdentityTask;
import com.example.identifylanguage.view.HistoryFragment;
import com.example.identifylanguage.view.MainActivity;
import com.example.identifylanguage.view.NewTextFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Босс, который рулит всем процессом
 */
public class Presenter implements Serializable {

    private MainActivity activity;
    private NewTextFragment newTextFragment;
    private HistoryFragment historyFragment;
    private ProgressDialog progressDialog;

    private boolean isActive = true;
    private String text;
    private String resultOfIdentification = "пока не известен";
    private Map<String, String> map = new HashMap<>();

    public Presenter(MainActivity activity) {
        attachView(activity);
        new GetIdentifiableLanguagesTask(this).execute();
    }

    private void attachView(MainActivity activity) {
        this.activity = activity;
    }

    public void onStart() {
        createFragments();
        //todo должен быть тот, который был при выходе
        showFragment(newTextFragment);
        isActive = true;
    }

    private void createFragments() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstantEnum.PRESENTER.getCode(), this);
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
        //todo починить заголовок
        activity.setTitle(item.getTitle());

        DrawerLayout drawer = activity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.popBackStack();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }

    public void onTextChanged(CharSequence charSequence) {
        text = charSequence.toString();
    }

    /**
     * @return записи о результатах определения
     */
    public ArrayList<Note> getNotes() {
        return DataBase.loadNotes(activity);
    }

    /**
     * Если результат распознования был успешным, но делаем запись
     */
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


    /**
     * @param code код языка
     * @return полное название языка, либо его код, если не удалось определить полное имя
     */
    public String getFullName(String code) {
        //todo перевод
        if (map.containsKey(code)) {
            return map.get(code);
        }
        return code;
    }

    /**
     * @param map мапа, полученная из списка код языка - полное название
     */
    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
