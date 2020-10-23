package com.example.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

public class Enemies {
    private ArrayList<Enemy> enemies;

    public Enemies(){
        enemies = new ArrayList<Enemy>();
    }

    public void spawnEnemy(Bitmap bitmap, int width, int height, int speed, int points){
        enemies.add(new Enemy(bitmap, width, height, speed, points));
    }

    public void update(){
        for (Enemy enemy : enemies) {
            enemy.update();
            if(enemy.getY() > MainActivity.getScreenHeight()){
                enemies.remove(enemy);
            }
        }
    }

    public void draw(Canvas canvas){
        for (Enemy enemy : enemies) {
            enemy.draw(canvas);
        }
    }

    public ArrayList<Enemy> getEnemies(){
        return this.enemies;
    }

    public void removeEnemy(Enemy enemy){
        enemies.remove(enemy);
    }

}
