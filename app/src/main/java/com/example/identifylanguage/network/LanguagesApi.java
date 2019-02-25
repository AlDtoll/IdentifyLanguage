package com.example.identifylanguage.network;

import com.example.identifylanguage.model.Languages;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Апи для взаимодействия с сервером
 */
public interface LanguagesApi {

    /**
     * @param credentials код авторизации
     * @param type тип
     * @param text текст, принадлежность которого нужно определить
     * @return объект, содержащий в себе отсортированный список языков: код - вероятность совпадения
     * {
     *   "languages": [
     *     {
     *       "confidence": 0.9143,
     *       "language": "en"
     *     },
     *     {
     *       "confidence": 0.0396,
     *       "language": "hu"
     *     },
     *     {
     *       "confidence": 0.0093,
     *       "language": "ro"
     *     },
     *     {
     *       "confidence": 0.005,
     *       "language": "nl"
     *     }
     *   ]
     * }
     * }
     */
    @POST("v3/identify?version=2018-05-01")
    Call<Languages> identify(@Header("Authorization") String credentials, @Header("Content-Type") String type, @Body RequestBody text);

    /**
     * @param credentials код авторизации
     * @return объект, содержащий в себе список языков: код - название
     * {
     *   "languages": [
     *     {
     *       "language": "af",
     *       "name": "Afrikaans"
     *     },
     *     {
     *       "language": "ar",
     *       "name": "Arabic"
     *     },
     *     {
     *       "language": "az",
     *       "name": "Azerbaijani"
     *     },
     *     {
     *       "language": "ba",
     *       "name": "Bashkir"
     *     }
     *   ]
     * }
     */
    @GET("v3/identifiable_languages?version=2018-05-01")
    Call<Languages> getLanguages(@Header("Authorization") String credentials);
}
