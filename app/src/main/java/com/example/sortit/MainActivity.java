package com.example.sortit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Score score = new Score();





    @Override
        protected void onCreate(Bundle savedInstanceState) {
        score.setScore(0);
        score.setHighscore(0);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final Button button = this.<Button>findViewById(R.id.startGameBtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                // in our case we switch to GameActivity
                Intent i  = new Intent(MainActivity.this, GameActivity.class);
                System.out.println(score.getScore()+" "+score.getHighscore());
                i.putExtra("score",score.getScore());
                i.putExtra("highscore",score.getHighscore());
                startActivity(i);

            }
        });
    }
    }
