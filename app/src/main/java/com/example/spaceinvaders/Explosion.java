package com.example.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Explosion {
    Bitmap[] bitmaps;
    Bitmap bitmap;
    private int x;
    private int y;
    private int coins;
    private int progress;
    private Paint paint;

    public Explosion(Bitmap[] bitmaps, int x, int y){
        this.bitmaps = bitmaps;
        this.x = x;
        this.y = y;
        progress = 0;
    }

    public Explosion(Bitmap[] bitmaps, int x, int y, Bitmap bitmap, int coins, Paint paint){
        this.bitmaps = bitmaps;
        this.x = x;
        this.y = y;
        this.bitmap = bitmap;
        this.coins = coins;
        this.paint = paint;
        progress = 0;
    }

    public int drawNextFrame(Canvas canvas){
        canvas.drawBitmap(bitmaps[progress++], x, y, null);

        if(bitmap != null){
            canvas.drawBitmap(bitmap, x + 20, y - 50 - progress, null);
            canvas.drawText("+" + coins, x + 70, y - 20 - progress, paint);
        }

        return progress;
    }
}
