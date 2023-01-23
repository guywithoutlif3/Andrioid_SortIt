package com.example.sortit;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class GameOver extends AppCompatActivity {
    /*initsalisation of score and highscore*/
    int score;
    int highscore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get score and highscore from mainActivity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover); // the the view that is ontextually used here
        Bundle extras = getIntent().getExtras(); // get the intent
        if (extras != null) { // if intent not empty
            score = extras.getInt("score"); // get score from intend
            highscore = extras.getInt("highscore"); // get highscorew from intent
            TextView viewScore = (TextView) findViewById(R.id.score);
            TextView viewHighscore = (TextView) findViewById(R.id.highscore);
            viewScore.setText("Score: " + score);// set on View with score
            viewHighscore.setText("Highscore: " + highscore);// set on View with highscore

        }


        final Button button = this.<Button>findViewById(R.id.back);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                // in our case we switch to GameActivity
                Intent i  = new Intent(GameOver.this, MainActivity.class); // NEW INTENT

                i.putExtra("score",score);   // PUT SCORE IN
                i.putExtra("highscore",score); // PUT HIGHSCORE IN
                startActivity(i); // SEND US TO MAIN ACTIVITY AGAIN

            }
        });
    }
}
