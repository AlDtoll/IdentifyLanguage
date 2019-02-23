package com.example.identifylanguage.network;

import com.example.identifylanguage.model.Languages;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LanguagesApi {

    @POST("v3/identify?version=2018-05-01")
    Call<Languages> languages(@Header("Authorization") String credentials, @Header("Content-Type") String type, @Body String text);
}
