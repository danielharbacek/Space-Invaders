package com.example.spaceinvaders;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

public class Enemy {
    private Bitmap bitmap;
    private int x;
    private int y;
    private int width;
    private int height;
    private int speed;
    private int points;
    private int coins;
    private boolean isDead;

    public Enemy(Bitmap bitmap, int width, int height, int speed, int points, int coins){
        this.bitmap = bitmap;
        this.x = randomNumber(0, MainActivity.getScreenWidth() - width);
        this.y = 200 - height;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.points = points;
        this.coins = coins;
        isDead = false;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmap, x, y, null);
    }

    public void update(){
        moveDown();
    }

    private void moveDown() {
        y += speed;
    }

    public void setSpeed(int speed){
        this.speed = speed;
    }

    private int randomNumber(int min, int max){
        Random rn = new Random();
        int range = max - min + 1;
        int randomNum =  rn.nextInt(range) + min;

        return randomNum;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public Rect getRect(){
        return new Rect(x, y, x + width, y + height);
    }

    public int getPoints(){
        return this.points;
    }

    public int getCoins(){
        return this.coins;
    }

    public void die(){
        isDead = true;
    }

    public boolean isDead(){
        return isDead;
    }
}
