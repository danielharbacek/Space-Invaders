package com.example.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

public class Player {
    private Bitmap bitmap;
    private int x;
    private int y;
    private final int height;
    private final int width;
    private int xVelicoty = 5;

    public Player(Bitmap bitmap, int width, int height){
        this.bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);;
        this.width = width;
        this.height = height;
        this.x = MainActivity.getScreenWidth() / 2 - this.width / 2;
        this.y = MainActivity.getScreenHeight() - this.height - 100;
    }

    public void update(){
        if(xVelicoty < 0){      //go left
            if(x > 0 + Math.abs(xVelicoty)){
                x += xVelicoty;
            }
        }else{                  //go right
            if(x < MainActivity.getScreenWidth() - width - Math.abs(xVelicoty)){
                x += xVelicoty;
            }
        }

    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmap, x, y, null);
    }

    public void moveLeft(){
        xVelicoty = -5;
    }

    public void moveRight(){
        xVelicoty = 5;
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
}
