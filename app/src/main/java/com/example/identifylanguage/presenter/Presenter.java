package com.example.identifylanguage.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.identifylanguage.BuildConfig;
import com.example.identifylanguage.IdentityTask;
import com.example.identifylanguage.R;
import com.example.identifylanguage.database.DataBase;
import com.example.identifylanguage.model.Note;
import com.example.identifylanguage.network.LanguagesApi;
import com.example.identifylanguage.view.HistoryFragment;
import com.example.identifylanguage.view.MainActivity;
import com.example.identifylanguage.view.NewTextFragment;

import java.io.Serializable;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Presenter implements Serializable {

    private static final String BASE_URL = "https://gateway.watsonplatform.net/language-translator/api/";
    public static final String PRESENTER = "presenter";

    private Activity activity;
    private Fragment newTextFragment;
    private Fragment historyFragment;
    private ProgressDialog progressDialog;
    private LanguagesApi languagesApi;

    private String text;
    private String resultOfIdentification = "пока не известен";

    public Presenter(Activity activity) {
        attachView(activity);
        createFragments();
        createApi();
        showFragment(newTextFragment);
    }

    private void attachView(Activity activity) {
        this.activity = activity;
    }

    private void createFragments() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(PRESENTER, this);
        newTextFragment = new NewTextFragment();
        newTextFragment.setArguments(bundle);
        historyFragment = new HistoryFragment();
        historyFragment.setArguments(bundle);
    }

    private void createApi() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        languagesApi = retrofit.create(LanguagesApi.class);
    }

    public void fabWasClicked(View view) {
        startIdentity();
    }

    public void showResultOfIdentification() {
        progressDialog.dismiss();
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder
                .setTitle("Результат идентификации")
                .setMessage(resultOfIdentification)
                .setPositiveButton("Ok", (dialog, which) -> ((EditText) activity.findViewById(R.id.editText)).setText(""))
                .create();
        builder.show();
    }

    private void startIdentity() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Идет распознавание языка. Пожалуйста подождите...");
        progressDialog.show();

        //todo сделать сервис
        IdentityTask identityTask = new IdentityTask(this);
        identityTask.execute(text);
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
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }

    public Fragment getNewTextFragment() {
        return newTextFragment;
    }

    public void onTextChanged(CharSequence charSequence) {
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

    public LanguagesApi getApi() {
        return languagesApi;
    }
}
