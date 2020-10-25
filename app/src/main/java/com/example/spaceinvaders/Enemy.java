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
    private int health;

    public Enemy(Bitmap bitmap, int width, int height, int speed, int points, int coins, int health){
        this.bitmap = bitmap;
        this.x = randomNumber(0, MainActivity.getScreenWidth() - width);
        this.y = 200 - height;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.points = points;
        this.coins = coins;
        this.health = health;
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

    private int randomNumber(int min, int max){
        Random rn = new Random();
        int range = max - min + 1;
        int randomNum =  rn.nextInt(range) + min;

        return randomNum;
    }

    public void decreaseSpeed(){
        speed /= 2;
    }

    public void increaseSpeed(){
        speed *= 2;
    }

    public Bitmap getBitmap(){
        return this.bitmap;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

    public int getHealth(){
        return this.health;
    }

    public int getSpeed(){
        return this.speed;
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

    public int decreaseHealth(int damage){
        health -= damage;

        return health;
    }
}
