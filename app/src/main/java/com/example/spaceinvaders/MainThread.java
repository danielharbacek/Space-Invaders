package com.example.spaceinvaders;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {

    private SurfaceHolder surfaceHolder;
    private SpaceInvaders spaceInvaders;
    private boolean running;
    public static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, SpaceInvaders spaceInvaders){
        super();

        this.spaceInvaders = spaceInvaders;
        this.surfaceHolder = surfaceHolder;
    }

    @Override
    public void run() {
        while(running){
            canvas = null;

            try{
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    this.spaceInvaders.update();
                    this.spaceInvaders.draw(canvas);
                }
            }catch(Exception e){
            }finally {
                if(canvas != null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
