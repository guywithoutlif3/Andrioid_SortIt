package com.example.sortit;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.*;

public class GameActivity extends AppCompatActivity implements SensorEventListener {
    String[] stockPeople = {};
    String[] criminals = {};
    String[] cats = {};
    String[] dogs = {};
    private SensorManager sensorManager;
    private Sensor accelerator;
    int hardCodedID = 010400;
    DisplayMetrics displayMetrics = new DisplayMetrics();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext()); //what does this even doo i dont know but it fixes something lol
        super.onCreate(savedInstanceState);
        accelerator = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        setContentView(R.layout.activity_game);
    }

    public int getRandom(int min, int max) {
        return (int) (min + Math.floor(Math.random() * (max - min + 1)));
    }

    public void spawn() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.gamelayout);
        ImageView ivBasicImage = (ImageView) findViewById(R.id.dumbFuckingPicture);

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int rand = getRandom(1, 2);
        if (rand == 1) {
            int randArrVal = getRandom(0, criminals.length - 1);
            ImageView image = new ImageView(this);
            //ivBasicImage.setLayoutParams(new android.view.ViewGroup.LayoutParams(200,200));
            //ivBasicImage.setX(height/2);
            //ivBasicImage.setY(width/2);
            ivBasicImage.setBackgroundColor(getResources().getColor(R.color.black));
            //image.setId(hardCodedID); // i hardcode the id so i can always find it and because i always plan to only load one image at a time it should befine
            Picasso.get().load(criminals[randArrVal]).into(ivBasicImage);
            // Adds the view to the layout
            //layout.addView(image);
            layout.refreshDrawableState();

        }/*else if(rand== 2){
            int randArrVal = getRandom(0,dogs.length);
            ImageView image = new ImageView(this);
            image.setLayoutParams(new android.view.ViewGroup.LayoutParams(500,500));
            image.setX(height/2);
            image.setY(width/2);
            image.setId(hardCodedID); // i hardcode the id so i can always find it and because i always plan to only load one image at a time it should befine
            Picasso.get().load(dogs[randArrVal]).into(image);            // Adds the view to the layout
            layout.addView(image);
        }*/ else if (rand == 2) {
            int randArrVal = getRandom(0, stockPeople.length - 1);
            ImageView image = new ImageView(this);
            // ivBasicImage.setLayoutParams(new android.view.ViewGroup.LayoutParams(200,200));
            //ivBasicImage.setX(height/2);
            //ivBasicImage.setY(width/2);
            //image.setId(hardCodedID); // i hardcode the id so i can always find it and because i always plan to only load one image at a time it should befine
            ivBasicImage.setBackgroundColor(getResources().getColor(R.color.black));
            Picasso.get().load(stockPeople[randArrVal]).into(ivBasicImage);            // Adds the view to the layout
            //layout.addView(ivBasicImage);
            layout.refreshDrawableState();
        }/*else if(rand == 4){
            int randArrVal = getRandom(0,cats.length);

            ImageView image = new ImageView(this);
            image.setLayoutParams(new android.view.ViewGroup.LayoutParams(500,500));
            image.setX(height/2);
            image.setY(width/2);
            image.setId(hardCodedID); // i hardcode the id so i can always find it and because i always plan to only load one image at a time it should befine
            Picasso.get().load(cats[randArrVal]).into(image);
            // Adds the view to the layout
            layout.addView(image);
        }*/


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
            System.out.println(object.getString("mugshot"));
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
            System.out.println(object.getString("url"));
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
            System.out.println(object.getString("url"));
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
            System.out.println(Jarray.get(i).toString());
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[2];



        ImageView ivBasicImage = (ImageView) findViewById(R.id.dumbFuckingPicture);

        ivBasicImage.setX(ivBasicImage.getX()+x*10);
        ivBasicImage.setY(ivBasicImage.getY()+y*10);

        // System.out.println("X:"+String.valueOf(x)+" Y: " +String.valueOf(y)+" Z: "+ String.valueOf(z));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this, accelerator);
        super.onPause();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.

        super.onResume();
        try {
            getCriminals();
            getStockPeople();
            //getDogs();
            //getCats();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        spawn();

        sensorManager.registerListener(this, accelerator, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
