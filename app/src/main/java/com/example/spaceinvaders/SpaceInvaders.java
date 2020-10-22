package com.example.spaceinvaders;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class SpaceInvaders extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder;
    private MainThread mainThread;

    private Invaders invaders;

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

        invaders = new Invaders(100, 100, 100);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        mainThread.setRunning(true);
        mainThread.start();

        invaders.addNewInvader(BitmapFactory.decodeResource(getResources(), R.drawable.invader1), 100, 100);
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
    }

    public void update(){
        invaders.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if(canvas != null){
            invaders.draw(canvas);
        }
    }
}
