package com.example.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

public class Enemies {
    private ArrayList<Enemy> enemies;

    public Enemies(){
        enemies = new ArrayList<>();
    }

    public void spawnEnemy(Bitmap bitmap, int width, int height, int speed, int points, int coins){
        enemies.add(new Enemy(bitmap, width, height, speed, points, coins));
    }

    public void update(){
        //faster than for-each version
        for(int i = 0; i < enemies.size(); i++){
            if(enemies.get(i).getY() > MainActivity.getScreenHeight() + 20){
                enemies.remove(i);
            }
            enemies.get(i).update();
        }

        /*
        for (Enemy enemy : enemies) {
            if(enemy.getY() > MainActivity.getScreenHeight() + 20){
                enemies.remove(enemy);
            }
            enemy.update();
        }*/
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
