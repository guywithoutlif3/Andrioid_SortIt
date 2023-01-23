package com.example.sortit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.*;

public class GameActivity extends AppCompatActivity implements SensorEventListener {
    //intitalization of everything
    /*
     * here i intizalize everything for later global use like all my arrays with the images
     * or ther sensor manager used for the gyro and the actual sensor for the gyor i also
     * intit here
     * also score and highscore get inited here*/
    int counter = 30;

    String[] stockPeople = {};
    String[] criminals = {};
    String[] cats = {};
    String[] dogs = {};
    private SensorManager sensorManager;
    private Sensor gyro;
    DisplayMetrics displayMetrics = new DisplayMetrics();
    private Executor executor = Executors.newFixedThreadPool(4);
    CountDownLatch latch = new CountDownLatch(4);
    int score;
    int highscore;
    CountDownTimer yourCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get score and highscore from mainActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            score = extras.getInt("score");
            highscore = extras.getInt("highscore");

        }

        //register a sensorService on my sensorManafer
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext()); //what does this even doo i dont know but it fixes something lol
        super.onCreate(savedInstanceState);
        // register my sensor with the actual GYROSCOPE Sennsr -> get sensor
        gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        //set Context View for the activity
        setContentView(R.layout.activity_game);

        //start the countdown
        gameOverByTimeout();
    }

    //a function to get a random int between MIN and MAX given into the function
    public int getRandom(int min, int max) {
        return (int) (min + Math.floor(Math.random() * (max - min + 1)));
    }

    //responsible for spawning a new image in the middle of the screen upon game launch and
    //when a new image is supposed to be spawned when scorred
    public void spawn() throws InterruptedException {
        //get the gameLayout from View
        LinearLayout layout = (LinearLayout) findViewById(R.id.gamelayout);
        //get the image where the image get inserted / spawned into from View
        ImageView ivBasicImage = (ImageView) findViewById(R.id.dumbFuckingPicture);
        //get ramdom 1 or 2 for randomzing spawn of either criminal or stock person
        //TODO: implement DOG and CAT Again
        int rand = getRandom(1, 4);
        if (rand == 1) {
            // Case for Criminals
            int randArrVal = getRandom(0, criminals.length - 1); // get a random img from assigned array
            ivBasicImage.setBackgroundColor(getResources().getColor(R.color.black));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                //I set this to check later if the user sorted the image correctly
                ivBasicImage.setAccessibilityPaneTitle("criminal");
            }
            //get the ImageMatrix / data of the url and right after load it into the image on screen
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Picasso.get().load(criminals[randArrVal]).into(ivBasicImage);
            ivBasicImage.refreshDrawableState();
            layout.refreshDrawableState();

        }else if(rand== 2){
            // Case for dog
            int randArrVal = getRandom(0, dogs.length - 1); // get a random img from assigned array
            ivBasicImage.setBackgroundColor(getResources().getColor(R.color.black));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                //I set this to check later if the user sorted the image correctly
                ivBasicImage.setAccessibilityPaneTitle("dog");
            }
            //get the ImageMatrix / data of the url and right after load it into the image on screen
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Picasso.get().load(dogs[randArrVal]).into(ivBasicImage);
            ivBasicImage.refreshDrawableState();
            layout.refreshDrawableState();
        } else if (rand == 3) {
            // Case for Stock images
            int randArrVal = getRandom(0, stockPeople.length - 1); // get a random img from assigned array
            ivBasicImage.setBackgroundColor(getResources().getColor(R.color.black));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                //I set this to check later if the user sorted the image correctly
                ivBasicImage.setAccessibilityPaneTitle("stock");
            }
            //get the ImageMatrix / data of the url and right after load it into the image on screen
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Picasso.get().load(stockPeople[randArrVal]).into(ivBasicImage);            // Adds the view to the layout
            ivBasicImage.refreshDrawableState();
            layout.refreshDrawableState();
        }else if(rand == 4){
            // Case for Stock images
            int randArrVal = getRandom(0, cats.length - 1); // get a random img from assigned array
            ivBasicImage.setBackgroundColor(getResources().getColor(R.color.black));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                //I set this to check later if the user sorted the image correctly
                ivBasicImage.setAccessibilityPaneTitle("cat");
            }
            //get the ImageMatrix / data of the url and right after load it into the image on screen
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Picasso.get().load(cats[randArrVal]).into(ivBasicImage);            // Adds the view to the layout
            ivBasicImage.refreshDrawableState();
            layout.refreshDrawableState();
        }


    }


    //get all the images thru the jailbase API and sort them into their array "crimnials"
    public void getCriminals() throws IOException, JSONException {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //make Policy permit all
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    // New okHttp client
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .build();
                    // set media type of querry
                    MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                    // build the request
                    Request request = new Request.Builder()
                            .url("https://jailbase-jailbase.p.rapidapi.com/recent/?source_id=ar-jcso")
                            .method("GET", null)
                            .addHeader("X-RapidAPI-Key", "e451035421msh38c089d44761c42p1b4070jsn3ebb2f8a409c")
                            .addHeader("X-RapidAPI-Host", "jailbase-jailbase.p.rapidapi.com")
                            .build();
                    // execute the query
                    Response response = client.newCall(request).execute();

                    // part bellow goes into the JSON Object and sorts the images urls into their array
                    JSONObject Jobject = new JSONObject(response.body().string());
                    JSONArray Jarray = Jobject.getJSONArray("records");

                    for (int i = 0; i < Jarray.length(); i++) {
                        JSONObject object = Jarray.getJSONObject(i);
                        criminals = Arrays.copyOf(criminals, criminals.length + 1);
                        criminals[criminals.length - 1] = object.getString("mugshot");
                        System.out.println(object.getString("mugshot"));
                    }
                    latch.countDown();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    //get all the images thru the pexels API and sort them into their array "stockPeople"
    public void getStockPeople() throws IOException, JSONException {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // Make API call here
                    //make Policy permit all
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    // build the request
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .build();
                    // set media type of querry
                    MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                    // build the request
                    Request request = new Request.Builder()
                            .url("https://pexelsdimasv1.p.rapidapi.com/v1/search?query=mugshot&locale=en-US&per_page=2")
                            .method("GET", null)
                            .addHeader("Authorization", "563492ad6f91700001000001ddd8be82192b429ab31f5adc64661679")
                            .addHeader("X-RapidAPI-Key", "e451035421msh38c089d44761c42p1b4070jsn3ebb2f8a409c")
                            .addHeader("X-RapidAPI-Host", "PexelsdimasV1.p.rapidapi.com")
                            .build();
                    // execute the query
                    Response response = client.newCall(request).execute();

                    // part bellow goes into the JSON Object and sorts the images urls into their array
                    JSONObject Jobject = new JSONObject(response.body().string());
                    JSONArray Jarray = Jobject.getJSONArray("photos");
                    for (int i = 0; i < Jarray.length(); i++) {
                        JSONObject object = Jarray.getJSONObject(i);
                        System.out.println(object.toString());

                        stockPeople = Arrays.copyOf(stockPeople, stockPeople.length + 1);
                        String srcString = object.getJSONObject("src").getString("tiny");
                        stockPeople[stockPeople.length - 1] = srcString;
                        System.out.println(srcString);
                        latch.countDown();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public void getCats() throws IOException, JSONException {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
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
                    latch.countDown();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    public void getDogs() throws IOException, JSONException {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // Make API call here
                try {
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
                    latch.countDown();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override // if the sensor Changed then set the X and Y accordingly = movement
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0]; // get X
        float y = sensorEvent.values[1]; // get Y
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics); // get Screen Metrics
        int height = displayMetrics.heightPixels; // Screen Height
        int width = displayMetrics.widthPixels; // Screen Width

        //get image so i can move it
        ImageView ivBasicImage = (ImageView) findViewById(R.id.dumbFuckingPicture);
        // Move the Image according to theit current X and Y postion and the Sensor value
        //*255 for accalarted speed
        ivBasicImage.setX(ivBasicImage.getX() + y * 255);
        ivBasicImage.setY(ivBasicImage.getY() + x * 255);

        //here i see if the image has left the bounds on eiter of the 4 sides and if did
        // i call  the function checkCategory which is responsible for checking if correct or not
        if (ivBasicImage.getY() > height) {
            System.out.println("down");
            try {
                checkCategory("down");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (ivBasicImage.getY() <= 0) {
            System.out.println("up");
            try {
                checkCategory("up");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (ivBasicImage.getX() > width) {
            System.out.println("right");
            try {
                checkCategory("right");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else if (ivBasicImage.getX() <= 0) {
            System.out.println("left");
            try {
                checkCategory("left");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    // This function is responsible for checking the direction of the moved image and act accordingly
    public void checkCategory(String direction) throws InterruptedException {
        // get image to set X and Y
        ImageView ivBasicImage = (ImageView) findViewById(R.id.dumbFuckingPicture);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            //This i get to check the category of the image later
            String category = (String) ivBasicImage.getAccessibilityPaneTitle();

            //checks direction and each direction is assigned to a category
            // if correct I call the addToScore function to add to score
            // spawn to respawn a new image
            /* ivBasicImage.setX(300) ivBasicImage.setY(1000); These lines are for resetting position
            Back to middle*/
            // If wrong gameOver gets called
            if (direction == "down") {
                if (category == "stock") {
                    addToScore();
                    spawn();
                    ivBasicImage.setX(300);
                    ivBasicImage.setY(1000);
                } else {
                    gameOver();
                }
            } else if (direction == "up") {
                if (category == "criminal") {
                    addToScore();
                    spawn();
                    ivBasicImage.setX(300);
                    ivBasicImage.setY(1000);
                } else {
                    gameOver();
                }
            }else if (direction == "left") {
                if (category == "cat") {
                    addToScore();
                    spawn();
                    ivBasicImage.setX(300);
                    ivBasicImage.setY(1000);
                } else {
                    gameOver();
                }
            }else if (direction == "right") {
                if (category == "dog") {
                    addToScore();
                    spawn();
                    ivBasicImage.setX(300);
                    ivBasicImage.setY(1000);
                } else {
                    gameOver();
                }
            }


        }
    }

    // this function says what it does: It adds 1 to score and gets called if right
    public void addToScore() {
        final TextView scoreOnView = this.<TextView>findViewById(R.id.Score);
        score++;
        scoreOnView.setText(Integer.toString(score));
        if (score > highscore) {
            highscore++;
        }

    }

    //Makes Player loose and send them to game over sceen / activity with theit score and highscore
    public void gameOver() {
        yourCountDownTimer.cancel(); // stop timer
        Intent i = new Intent(GameActivity.this, GameOver.class); // new intent
        i.putExtra("score", score); // send score with intent
        i.putExtra("highscore", highscore); // send highscore with intent
        startActivity(i); // start the new acticivity with the new intent
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    //here the countdown starts and starts counting down from 30 seconds
    // onFinish gameOver gets called again
    public void gameOverByTimeout() {
        TextView countdownOnView = this.<TextView>findViewById(R.id.countdown);
        //stack overflow help i found for this: https://stackoverflow.com/questions/10032003/how-to-make-a-countdown-timer-in-android?newreg=c2817f9bbeaa4cf3afe7c3db9ebe9833
        yourCountDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long ms) {
                countdownOnView.setText("countdown: " + ms / 1000); // setting time left on View
            }

            public void onFinish() {
                gameOver();
            }
        }.start();

    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this, gyro); // on pause we unregister gyro for performance
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
            getCriminals(); // call the api
            getStockPeople(); // cal the api
            getDogs();
            getCats();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            spawn();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // On pause we register a listener for the gyro sensor
        sensorManager.registerListener(this, gyro, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
