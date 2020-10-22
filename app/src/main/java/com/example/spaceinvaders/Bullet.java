package com.example.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Bullet {
    private Bitmap bitmap;
    private int x;
    private int y;
    private int width;
    private int height;
    private String shooter;

    public Bullet(Bitmap bitmap, int x, int y, int width, int height, String shooter){
        this.bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.shooter =shooter;
    }

    public void update(){
        if(shooter == "player"){
            y -= 15;
        }else{
            y += 15;
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmap, x, y, null);
    }
}
