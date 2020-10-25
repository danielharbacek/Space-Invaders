package com.example.spaceinvaders;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UpgradeActivity extends AppCompatActivity {

    SharedPreferences sharedprefs;
    public static final String PrefsName = "MyPrefs";
    private int coins;

    private static final String[] shipNames = {
            "Battlestar Galactica",
            "USS Enterprise",
            "Millennium Falcon",
            "The Resolute",
            "Serenity"};

    private TextView coinsText;
    private TextView livesText;
    private TextView damageText;
    private TextView gunText;
    private TextView speedText;
    
    private TextView shipCost;
    private TextView livesCost;
    private TextView damageCost;
    private TextView gunCost;
    private TextView speedCost;
    
    private TextView shipName;

    private ImageView shipImage;
    private ImageView plusLives;
    private ImageView plusDamage;
    private ImageView plusGun;
    private ImageView plusSpeed;

    private ImageView shipCoin;
    private ImageView livesCoin;
    private ImageView damageCoin;
    private ImageView gunCoin;
    private ImageView speedCoin;

    private ImageView checkmark;

    private Button buyButton;

    private Bitmap[] ships;
    private int currentShip = 0;

    private int[] shipsCosts;
    private int[][] livesCosts;
    private int[][] damageCosts;
    private int[][] gunCosts;
    private int[][] speedCosts;

    private boolean currentShips[];
    private int currentLives[];
    private int currentDamage[];
    private int currentGun[];
    private int currentSpeed[];

    private int shipsCount;
    private int upgradesCount;

    MediaPlayer upgradeSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        sharedprefs = getSharedPreferences(PrefsName, 0);

        upgradeSound = MediaPlayer.create(getApplicationContext(), R.raw.drill1);

        shipsCount = 5;
        upgradesCount = 4;

        findViews();
        initCosts();
        loadBitmaps();
        loadUpgrades();
        updateCosts();
        updateStats();
        checkMaxUpgrade();

        coins = sharedprefs.getInt("coins", 0);
        coinsText.setText(coins + "");

        shipName.setText(shipNames[currentShip]);
    }

    private void initCosts(){
        shipsCosts = new int[shipsCount];
        livesCosts = new int[shipsCount][upgradesCount];
        damageCosts = new int[shipsCount][upgradesCount];
        gunCosts = new int[shipsCount][upgradesCount];
        speedCosts = new int[shipsCount][upgradesCount];

        for(int i = 0; i < shipsCount; i++){
            shipsCosts[i] = 2000 * i;
            for(int j = 0; j < upgradesCount; j++){
                livesCosts[i][j] = 200 + 150 * i + (j+1) * 250;
                damageCosts[i][j] = 150 + 100 * i + (j+1) * 200;
                gunCosts[i][j] = 300 + 100 * i + (j+1) * 150;
                speedCosts[i][j] = 100 + 50 * i + (j+1) * 50;
            }

        }
    }

    private void findViews(){
        coinsText = findViewById(R.id.coinsText);
        livesText = findViewById(R.id.livesText);
        damageText = findViewById(R.id.damageText);
        gunText = findViewById(R.id.gunText);
        speedText = findViewById(R.id.speedText);

        shipCost = findViewById(R.id.shipCost);
        livesCost = findViewById(R.id.livesCost);
        damageCost = findViewById(R.id.damageCost);
        gunCost = findViewById(R.id.gunCost);
        speedCost = findViewById(R.id.speedCost);

        shipName = findViewById(R.id.shipName);

        shipImage = findViewById(R.id.shipImage);
        plusLives = findViewById(R.id.plusLives);
        plusDamage = findViewById(R.id.plusDamage);
        plusGun = findViewById(R.id.plusGun);
        plusSpeed = findViewById(R.id.plusSpeed);

        shipCoin = findViewById(R.id.shipCoin);
        livesCoin = findViewById(R.id.livesCoin);
        damageCoin = findViewById(R.id.damageCoin);
        gunCoin = findViewById(R.id.gunCoin);
        speedCoin = findViewById(R.id.speedCoin);

        checkmark = findViewById(R.id.checkmark);

        buyButton = findViewById(R.id.buyButton);
    }

    private void loadBitmaps(){
        ships = new Bitmap[shipsCount];

        for(int i = 0; i < shipsCount; i++){
            ships[i] = BitmapFactory.decodeResource(getResources(),
                    getResources().getIdentifier("ship"+(i+1), "drawable", getPackageName()));
        }
    }

    private void loadUpgrades(){
        currentLives = new int[shipsCount];
        currentDamage = new int[shipsCount];
        currentGun = new int[shipsCount];
        currentSpeed = new int[shipsCount];
        currentShips = new boolean[shipsCount];

        for(int i = 0; i < shipsCount; i++){
            currentLives[i] = sharedprefs.getInt("ship" + i + "currentlives", 0);
            currentDamage[i] = sharedprefs.getInt("ship" + i + "currentdamage", 0);
            currentGun[i] = sharedprefs.getInt("ship" + i + "currentgun", 0);
            currentSpeed[i] = sharedprefs.getInt("ship" + i + "currentspeed", 0);
            currentShips[i] = sharedprefs.getBoolean("ship" + i, false);
        }
    }

    private void updateCosts(){
        shipCost.setText(shipsCosts[currentShip] + "");
        livesCost.setText(livesCosts[currentShip][currentLives[currentShip]] + "");
        damageCost.setText(damageCosts[currentShip][currentDamage[currentShip]] + "");
        gunCost.setText(gunCosts[currentShip][currentGun[currentShip]] + "");
        speedCost.setText(speedCosts[currentShip][currentSpeed[currentShip]] + "");
    }

    private void updateStats(){
        int lives = currentLives[currentShip] + 2 + (currentShip / 2);
        livesText.setText("Lives: " + lives);

        int damage = currentDamage[currentShip] * 50 + 100 + (currentShip+1) * 25 + currentDamage[currentShip]*10*currentShip;
        damageText.setText("Damage: " + damage);

        String text = "Gun: ";
        if(currentGun[currentShip] == 0){
            text += "missile";
        }else if(currentGun[currentShip] == 1){
            text += "laser";
        }else if(currentGun[currentShip] == 2){
            text += "double missile";
        }else if(currentGun[currentShip] == 3){
            text += "double laser";
        }
        gunText.setText(text);

        int speed = currentSpeed[currentShip] + 9;
        speedText.setText("Speed: " + speed);
    }

    private void viewLives(int i){
        plusLives.setVisibility(i);
        livesCoin.setVisibility(i);
        livesCost.setVisibility(i);
    }

    private void viewDamage(int i){
        plusDamage.setVisibility(i);
        damageCoin.setVisibility(i);
        damageCost.setVisibility(i);

    }

    private void viewGun(int i){
        plusGun.setVisibility(i);
        gunCoin.setVisibility(i);
        gunCost.setVisibility(i);
    }

    private void viewSpeed(int i){
        plusSpeed.setVisibility(i);
        speedCoin.setVisibility(i);
        speedCost.setVisibility(i);
    }

    private void viewShip(int i){
        if(i == View.VISIBLE){
            shipCoin.setVisibility(i);
            shipCost.setVisibility(i);
            checkmark.setVisibility(View.INVISIBLE);
        }else{
            shipCoin.setVisibility(i);
            shipCost.setVisibility(i);
            checkmark.setVisibility(View.VISIBLE);
        }
    }

    private void checkMaxUpgrade(){
        if(currentLives[currentShip] == upgradesCount - 1){
            viewLives(View.INVISIBLE);
        }else{
            viewLives(View.VISIBLE);
        }

        if(currentDamage[currentShip] == upgradesCount - 1){
            viewDamage(View.INVISIBLE);
        }else{
            viewDamage(View.VISIBLE);
        }

        if(currentGun[currentShip] == upgradesCount - 1){
            viewGun(View.INVISIBLE);
        }else{
            viewGun(View.VISIBLE);
        }

        if(currentSpeed[currentShip] == upgradesCount - 1){
            viewSpeed(View.INVISIBLE);
        }else{
            viewSpeed(View.VISIBLE);
        }

        if(currentShips[currentShip]){
            viewShip(View.INVISIBLE);
        }else{
            viewShip(View.VISIBLE);
        }
    }

    private void updateCoins(int cost){
        coins -= cost;

        SharedPreferences.Editor editor = sharedprefs.edit();
        editor.putInt("coins", coins);
        editor.commit();

        coinsText.setText(coins + "");
    }

    public void arrowRight(View view) {
        currentShip = ++currentShip % ships.length;
        shipImage.setImageBitmap(ships[currentShip]);

        updateCosts();
        updateStats();
        checkMaxUpgrade();
        
        shipName.setText(shipNames[currentShip]);
    }

    public void arrowLeft(View view) {
        if(currentShip == 0){
            currentShip = ships.length-1;
            shipImage.setImageBitmap(ships[currentShip]);
        }else{
            shipImage.setImageBitmap(ships[--currentShip]);
        }

        updateCosts();
        updateStats();
        checkMaxUpgrade();

        shipName.setText(shipNames[currentShip]);
    }

    public void buyShip(View view) {
        int cost = shipsCosts[currentShip];
        if(coins < cost){
            Toast.makeText(this, "You don't have enough coins.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(currentShips[currentShip]){
            Toast.makeText(this, "You already own this ship.", Toast.LENGTH_SHORT).show();
            return;
        }

        viewShip(View.INVISIBLE);
        updateCoins(cost);

        currentShips[currentShip] = true;

        SharedPreferences.Editor editor = sharedprefs.edit();
        editor.putBoolean("ship" + currentShip, true);
        editor.commit();

        upgradeSound.start();
    }

    public void back(View view) {
        Intent intent = new Intent(UpgradeActivity.this, MenuActivity.class);
        intent.putExtra("theme", false);
        startActivity(intent);
    }

    public void addSpeed(View view) {
        int cost = speedCosts[currentShip][currentSpeed[currentShip]];
        if(coins < cost){
            Toast.makeText(this, "You don't have enough coins.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!currentShips[currentShip]){
            Toast.makeText(this, "You don't own this ship.", Toast.LENGTH_SHORT).show();
            return;
        }

        currentSpeed[currentShip]++;
        if(currentSpeed[currentShip] == upgradesCount - 1){
            viewSpeed(View.INVISIBLE);
        }

        SharedPreferences.Editor editor = sharedprefs.edit();
        editor.putInt("ship" + currentShip + "currentspeed", currentSpeed[currentShip]);
        editor.commit();

        updateCosts();
        updateStats();
        updateCoins(cost);

        upgradeSound.start();
    }

    public void addGun(View view) {
        int cost = gunCosts[currentShip][currentGun[currentShip]];
        if(coins < cost){
            Toast.makeText(this, "You don't have enough coins.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!currentShips[currentShip]){
            Toast.makeText(this, "You don't own this ship.", Toast.LENGTH_SHORT).show();
            return;
        }

        currentGun[currentShip]++;
        if(currentGun[currentShip] == upgradesCount - 1){
            viewGun(View.INVISIBLE);
        }

        SharedPreferences.Editor editor = sharedprefs.edit();
        editor.putInt("ship" + currentShip + "currentgun", currentGun[currentShip]);
        editor.commit();

        updateCosts();
        updateStats();
        updateCoins(cost);

        upgradeSound.start();
    }

    public void addDamage(View view) {
        int cost = damageCosts[currentShip][currentDamage[currentShip]];
        if(coins < cost){
            Toast.makeText(this, "You don't have enough coins.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!currentShips[currentShip]){
            Toast.makeText(this, "You don't own this ship.", Toast.LENGTH_SHORT).show();
            return;
        }

        currentDamage[currentShip]++;
        if(currentDamage[currentShip] == upgradesCount - 1){
            viewDamage(View.INVISIBLE);
        }

        SharedPreferences.Editor editor = sharedprefs.edit();
        editor.putInt("ship" + currentShip + "currentdamage", currentDamage[currentShip]);
        editor.commit();

        updateCosts();
        updateStats();
        updateCoins(cost);

        upgradeSound.start();
    }

    public void addLives(View view) {
        int cost = livesCosts[currentShip][currentLives[currentShip]];
        if(coins < cost){
            Toast.makeText(this, "You don't have enough coins.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!currentShips[currentShip]){
            Toast.makeText(this, "You don't own this ship.", Toast.LENGTH_SHORT).show();
            return;
        }

        currentLives[currentShip]++;
        if(currentLives[currentShip] == upgradesCount - 1){
            viewLives(View.INVISIBLE);
        }

        SharedPreferences.Editor editor = sharedprefs.edit();
        editor.putInt("ship" + currentShip + "currentlives", currentLives[currentShip]);
        editor.commit();

        updateCosts();
        updateStats();
        updateCoins(cost);

        upgradeSound.start();
    }
}