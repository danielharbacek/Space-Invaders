package com.example.spaceinvaders;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ChoosingActivity extends AppCompatActivity {

    SharedPreferences sharedprefs;
    public static final String PrefsName = "MyPrefs";

    private static final String[] shipNames = {
            "Battlestar Galactica",
            "USS Enterprise",
            "Millennium Falcon",
            "The Resolute",
            "Serenity"
    };

    private static final String[] levelNames = {
            "Mars",
            "Jupiter",
            "Earth",
    };

    private Bitmap[] ships;
    private Bitmap[] levels;

    private int currentShip = 0;
    private int shipsCount;

    private int currentLevel = 0;
    private int levelsCount = 3;

    private int[] shipIndexes;

    private TextView shipName;
    private TextView levelName;

    private ImageView shipImage;
    private ImageView levelImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing);

        sharedprefs = getSharedPreferences(PrefsName, 0);

        shipImage = findViewById(R.id.shipImage);
        shipName = findViewById(R.id.shipName);
        levelImage = findViewById(R.id.levelImage);
        levelName = findViewById(R.id.levelName);

        shipsCount = getShipsCount();
        loadBitmaps();

        shipName.setText(shipNames[shipIndexes[currentShip]]);
        levelName.setText(levelNames[currentLevel]);
    }

    private int getShipsCount(){
        int count = 0;
        shipIndexes = new int[5];

        for(int i = 0; i < 5; i++){
            if(sharedprefs.getBoolean("ship" + i, false)){
                shipIndexes[count] = i;
                count++;
            }
        }

        return count;
    }

    private void loadBitmaps(){
        ships = new Bitmap[shipsCount];
        levels = new Bitmap[levelsCount];

        for(int i = 0; i < shipsCount; i++){
            ships[i] = BitmapFactory.decodeResource(getResources(),
                    getResources().getIdentifier("ship"+(shipIndexes[i]+1), "drawable", getPackageName()));
        }

        for(int i = 0; i < levelsCount; i++){
            levels[i] = BitmapFactory.decodeResource(getResources(),
                    getResources().getIdentifier("level"+(i+1), "drawable", getPackageName()));
        }
    }

    public void arrowRight(View view) {
        if(shipsCount == 1){
            Toast.makeText(this, "You have only one ship.", Toast.LENGTH_SHORT).show();
            return;
        }

        currentShip = ++currentShip % ships.length;
        shipImage.setImageBitmap(ships[currentShip]);

        shipName.setText(shipNames[shipIndexes[currentShip]]);
    }

    public void arrowLeft(View view) {
        if(shipsCount == 1){
            Toast.makeText(this, "You have only one ship.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(currentShip == 0){
            currentShip = ships.length-1;
            shipImage.setImageBitmap(ships[currentShip]);
        }else{
            shipImage.setImageBitmap(ships[--currentShip]);
        }

        shipName.setText(shipNames[shipIndexes[currentShip]]);
    }

    public void start(View view) {
        Intent intent = new Intent(ChoosingActivity.this, MainActivity.class);

        SharedPreferences.Editor editor = sharedprefs.edit();
        editor.putString("background", "background" + (currentLevel+1));
        editor.putString("ship", "ship" + (shipIndexes[currentShip]+1));
        editor.commit();

        startActivity(intent);
    }

    public void back(View view) {
        Intent intent = new Intent(ChoosingActivity.this, MenuActivity.class);
        intent.putExtra("theme", false);
        startActivity(intent);
    }

    public void arrowLevelLeft(View view) {
        if(levelsCount == 1){
            Toast.makeText(this, "There is only one level.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(currentLevel == 0){
            currentLevel = levels.length-1;
            levelImage.setImageBitmap(levels[currentLevel]);
        }else{
            levelImage.setImageBitmap(levels[--currentLevel]);
        }

        levelName.setText(levelNames[currentLevel]);
    }

    public void arrowLevelRight(View view) {
        if(levelsCount == 1){
            Toast.makeText(this, "There is only one level.", Toast.LENGTH_SHORT).show();
            return;
        }

        currentLevel = ++currentLevel % levels.length;
        levelImage.setImageBitmap(levels[currentLevel]);

        levelName.setText(levelNames[currentLevel]);
    }
}