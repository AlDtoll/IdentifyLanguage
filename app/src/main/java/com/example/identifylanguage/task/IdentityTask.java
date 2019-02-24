package com.example.identifylanguage.task;

import android.os.AsyncTask;

import com.example.identifylanguage.model.Language;
import com.example.identifylanguage.model.Languages;
import com.example.identifylanguage.network.LanguagesApi;
import com.example.identifylanguage.network.MyRetrofit;
import com.example.identifylanguage.presenter.Presenter;

import java.text.DecimalFormat;
import java.util.List;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IdentityTask extends AsyncTask<String, Void, String> {

    private static final String USERNAME = "6987a48d-342e-4a69-8adc-e65b1cc0b9da";
    private static final String PASSWORD = "MxYSIi6nQP2Y";

    private Presenter presenter;

    public IdentityTask(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected String doInBackground(String... strings) {
        LanguagesApi languagesApi = MyRetrofit.languagesApi;
        String credentials = Credentials.basic(USERNAME, PASSWORD);

        Call<Languages> languages = languagesApi.languages(credentials, "text/plain", presenter.getText());
        languages.enqueue(new Callback<Languages>() {
            @Override
            public void onResponse(Call<Languages> call, Response<Languages> response) {
                analyzeResponse(response);
            }

            @Override
            public void onFailure(Call<Languages> call, Throwable t) {
                presenter.setResultOfIdentification("Произошла ошибка");
                presenter.showResultOfIdentification();
            }
        });
        return "success";
    }

    private void analyzeResponse(Response<Languages> response) {
        if (response.isSuccessful()) {
            List<Language> languages = response.body().getLanguages();
            Language language = languages.get(0);
            String probability = new DecimalFormat("#0.00 %").format(language.getConfidence());
            //todo ответ по прикольнее
            String resultOfIdentification = "Это " + language.getLanguage() + ". С вероятностью " + probability;
            presenter.setResultOfIdentification(resultOfIdentification);
            presenter.makeNote();
        } else {
            presenter.setResultOfIdentification("Не удалось распознать текст");
        }
        presenter.showResultOfIdentification();
    }
}
