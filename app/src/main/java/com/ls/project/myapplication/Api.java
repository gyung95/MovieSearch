package com.ls.project.myapplication;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Api {
    // GET 방식으로 전송
    @GET("v1/search/movie.json")
    Call<JsonObject> getMovieList (@Header("X-Naver-Client-Id") String clientId , @Header("X-Naver-Client-Secret") String clientSecret , @Query("query") String query);

}
