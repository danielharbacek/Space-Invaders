package com.example.spaceinvaders;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Explosion {
    Bitmap[] bitmaps;
    private int x;
    private int y;
    private int progress;

    public Explosion(Bitmap[] bitmaps, int x, int y){
        this.bitmaps = bitmaps;
        this.x = x;
        this.y = y;
        progress = 0;
    }

    public int drawNextFrame(Canvas canvas){
        canvas.drawBitmap(bitmaps[progress++], x, y, null);

        return progress;
    }
}
