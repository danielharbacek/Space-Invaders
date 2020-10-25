package com.example.spaceinvaders;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity implements LifecycleObserver{

    private MediaPlayer theme;

    SharedPreferences sharedprefs;
    public static final String PrefsName = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        sharedprefs = getSharedPreferences(PrefsName, 0);
        SharedPreferences.Editor editor = sharedprefs.edit();
        editor.putBoolean("ship0", true);
        editor.commit();

        theme = MediaPlayer.create(this, R.raw.theme);
        theme.setLooping(true);

        if(getIntent().getBooleanExtra("theme", true)){
            theme.start();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded() {
        if(theme != null){
            if(theme.isPlaying()){
                theme.stop();
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppForegrounded() {
        if(theme != null){
            if(!theme.isPlaying()){
                theme.start();
            }
        }
    }

    public void play(View view) {
        Intent intent = new Intent(MenuActivity.this, ChoosingActivity.class);
        startActivity(intent);
    }

    public void exit(View view) {
        System.exit(0);
    }

    public void upgrade(View view) {
        Intent intent = new Intent(MenuActivity.this, UpgradeActivity.class);
        startActivity(intent);
    }

}
