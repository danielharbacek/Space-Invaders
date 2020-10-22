package com.example.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

public class Bullets {
    private ArrayList<Bullet> bullets;
    private int width;
    private int height;

    public Bullets(int count, int width, int height){
        bullets = new ArrayList<Bullet>();
        this.width = width;
        this.height = height;
    }

    public void addNewBullet(Bitmap bitmap, int x, int y, String shooter){
        bullets.add(new Bullet(bitmap, x ,y, width, height, shooter));
    }

    public void update(){
        for (Bullet bullet : bullets) {
            bullet.update();
        }
    }

    public void draw(Canvas canvas){
        for (Bullet bullet : bullets) {
            bullet.draw(canvas);
        }
    }
}
