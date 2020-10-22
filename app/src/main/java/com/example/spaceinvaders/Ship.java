package com.example.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

public class Ship {
    private Bitmap bitmap;
    private int x;
    private int y;
    private final int height;
    private final int width;

    public Ship(Bitmap bitmap, int width, int height){
        this.bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);;
        this.width = width;
        this.height = height;
        this.x = MainActivity.getScreenWidth() / 2 - this.width / 2;
        this.y = MainActivity.getScreenHeight() / 2 - this.height / 2;
    }

    public void update(){

    }

    public void onDraw(Canvas canvas){
        canvas.drawBitmap(bitmap, x, y, null);
    }
}
