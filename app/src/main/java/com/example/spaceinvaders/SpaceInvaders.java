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
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SpaceInvaders extends SurfaceView implements SurfaceHolder.Callback {
//TODO: add pause button to surface view
//TODO: add bonus to spawn like enemies so player can catch them

    private SurfaceHolder surfaceHolder;
    private MainThread mainThread;

    private String backgroundString;
    private String shipString;

    private Enemies enemies;
    private Player player;
    private Bullets bullets;
    private ArrayList<Explosion> explosions;

    private Enemy[] enemiesToSpawn;
    private int enemiesCount = 6;
    private int currentEnemies = 1;

    int playerWidth = 110;
    int playerHeight = 120;
    int enemyWidth = 100;
    int enemyHeight = 100;
    int bulletWidth = 30;
    int bulletHeight = 60;
    int hearthWidth = 50;
    int hearthHeight = 50;
    int coinWidth = 35;
    int coinHeight = 35;

    int missileSpeed = 30;
    int laserSpeed = 60;

    private Bitmap background;
    private Bitmap playerBitmap;
    private Bitmap missileBitmap;
    private Bitmap laserBitmap;
    private Bitmap[] enemyBitmaps;
    private Bitmap hearthBitmap;
    private Bitmap coinBitmap;
    private Bitmap[] explosion;
    private Bitmap[] playerExplosionSmall;
    private Bitmap[] playerExplosion;

    private int shootRate = 500;                //in miliseconds
    private int enemySpawnRate = 1000;          //in miliseconds
    private int streakRate = 2000;              //in miliseconds
    private int nextEnemyRate = 20000 ;         //in miliseconds
    private int gameOverDelay = 3000;           //in miliseconds

    private int score = 0;
    private int streak = 0;
    private int coins = 0;
    private int maxLives;
    private int damage;
    private int speed;
    private int gun;
    private int lives;
    private int highScore;
    private boolean isGameOver = false;
    private long lastShot = 0;

    Timer spawnTimer;
    Timer shotTimer;
    Timer nextEnemyTimer;

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
        sharedprefs = getContext().getSharedPreferences(PrefsName, 0);
        highScore = sharedprefs.getInt("highscore", 0);

        loadPrefs();

        loadBitmaps();

        loadEnemiesToSpawn();

        enemies = new Enemies();
        player = new Player(playerBitmap, playerWidth, playerHeight, damage, speed);
        bullets = new Bullets();
        explosions = new ArrayList<>();

        shotSound = MediaPlayer.create(getContext(), R.raw.shot1);
        explosionSound = MediaPlayer.create(getContext(), R.raw.explosion_long);

        lives = maxLives;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(40);

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        mainThread = new MainThread(surfaceHolder, this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        mainThread.setRunning(true);
        mainThread.start();

        scheduleEnemySpawn();
        scheduleBulletShot();
        scheduleNextEnemy();
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
        spawnTimer.cancel();
        shotTimer.cancel();
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

            for(int i = 0; i < explosions.size(); i++){
                if(explosions.get(i).drawNextFrame(canvas) == 17){
                    explosions.remove(i);
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

    private void loadPrefs(){
        backgroundString = sharedprefs.getString("background", "background");
        shipString = sharedprefs.getString("ship", "ship1");

        int number = Integer.parseInt(shipString.substring(4));
        String ship = shipString.substring(0,shipString.length() - 1) + --number;

        maxLives = sharedprefs.getInt(ship + "currentlives", 0);
        damage = sharedprefs.getInt(ship + "currentdamage", 0);
        gun = sharedprefs.getInt(ship + "currentgun", 0);
        speed = sharedprefs.getInt(ship + "currentspeed", 0);

        maxLives = maxLives + 2 + (number / 2);
        damage = damage * 50 + 100 + ((number + 1) * 25) + damage*10*number;
        speed = speed + 7;
    }

    private void loadBitmaps(){
        playerBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                getResources().getIdentifier(shipString, "drawable", getContext().getPackageName())), playerWidth, playerHeight, false);
        missileBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.rocket), bulletWidth, bulletHeight, false);
        laserBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.laser), bulletWidth, bulletHeight, false);

        enemyBitmaps = new Bitmap[enemiesCount];
        for(int i = 0; i < enemiesCount; i++){
            enemyBitmaps[i] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), getResources().
                    getIdentifier("enemy" + (i+1), "drawable", getContext().getPackageName())), enemyWidth, enemyHeight, false);
        }

        background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(backgroundString,
                "drawable", getContext().getPackageName())), MainActivity.getScreenWidth(), MainActivity.getScreenHeight(), false);
        hearthBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.hearth), hearthWidth, hearthHeight, false);
        coinBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.coin), coinWidth, coinHeight, false);
        fillExplosionBitmap();
    }

    private void loadEnemiesToSpawn() {
        enemiesToSpawn = new Enemy[enemiesCount];

        enemiesToSpawn[0] = new Enemy(enemyBitmaps[0], enemyWidth, enemyHeight, 8, 10, 2, 100);
        enemiesToSpawn[1] = new Enemy(enemyBitmaps[1], enemyWidth, enemyHeight, 8, 15, 4, 200);
        enemiesToSpawn[2] = new Enemy(enemyBitmaps[2], enemyWidth, enemyHeight, 1, 20, 6, 300);
        enemiesToSpawn[3] = new Enemy(enemyBitmaps[3], enemyWidth, enemyHeight, 10, 30, 8, 450);
        enemiesToSpawn[4] = new Enemy(enemyBitmaps[4], enemyWidth, enemyHeight, 12, 40, 10, 600);
        enemiesToSpawn[5] = new Enemy(enemyBitmaps[5], enemyWidth, enemyHeight, 12, 50, 12, 800);
    }

    private void scheduleEnemySpawn(){
        spawnTimer = new Timer();
        spawnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                int position = randomNumber(0, currentEnemies-1);
                enemies.spawnEnemy(enemiesToSpawn[position].getBitmap(), enemiesToSpawn[position].getWidth(),
                        enemiesToSpawn[position].getHeight(), enemiesToSpawn[position].getSpeed(), enemiesToSpawn[position].getPoints(),
                        enemiesToSpawn[position].getCoins(), enemiesToSpawn[position].getHealth());
            }
        }, enemySpawnRate, enemySpawnRate);
    }

    private void scheduleBulletShot(){
        shotTimer = new Timer();
        shotTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                shotSound.start();
                if(gun == 0){
                    bullets.addNewBullet(missileBitmap, player.getBulletX() - bulletWidth / 2, player.getY() - bulletHeight / 2,
                            bulletWidth, bulletHeight, missileSpeed, "player");
                }else if(gun == 1){
                    bullets.addNewBullet(laserBitmap, player.getBulletX() - bulletWidth / 2, player.getY() - bulletHeight / 2,
                            bulletWidth, bulletHeight, laserSpeed, "player");
                }else if(gun == 2){
                    bullets.addNewBullet(missileBitmap, player.getBulletX() - bulletWidth / 2 - 25, player.getY() - bulletHeight / 2,
                            bulletWidth, bulletHeight, missileSpeed, "player");
                    bullets.addNewBullet(missileBitmap, player.getBulletX() - bulletWidth / 2 + 25, player.getY() - bulletHeight / 2,
                            bulletWidth, bulletHeight, missileSpeed, "player");
                }else{
                    bullets.addNewBullet(laserBitmap, player.getBulletX() - bulletWidth / 2 - 25, player.getY() - bulletHeight / 2,
                            bulletWidth, bulletHeight, laserSpeed, "player");
                    bullets.addNewBullet(laserBitmap, player.getBulletX() - bulletWidth / 2 + 25, player.getY() - bulletHeight / 2,
                            bulletWidth, bulletHeight, laserSpeed, "player");
                }

            }
        }, shootRate, shootRate);
    }

    private void scheduleNextEnemy(){
        nextEnemyTimer = new Timer();
        nextEnemyTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(currentEnemies < 6){
                    currentEnemies++;
                }
            }
        }, nextEnemyRate, nextEnemyRate);
    }

    private void checkCollision(Canvas canvas){
        for(Enemy enemy : enemies.getEnemies()){
            for (Bullet bullet : bullets.getBullets()){
                if(Rect.intersects(enemy.getRect(), bullet.getRect())){
                    enemyShot(enemy, bullet);
                }
            }
            if(Rect.intersects(enemy.getRect(), player.getRect())){
                lives--;
                enemies.removeEnemy(enemy);
                paint.setColor(Color.WHITE);
                explosions.add(new Explosion(explosion, enemy.getX(), enemy.getY()+20, coinBitmap, enemy.getCoins(), paint));
            }
            if(enemy.getY() >= MainActivity.getScreenHeight() && !enemy.isDead()){
                lives--;
                enemy.die();
            }
        }
    }

    private void enemyShot(Enemy enemy, Bullet bullet){
        if(enemy.decreaseHealth(player.getDamage()) <= 0){
            lastShot = System.currentTimeMillis();
            streak++;
            score += enemy.getPoints() + ( (streak - 1) * enemy.getPoints() );
            coins += enemy.getCoins();

            enemies.removeEnemy(enemy);
            bullets.removeBullet(bullet);

            paint.setColor(Color.WHITE);
            explosions.add(new Explosion(explosion, enemy.getX(), enemy.getY(), coinBitmap, enemy.getCoins(), paint));
        }else{
            bullets.removeBullet(bullet);
        }
    }

    private void fillExplosionBitmap(){
        explosion = new Bitmap[17];
        playerExplosionSmall = new Bitmap[17];
        playerExplosion = new Bitmap[17];

        for(int i = 0; i < 17; i++){
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                    getResources().getIdentifier("explosion"+(i+1), "drawable", getContext().getPackageName()));
            explosion[i] = Bitmap.createScaledBitmap(bitmap, enemyWidth, enemyHeight, false);
            playerExplosionSmall[i] = Bitmap.createScaledBitmap(bitmap, playerWidth / 2, playerHeight / 2, false);
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
        canvas.drawText(coins + "", 20 + coinWidth + 15, 25 + hearthHeight + 46, paint);
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

        editor.commit();
    }

    private void gameOver(){
        player.stopMoving();

        explosionSound.start();

        isGameOver = true;
        shotTimer.cancel();

        blowUpPlayer();

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

    private void blowUpPlayer(){

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                explosions.add(new Explosion(playerExplosionSmall, player.getX() + playerWidth/3, player.getY() + playerHeight/6));
            }
        }, 500);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                explosions.add(new Explosion(playerExplosionSmall, player.getX() + playerWidth/6, player.getY() + playerHeight/3));
            }
        }, 850);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                explosions.add(new Explosion(playerExplosionSmall, player.getX() + playerWidth/4, player.getY() + playerHeight/2));
            }
        }, 1200);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                explosions.add(new Explosion(playerExplosionSmall, player.getX() + playerWidth/3, player.getY() + playerHeight/3));
            }
        }, 1700);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                explosions.add(new Explosion(playerExplosion, player.getX(), player.getY()));
                player.moveAway();
            }
        }, 2000);
    }

    private int randomNumber(int min, int max){
        Random rn = new Random();
        int range = max - min + 1;
        int randomNum =  rn.nextInt(range) + min;

        return randomNum;
    }
}
