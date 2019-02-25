package com.example.identifylanguage.task;

import android.os.AsyncTask;

import com.example.identifylanguage.common.ConstantEnum;
import com.example.identifylanguage.model.Language;
import com.example.identifylanguage.model.Languages;
import com.example.identifylanguage.network.LanguagesApi;
import com.example.identifylanguage.network.MyRetrofit;
import com.example.identifylanguage.presenter.Presenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Фоновая задача, запускаемая для получения мапы: код языка - название языка
 */
public class GetIdentifiableLanguagesTask extends AsyncTask<String, Void, String> {

    private Presenter presenter;

    public GetIdentifiableLanguagesTask(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected String doInBackground(String... strings) {
        LanguagesApi languagesApi = MyRetrofit.languagesApi;
        String credentials = Credentials.basic(ConstantEnum.USERNAME.getCode(), ConstantEnum.PASSWORD.getCode());
        Call<Languages> languages = languagesApi.getLanguages(credentials);
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
            Map<String, String> map = new HashMap<>();
            for (Language language : languages) {
                map.put(language.getLanguage(), language.getName());
            }
            presenter.setMap(map);
        }
    }
}
