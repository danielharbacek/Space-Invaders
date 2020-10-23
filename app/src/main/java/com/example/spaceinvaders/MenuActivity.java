package com.example.spaceinvaders;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    MediaPlayer theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        theme = MediaPlayer.create(this, R.raw.theme);
        theme.setLooping(true);
        theme.start();
    }

    public void play(View view) {
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void exit(View view) {
        System.exit(0);
    }

    public void upgrade(View view) {
        Intent intent = new Intent(MenuActivity.this, UpgradeActivity.class);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(data == null)
            return;

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100){

        }
    }
}