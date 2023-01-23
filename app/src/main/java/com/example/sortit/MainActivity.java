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
    /*INTIZALISE  score and highscore*/
    int score ;
    int highscore ;
    private static final String SCORE_STATE = "score"; // intialize score state for save state
    private SharedPreferences preferences; // intizalise Shared preferences also fore save state

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        score= 0;
        highscore = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // the view context of the activity
        this.preferences = getPreferences(MODE_PRIVATE); // get Prefrences in private mode
        // if saved state isnt empty and contains our Score State
        if(savedInstanceState != null && savedInstanceState.containsKey(SCORE_STATE)) {
            TextView viewHighscore = (TextView) findViewById(R.id.highscore1);
            int i = 0;
            i = savedInstanceState.getInt(SCORE_STATE);
            viewHighscore.setText(Integer.toString(i)); // set the highscore to our saved state
        } else {
            TextView viewHighscore = (TextView) findViewById(R.id.highscore1);
            int i = 0;
            i = preferences.getInt(SCORE_STATE,i);
            viewHighscore.setText(Integer.toString(i));

        }
        renderHighscore(); // calls the rendering of the highscore
        Bundle extras = getIntent().getExtras(); // get intent from gameOver
        if (extras != null) { // if intent not empty
            score = extras.getInt("score"); // get score for no reason
            highscore = extras.getInt("highscore"); // get highscore ...
            TextView viewHighscore = (TextView) findViewById(R.id.highscore1);
            viewHighscore.setText(Integer.toString(highscore));// ..to set it on view
            saveCount(); // save the highscore

        }

        final Button button = this.<Button>findViewById(R.id.startGameBtn); // register button from view
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { // onClick of that button
                // Code here executes on main thread after user presses button
                // in our case we switch to GameActivity
                Intent i  = new Intent(MainActivity.this, GameActivity.class); // new intent
                i.putExtra("score",score); // put score into intent
                i.putExtra("highscore",highscore); // put highscore into intent
                startActivity(i); // start GameActivity with our intent
            }
        });
    }

    @Override //on Saved Instance State we set the highscore
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        TextView viewHighscore = (TextView) findViewById(R.id.highscore1);
        int i = Integer.parseInt((String) viewHighscore.getText());
        outState.putInt(SCORE_STATE, i);
        super.onSaveInstanceState(outState);
    }
    // Save highscore into State
    private void saveCount() {
        TextView viewHighscore = (TextView) findViewById(R.id.highscore1);
        SharedPreferences.Editor preferencesEditor = this.preferences.edit();
        int i = Integer.parseInt((String) viewHighscore.getText());
        preferencesEditor.putInt(SCORE_STATE, i);
        preferencesEditor.apply();
    }
    // render the highscore
    private void renderHighscore() {
        TextView viewHighscore = (TextView) findViewById(R.id.highscore1);
        int i = Integer.parseInt((String) viewHighscore.getText());
        if (viewHighscore!= null) {
            viewHighscore.setText(String.valueOf(i));
            saveCount();
        }
    }
    }
