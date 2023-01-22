package com.example.sortit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int score ;
    int highscore ;





    @Override
        protected void onCreate(Bundle savedInstanceState) {
        //TODO: Save the Highscore even on close andd everything
        score= 0;
        highscore = 0;
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            score = extras.getInt("score");
            highscore = extras.getInt("highscore");

            TextView viewHighscore = (TextView) findViewById(R.id.highscore1);
            viewHighscore.setText(" " + highscore);

        }

        final Button button = this.<Button>findViewById(R.id.startGameBtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                // in our case we switch to GameActivity
                Intent i  = new Intent(MainActivity.this, GameActivity.class);

                i.putExtra("score",score);
                i.putExtra("highscore",highscore);
                startActivity(i);

            }
        });
    }
    }
