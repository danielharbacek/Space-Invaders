package com.example.spaceinvaders;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Invader {
    private Bitmap bitmap;
    private int x;
    private int y;
    private int width;
    private int height;
    int shift = 5;

    public Invader(Bitmap bitmap, int x, int y, int width, int height){
        this.bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmap, x, y, null);
    }

    public void update(){
        if(x >= MainActivity.getScreenWidth() - width){
            shift = -shift;
        }
        if(x <= -shift){
            shift = -shift;
        }

        x += shift;
    }
}
