package com.example.spaceinvaders;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class UpgradeActivity extends AppCompatActivity {

    SharedPreferences sharedprefs;
    public static final String PrefsName = "MyPrefs";
    private int coins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        sharedprefs = getSharedPreferences(PrefsName, 0);

    }
}