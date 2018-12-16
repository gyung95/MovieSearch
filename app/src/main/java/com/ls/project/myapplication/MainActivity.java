package com.ls.project.myapplication;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ls.project.myapplication.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private MovieList movieList;
    private ActivityMainBinding binding;

    // 네이버 Api를 사용하기 위한 id와 Secret
    private String clientId = "z1peFt06UkopOCM7u3p0";
    private String clientSecret = "8KaFmoxMvZ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main); // activity_main 와 바인딩
        movieList = new MovieList();
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        setTitle("부스트캠프 사전과제");

        //recyclerView 클릭 리스너
        binding.recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), binding.recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(movieList.getMovieList().get(position).getLink().replace("\"",""))));
                    }

                }));

        //영화 제목 검색 버튼 리스터
        binding.buttonSearch.setOnClickListener((View v)->{
            if (binding.editViewSearch.getText().toString().isEmpty()){
                return;
            }
            Retrofit retrofit =new Retrofit.Builder()
                    .baseUrl("https://openapi.naver.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Api api= retrofit.create(Api.class);
            Call<JsonObject> call = api.getMovieList(clientId,clientSecret,binding.editViewSearch.getText().toString()); // 레트로핏을 사용해 Api의 id, Secret, 검색어 전달
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                    movieList.getMovieList().clear();

                    if(response.isSuccessful()) { // 결과가 true 일때
                        JsonParser parser = new JsonParser();
                        JsonObject object = (JsonObject) parser.parse(response.body().toString());
                        JsonArray array = (JsonArray) object.get("items");
                        if (array.toString().length()>2) {

                            // json으로 받아온 내용을 movieList에 하나씩 추가
                            for (int i = 0; i < array.size(); i++) {
                                JsonObject obj = (JsonObject) array.get(i);

                                MovieItem movieItem = new MovieItem(obj.get("title").toString(), obj.get("link").toString(),
                                        obj.get("image").toString(), obj.get("subtitle").toString(), obj.get("pubDate").toString(),
                                        obj.get("director").toString(),
                                        obj.get("actor").toString(),
                                        obj.get("userRating").toString());
                                movieList.addListItem(movieItem);
                                RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(movieList, getApplicationContext());
                                binding.recyclerView.setAdapter(mAdapter);

                            }
                        } else {
                            // 검색 내용이 없을 경우
                            Toast.makeText(MainActivity.this, "찾은 내용이 없습니다.", Toast.LENGTH_SHORT).show();
                            RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(movieList, getApplicationContext());
                            binding.recyclerView.setAdapter(mAdapter);
                        }
                    }

                }

                @Override
                public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });
    }

}
