package com.example.identifylanguage.task;

import android.os.AsyncTask;

import com.example.identifylanguage.common.ConstantEnum;
import com.example.identifylanguage.model.Language;
import com.example.identifylanguage.model.Languages;
import com.example.identifylanguage.network.LanguagesApi;
import com.example.identifylanguage.network.MyRetrofit;
import com.example.identifylanguage.presenter.Presenter;

import java.text.DecimalFormat;
import java.util.List;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Фоновая адача, запускаемая для идентификации введенного текста
 */
public class IdentityTask extends AsyncTask<String, Void, String> {

    private static final double threshold = 80;
    private Presenter presenter;

    public IdentityTask(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected String doInBackground(String... strings) {
        LanguagesApi languagesApi = MyRetrofit.languagesApi;
        String credentials = Credentials.basic(ConstantEnum.USERNAME.getCode(), ConstantEnum.PASSWORD.getCode());
        RequestBody text = RequestBody.create(MediaType.parse("text/plain"), presenter.getText());
        Call<Languages> languages = languagesApi.identify(credentials, "text/plain", text);
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
            presenter.setResultOfIdentification(createResultOfIdentification(languages));
            presenter.makeNote();
        } else {
            presenter.setResultOfIdentification("Не удалось распознать текст");
        }
        presenter.showResultOfIdentification();
    }

    private String createResultOfIdentification(List<Language> languages) {
        Language language = languages.get(0);
        String languageFullName = presenter.getFullName(language.getLanguage());
        String probability = new DecimalFormat("#0.00 %").format(language.getConfidence());
        if (language.getConfidence() > threshold) {
            return "Это определенно " + languageFullName + ". С вероятностью " + probability;
        }
        String secondLanguageFullName = presenter.getFullName(languages.get(1).getLanguage());
        String secondProbability = new DecimalFormat("#0.00 %").format(languages.get(1).getConfidence());
        return "Вероятность того, что это " + languageFullName + " " + probability + ". Так же возможно, что это " + secondLanguageFullName + " (" + secondProbability + ")";
    }
}
