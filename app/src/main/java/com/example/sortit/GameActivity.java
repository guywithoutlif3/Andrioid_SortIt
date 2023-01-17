package com.example.sortit;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

import okhttp3.*;

public class GameActivity extends AppCompatActivity {
    String[] stockPeople = {};
    String[] criminals = {};
    String[] cats = {};
    String[] dogs = {};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext()); //what does this even doo i dont know but it fixes something lol
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);
        try {
            getCriminals();
            getStockPeople();
            getDogs();
            getCats();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void getCriminals() throws IOException, JSONException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        Request request = new Request.Builder()
                .url("https://jailbase-jailbase.p.rapidapi.com/recent/?source_id=ar-jcso")
                .method("GET", null)
                .addHeader("X-RapidAPI-Key", "e451035421msh38c089d44761c42p1b4070jsn3ebb2f8a409c")
                .addHeader("X-RapidAPI-Host", "jailbase-jailbase.p.rapidapi.com")
                .build();
        Response response = client.newCall(request).execute();


        JSONObject Jobject = new JSONObject(response.body().string());
        JSONArray Jarray = Jobject.getJSONArray("records");

        for (int i = 0; i < Jarray.length(); i++) {
            JSONObject object = Jarray.getJSONObject(i);
            criminals = Arrays.copyOf(criminals, criminals.length + 1);
            criminals[criminals.length - 1] = object.getString("mugshot");
        }
    }

    public void getStockPeople() throws IOException, JSONException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        Request request = new Request.Builder()
                .url("https://pexelsdimasv1.p.rapidapi.com/v1/search?query=mugshot&locale=en-US&per_page=10&page=1")
                .method("GET", null)
                .addHeader("Authorization", "563492ad6f91700001000001ddd8be82192b429ab31f5adc64661679")
                .addHeader("X-RapidAPI-Key", "e451035421msh38c089d44761c42p1b4070jsn3ebb2f8a409c")
                .addHeader("X-RapidAPI-Host", "PexelsdimasV1.p.rapidapi.com")
                .build();
        Response response = client.newCall(request).execute();

        JSONObject Jobject = new JSONObject(response.body().string());
        JSONArray Jarray = Jobject.getJSONArray("photos");

        for (int i = 0; i < Jarray.length(); i++) {
            JSONObject object = Jarray.getJSONObject(i);
            stockPeople = Arrays.copyOf(stockPeople, stockPeople.length + 1);
            stockPeople[stockPeople.length - 1] = object.getString("url");
        }
    }

    public void getCats()
            throws IOException, JSONException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/images/search?limit=10")
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();


        JSONArray Jarray = new JSONArray(response.body().string());

        for (int i = 0; i < Jarray.length(); i++) {
            JSONObject object = Jarray.getJSONObject(i);
            cats = Arrays.copyOf(cats, cats.length + 1);
            cats[cats.length - 1] = object.getString("url");
        }


    }

    public void getDogs() throws IOException, JSONException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        Request request = new Request.Builder()
                .url("https://dog.ceo/api/breeds/image/random/25")
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();

        JSONObject Jobject = new JSONObject(response.body().string());

        JSONArray Jarray = Jobject.getJSONArray("message");

        for (int i = 0; i < Jarray.length(); i++) {
            System.out.println();
            dogs = Arrays.copyOf(dogs, dogs.length + 1);
            dogs[dogs.length - 1] = Jarray.get(i).toString();
        }
    }

}
