package com.example.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

public class Invaders {
    private ArrayList<Invader> invaders;
    private int width;
    private int height;

    public Invaders(int count, int width, int height){
        invaders = new ArrayList<Invader>();
        this.width = width;
        this.height = height;
    }

    public void addNewInvader(Bitmap bitmap, int x, int y){
        invaders.add(new Invader(bitmap, x ,y, width, height));
    }

    public void update(){
        for (Invader invader : invaders) {
            invader.update();
        }
    }

    public void draw(Canvas canvas){
        for (Invader invader : invaders) {
            invader.draw(canvas);
        }
    }
}
