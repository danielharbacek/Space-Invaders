package com.example.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

public class Bullets {
    private ArrayList<Bullet> bullets;

    public Bullets(){
        bullets = new ArrayList<Bullet>();
    }

    public void addNewBullet(Bitmap bitmap, int x, int y, int width, int height, int speed, String shooter){
        bullets.add(new Bullet(bitmap, x ,y, width, height, speed, shooter));
    }

    public void update(){
        for (Bullet bullet : bullets) {
            bullet.update();
            if(bullet.getX() < -50){
                bullets.remove(bullet);
            }
        }
    }

    public void draw(Canvas canvas){
        for (Bullet bullet : bullets) {
            bullet.draw(canvas);
        }
    }

    public ArrayList<Bullet> getBullets(){
        return this.bullets;
    }

    public void removeBullet(Bullet bullet){
        bullets.remove(bullet);
    }
}
