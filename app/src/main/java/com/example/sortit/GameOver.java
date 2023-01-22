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
    int score;
    int highscore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get score and highscore from mainActivity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            score = extras.getInt("score");
            highscore = extras.getInt("highscore");
            TextView viewScore = (TextView) findViewById(R.id.score);
            TextView viewHighscore = (TextView) findViewById(R.id.highscore);
            viewScore.setText("Score: " + score);
            viewHighscore.setText("Highscore: " + highscore);

        }


        final Button button = this.<Button>findViewById(R.id.back);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                // in our case we switch to GameActivity
                Intent i  = new Intent(GameOver.this, MainActivity.class);

                i.putExtra("score",score);
                i.putExtra("highscore",score);
                startActivity(i);

            }
        });
    }
}
