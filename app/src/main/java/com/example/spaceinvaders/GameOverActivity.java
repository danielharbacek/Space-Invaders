package com.example.spaceinvaders;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {

    private TextView scoreText;
    private TextView highscoreText;
    private TextView coinsText;
    private TextView newHighscoreText;

    SharedPreferences sharedprefs;
    public static final String PrefsName = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        scoreText = findViewById(R.id.scoreText);
        highscoreText = findViewById(R.id.highscoreText);
        coinsText = findViewById(R.id.coinsText);
        newHighscoreText = findViewById(R.id.newHighscore);

        sharedprefs = getSharedPreferences(PrefsName, 0);

        int highscore = sharedprefs.getInt("highscore", 0);
        int score = getIntent().getIntExtra("score", -1);
        int coins = sharedprefs.getInt("coins", -1);
        int earnedCoins = getIntent().getIntExtra("coins", -1);

        if(highscore < score){
            highscore = score;
            newHighscoreText.setVisibility(View.VISIBLE);
        }else{
            newHighscoreText.setVisibility(View.INVISIBLE);
        }

        scoreText.setText("Score: " + score);
        highscoreText.setText("High score: " + highscore);
        coinsText.setText("Coins: " + coins + earnedCoins + " (+" + earnedCoins + ")");

    }

    public void proceed(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra("theme", false);
        startActivity(intent);
    }
}