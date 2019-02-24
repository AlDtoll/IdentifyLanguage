package com.example.identifylanguage.network;

import com.example.identifylanguage.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyRetrofit {

    private static final String BASE_URL = "https://gateway.watsonplatform.net/language-translator/api/";

    private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

    private static OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build();

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();

    public static LanguagesApi languagesApi = retrofit.create(LanguagesApi.class);

    private MyRetrofit() {
    }


}
