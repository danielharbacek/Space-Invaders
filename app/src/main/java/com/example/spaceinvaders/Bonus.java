package com.example.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

public class Bonus {
    private Bitmap bitmap;
    private int x;
    private int y;
    private int width;
    private int height;
    private int speed;
    private BonusType type;

    public Bonus(Bitmap bitmap, BonusType type, int width, int height, int speed){
        this.bitmap = bitmap;
        this.type = type;
        this.x = randomNumber(0, MainActivity.getScreenWidth() - width);
        this.y = 200 - height;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmap, x, y, null);
    }

    public void update(){
        y += speed;
    }

    public Rect getRect(){
        return new Rect(x, y, x+width, y+height);
    }

    public BonusType getType(){
        return this.type;
    }

    private int randomNumber(int min, int max){
        Random rn = new Random();
        int range = max - min + 1;
        int randomNum =  rn.nextInt(range) + min;

        return randomNum;
    }
}
