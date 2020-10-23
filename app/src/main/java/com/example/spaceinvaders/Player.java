package com.example.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class Player {
    private Bitmap bitmap;
    private int x;
    private int y;
    private final int height;
    private final int width;
    private int xVelocity = 5;

    public Player(Bitmap bitmap, int width, int height){
        this.bitmap = bitmap;
        this.width = width;
        this.height = height;
        this.x = MainActivity.getScreenWidth() / 2 - this.width / 2;
        this.y = MainActivity.getScreenHeight() - this.height - 20;
    }

    public void update(){
        if(xVelocity < 0){      //go left
            if(x > 0 + Math.abs(xVelocity)){
                x += xVelocity;
            }
        }else{                  //go right
            if(x < MainActivity.getScreenWidth() - width - Math.abs(xVelocity)){
                x += xVelocity;
            }
        }

    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmap, x, y, null);
    }

    public void moveLeft(){
        xVelocity = -5;
    }

    public void moveRight(){
        xVelocity = 5;
    }

    public void shoot(){
        Log.d("touch", "shoot");

    }

    public int getBulletX(){
        return x + width / 2;
    }

    public int getY(){
        return this.y;
    }

    public Rect getRect(){
        return new Rect(x, y, x + width, y + height);
    }

    public int getX(){
        return this.x;
    }

    public void moveAway(){
        y = MainActivity.getScreenHeight() + height + 50;
    }
}
