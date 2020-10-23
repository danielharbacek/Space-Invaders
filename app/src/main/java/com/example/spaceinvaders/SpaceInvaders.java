package com.example.spaceinvaders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SpaceInvaders extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder;
    private MainThread mainThread;

    private Enemies enemies;
    private Player player;
    private Bullets bullets;
    private ArrayList<Explosion> explosions;

    int playerWidth = 120;
    int playerHeight = 100;
    int enemyWidth = 100;
    int enemyHeight = 100;
    int bulletWidth = 30;
    int bulletHeight = 60;
    int hearthWidth = 50;
    int hearthHeight = 50;
    int coinWidth = 45;
    int coinHeight = 45;

    int enemySpeed = 10;
    int bulletSpeed = 40;

    private Bitmap background;
    private Bitmap playerBitmap;
    private Bitmap bulletBitmap;
    private Bitmap enemyBitmap01;
    private Bitmap hearthBitmap;
    private Bitmap coinBitmap;
    private Bitmap[] explosion;
    private Bitmap[] playerExplosion;

    private int shootRate = 1000;               //in miliseconds
    private int enemySpawnRate = 2000;          //in miliseconds
    private int streakRate = 3000;               //in miliseconds
    private int gameOverDelay = 2000;

    private int enemyPoints;
    private int enemyCoins;
    private int score = 0;
    private int streak = 0;
    private int coins = 0;
    private int maxLives = 3;
    private int lives;
    private int highScore;
    private boolean isGameOver = false;
    private long lastShot = 0;

    Timer spawnTimer;
    Timer shotTimer;

    MediaPlayer shotSound;
    MediaPlayer explosionSound;

    Paint paint;

    SharedPreferences sharedprefs;
    public static final String PrefsName = "MyPrefs";
    
    public SpaceInvaders(Context context) {
        super(context);
        init();
    }

    public SpaceInvaders(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpaceInvaders(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        mainThread = new MainThread(surfaceHolder, this);
        setFocusable(true);

        playerBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.ship1), playerWidth, playerHeight, false);
        bulletBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.rocket), bulletWidth, bulletHeight, false);
        enemyBitmap01 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.enemy4), enemyWidth, enemyHeight, false);
        background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.background), MainActivity.getScreenWidth(), MainActivity.getScreenHeight(), false);
        hearthBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.hearth), hearthWidth, hearthHeight, false);
        coinBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.coin), coinWidth, coinHeight, false);
        fillExplosionBitmap();

        enemies = new Enemies();
        player = new Player(playerBitmap, playerWidth, playerHeight);
        bullets = new Bullets();
        explosions = new ArrayList<>();

        lives = maxLives;
        enemyPoints = 10;
        enemyCoins = 2;

        spawnTimer = new Timer();
        spawnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                enemies.spawnEnemy(enemyBitmap01, enemyWidth, enemyHeight, enemySpeed, enemyPoints, enemyCoins);
            }
        }, enemySpawnRate, enemySpawnRate);

        shotTimer = new Timer();
        shotTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                shotSound.start();
                bullets.addNewBullet(bulletBitmap, player.getBulletX() - bulletWidth / 2, player.getY() - bulletHeight / 2,
                        bulletWidth, bulletHeight, bulletSpeed, "player");
            }
        }, shootRate, shootRate);

        shotSound = MediaPlayer.create(getContext(), R.raw.shot1);
        explosionSound = MediaPlayer.create(getContext(), R.raw.explosion);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(40);

        sharedprefs = getContext().getSharedPreferences(PrefsName, 0);
        highScore = sharedprefs.getInt("highscore", 0);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        mainThread.setRunning(true);
        mainThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean retry = true;

        while(retry){
            try{
                mainThread.setRunning(false);
                mainThread.join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            retry = false;
        }

        saveHighscoreAndCoins();
    }

    public void update(){
        enemies.update();
        bullets.update();
        player.update();
        calculateStreak();
        if(lives <= 0 && !isGameOver){
            gameOver();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if(canvas != null){
            canvas.drawBitmap(background, 0, 0, null);
            enemies.draw(canvas);
            player.draw(canvas);
            bullets.draw(canvas);

            for(Explosion explosion : explosions){
                if(explosion.drawNextFrame(canvas) == 17){
                    explosions.remove(explosion);
                }
            }

            drawStats(canvas);
            checkCollision(canvas);
        }
    }

    public void movePlayerLeft(){
        player.moveLeft();
    }

    public void movePlayerRight(){
        player.moveRight();
    }

    private void checkCollision(Canvas canvas){
        for(Enemy enemy : enemies.getEnemies()){
            for (Bullet bullet : bullets.getBullets()){
                if(Rect.intersects(enemy.getRect(), bullet.getRect())){
                    enemyShot(enemy, bullet);
                    showCoinsCollected(canvas);
                }
            }
            if(Rect.intersects(enemy.getRect(), player.getRect())){
                lives--;
                enemies.removeEnemy(enemy);
                explosions.add(new Explosion(explosion, enemy.getX(), enemy.getY()+20));
            }
            if(enemy.getY() >= MainActivity.getScreenHeight() && !enemy.isDead()){
                lives--;
                enemy.die();
            }
        }
    }

    private void enemyShot(Enemy enemy, Bullet bullet){
        lastShot = System.currentTimeMillis();
        streak++;
        score += enemy.getPoints() + ( (streak - 1) * enemy.getPoints() );
        coins += enemy.getCoins();

        enemies.removeEnemy(enemy);
        bullets.removeBullet(bullet);

        explosions.add(new Explosion(explosion, enemy.getX(), enemy.getY()));
    }

    private void fillExplosionBitmap(){
        explosion = new Bitmap[17];
        playerExplosion = new Bitmap[17];

        for(int i = 0; i < 17; i++){
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                    getResources().getIdentifier("explosion"+(i+1), "drawable", getContext().getPackageName()));
            explosion[i] = Bitmap.createScaledBitmap(bitmap, enemyWidth, enemyHeight, false);
            playerExplosion[i] = Bitmap.createScaledBitmap(bitmap, playerWidth, playerHeight, false);
        }
    }

    private void drawStats(Canvas canvas){
        paint.setColor(Color.BLACK);
        canvas.drawRect(new Rect(0, 0, MainActivity.getScreenWidth(), 150), paint);
        for(int i = 0; i < lives; i++){
            canvas.drawBitmap(hearthBitmap, 20 + (hearthWidth + 10) * i, 25, null);
        }
        canvas.drawBitmap(coinBitmap, 20, 25 + hearthHeight + 15, null);

        paint.setColor(Color.WHITE);
        canvas.drawText(coins + "", 20 + coinWidth + 15, 25 + hearthHeight + 52, paint);
        String scoreText = "Score: " + score;
        String streakText = "Streak: " + streak;
        //float scoreTextLength = (float) (scoreText.length() * paint.getTextSize() / 2.2);
        //float streakTextLength = (float) (streakText.length() * paint.getTextSize() / 2.2);
        canvas.drawText(scoreText, MainActivity.getScreenWidth() - 200, 60, paint);
        canvas.drawText(streakText, MainActivity.getScreenWidth() - 200, 120, paint);
    }

    private void calculateStreak(){
        if(System.currentTimeMillis() - lastShot > streakRate){
            streak = 0;
        }
    }

    private void saveHighscoreAndCoins(){
        SharedPreferences.Editor editor = sharedprefs.edit();

        if(score > highScore){
            editor.putInt("highscore", score);
        }

        int allCoins = sharedprefs.getInt("coins", 0) + coins;

        editor.putInt("coins", allCoins);

        editor.putInt("coins", allCoins);

        editor.commit();
    }

    private void showCoinsCollected(Canvas canvas){
        //TODO: show coins gained when enemy is killed
    }

    private void gameOver(){
        isGameOver = true;
        shotTimer.cancel();
        explosionSound.start();
        explosions.add(new Explosion(playerExplosion, player.getX(), player.getY()));
        player.moveAway();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                spawnTimer.cancel();
                Intent intent = new Intent(getContext(), GameOverActivity.class);
                intent.putExtra("score", score);
                intent.putExtra("coins", coins);
                getContext().startActivity(intent);
            }
        }, gameOverDelay);
    }
}
