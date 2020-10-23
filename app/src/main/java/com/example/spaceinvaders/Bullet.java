package com.example.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Bullet {
    private Bitmap bitmap;
    private int x;
    private int y;
    private int width;
    private int height;
    private int speed;
    private String shooter;

    public Bullet(Bitmap bitmap, int x, int y, int width, int height, int speed, String shooter){
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.shooter = shooter;
    }

    public void update(){
        if(shooter == "player"){
            y -= speed;
        }else{
            y += speed;
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmap, x, y, null);
    }

    public Rect getRect(){
        return new Rect(x, y, x + width, y + height);
    }

    public int getX(){
        return this.x;
    }
}
