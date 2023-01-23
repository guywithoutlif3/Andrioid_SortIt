package com.example.sortit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int score ;
    int highscore ;
    private static final String SCORE_STATE = "score";

    private SharedPreferences preferences;





    @Override
        protected void onCreate(Bundle savedInstanceState) {
        //TODO: Save the Highscore even on close andd everything
        score= 0;
        highscore = 0;
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        this.preferences = getPreferences(MODE_PRIVATE);

        Bundle extras = getIntent().getExtras();
        if(savedInstanceState != null && savedInstanceState.containsKey(SCORE_STATE)) {
            TextView viewHighscore = (TextView) findViewById(R.id.highscore1);


            int i = 0;
            i = savedInstanceState.getInt(SCORE_STATE);
            viewHighscore.setText(Integer.toString(i));
        } else {
            TextView viewHighscore = (TextView) findViewById(R.id.highscore1);

            int i = 0;
            i = preferences.getInt(SCORE_STATE,i);
            viewHighscore.setText(Integer.toString(i));

        }
        renderHighscore();
        if (extras != null) {
            score = extras.getInt("score");
            highscore = extras.getInt("highscore");

            TextView viewHighscore = (TextView) findViewById(R.id.highscore1);
            viewHighscore.setText(Integer.toString(highscore));
            saveCount();

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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        TextView viewHighscore = (TextView) findViewById(R.id.highscore1);

        int i = Integer.parseInt((String) viewHighscore.getText());
        outState.putInt(SCORE_STATE, i);
        super.onSaveInstanceState(outState);
    }
    private void saveCount() {
        TextView viewHighscore = (TextView) findViewById(R.id.highscore1);

        SharedPreferences.Editor preferencesEditor = this.preferences.edit();
        int i = Integer.parseInt((String) viewHighscore.getText());
        preferencesEditor.putInt(SCORE_STATE, i);
        preferencesEditor.apply();
    }
    private void renderHighscore() {
        TextView viewHighscore = (TextView) findViewById(R.id.highscore1);

        int i = Integer.parseInt((String) viewHighscore.getText());

        if (viewHighscore!= null) {
            System.out.println("i am in if of render count"+ i);
            viewHighscore.setText(String.valueOf(i));
            saveCount();
        }
    }
    }
