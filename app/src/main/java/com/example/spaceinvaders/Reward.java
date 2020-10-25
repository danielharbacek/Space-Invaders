package com.example.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Reward {
    int coins;
    Bitmap bitmap;
    int x;
    int y;
    int progress;

    public Reward(Bitmap bitmap, int coins, int x, int y){
        this.bitmap = bitmap;
        this.coins = coins;
        this.x = x;
        this.y = y;
    }

    public void drawNextFrame(Canvas canvas){

    }
}
