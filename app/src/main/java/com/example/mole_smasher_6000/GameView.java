package com.example.mole_smasher_6000;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.Random;

public class GameView extends SurfaceView implements Runnable{
    private Thread thread;
    private boolean isPlaying;
    private int screenX, screenY;
    public static float screenRatioX, screenRatioY;
    private Background background1, background2;
    private Paint paint;
    private  Mole mole1,mole2,mole3,mole4,mole5,mole6;
    private float time;
    private GameActivity gameActivity;
    private int moleHits, redMoleHits, moleCount, redMoleCount;

    public GameView(GameActivity gameActivity, int screenX, int screenY) {
        super(gameActivity);

        this.gameActivity = gameActivity;

        this.screenX=screenX;
        this.screenY=screenY;

        screenRatioX=1080f/screenX;
        screenRatioY=1920f/screenY;

        background1=new Background(screenX,screenY,getResources(), gameActivity.gameMode);
        background2=new Background(screenX,screenY,getResources(), gameActivity.gameMode);

        mole1=new Mole(screenY/2, (int) (screenX*0.2),getResources(), gameActivity.difficulty);
        mole2=new Mole(screenY/2,(int) (screenX*0.5),getResources(), gameActivity.difficulty);
        mole3=new Mole(screenY/2,(int) (screenX*0.8),getResources(), gameActivity.difficulty);
        mole4=new Mole((int) (screenY/1.35),(int) (screenX*0.2),getResources(), gameActivity.difficulty);
        mole5=new Mole((int) (screenY/1.35),(int) (screenX*0.5),getResources(), gameActivity.difficulty);
        mole6=new Mole((int) (screenY/1.35),(int) (screenX*0.8),getResources(), gameActivity.difficulty);

        background2.x=screenX;

        paint=new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(100);

        time=System.nanoTime();
    }

    @Override
    public void run() {
        while(isPlaying){
            update();
            draw();
            sleep();
        }
    }

    private void update(){
        background1.x-=10*screenRatioX;
        background2.x-=10*screenRatioX;

        if(background1.x+background1.background.getWidth()<0){
            background1.x=screenX;
        }

        if(background2.x+background2.background.getWidth()<0){
            background2.x=screenX;
        }

        moleCount=mole1.moleState+mole2.moleState+mole3.moleState+mole4.moleState+mole5.moleState+mole6.moleState;
        redMoleCount=mole1.redMoleState+mole2.redMoleState+mole3.redMoleState+mole4.redMoleState+mole5.redMoleState+mole6.redMoleState;

        if((System.nanoTime()-time)/1000000000>90){
            gameActivity.mediaPlayer.stop();
            isPlaying=false;
            Intent intent = new Intent(gameActivity, GameOverActivity.class);
            Bundle b = new Bundle();

            b.putInt("moleCount", moleCount);
            b.putInt("redMoleCount", redMoleCount);
            b.putInt("moleHits", moleHits);
            b.putInt("redMoleHits", redMoleHits);
            b.putInt("gameMode", gameActivity.gameMode);
            b.putInt("difficulty", gameActivity.difficulty);

            intent.putExtras(b);
            gameActivity.startActivity(intent);
            gameActivity.finish();
        }
    }

    private void draw(){
        if(getHolder().getSurface().isValid()){
            Canvas canvas   =getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x,background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x,background2.y, paint);

            canvas.drawBitmap(mole1.getMole(),mole1.x,mole1.y,paint);
            canvas.drawBitmap(mole2.getMole(),mole2.x,mole2.y,paint);
            canvas.drawBitmap(mole3.getMole(),mole3.x,mole3.y,paint);
            canvas.drawBitmap(mole4.getMole(),mole4.x,mole4.y,paint);
            canvas.drawBitmap(mole5.getMole(),mole5.x,mole5.y,paint);
            canvas.drawBitmap(mole6.getMole(),mole6.x,mole6.y,paint);

            //mole and fake mole hit counter (can be helpful)
            //canvas.drawText(String.valueOf(moleHits)+"|"+String.valueOf(redMoleHits),screenX/2,screenY/3,paint);

            canvas.drawText("X",screenX/9,screenY/10,paint);

            canvas.drawText(String.format("%.0f:%.0f",(float)(int)((((System.nanoTime()-time)/1000000000) % 3600) / 60),(float)(int)(((System.nanoTime()-time)/1000000000)%60)),screenX/2,screenY/10,paint);

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void sleep(){
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume(){
        isPlaying=true;
        thread=new Thread(this);
        thread.start();
    }

    public void pause(){
        try {
            isPlaying=false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean onTouchEvent(MotionEvent event){

        float x=event.getX();
        float y = event.getY();

        if(y<=screenY/8 && y>=screenY/18){
            if(x<=screenX/6 && x>=screenX/17){
                gameActivity.mediaPlayer.stop();
                gameActivity.startActivity(new Intent(gameActivity,  MainActivity.class));
                gameActivity.finish();
            }
        }

        //check which mole has been hit

        if(y<=mole1.y+mole1.height && y>=mole1.y){
            if(x<=mole1.x+mole1.width && x>=mole1.x){
                if(mole1.displayState){
                    if(mole1.state==0){
                        moleHits++;
                    }else if(mole1.state==1){
                        redMoleHits++;
                    }
                    mole1.state=2;
                    mole1.myTimer=0;
                    mole1.isDetermined=false;
                }
            }
            if(x<=mole2.x+mole2.width && x>=mole2.x){
                if(mole2.displayState){
                    if(mole2.state==0){
                        moleHits++;
                    }else if(mole2.state==1){
                        redMoleHits++;
                    }
                    mole2.state=2;
                    mole2.myTimer=0;
                    mole2.isDetermined=false;
                }
            }if(x<=mole3.x+mole3.width && x>=mole3.x){
                if(mole3.displayState){
                    if(mole3.state==0){
                        moleHits++;
                    }else if(mole3.state==1){
                        redMoleHits++;
                    }
                    mole3.state=2;
                    mole3.myTimer=0;
                    mole3.isDetermined=false;
                }
            }
        }if(y<=mole4.y+mole4.height && y>=mole4.y){
            if(x<=mole4.x+mole4.width && x>=mole4.x){

                if(mole4.displayState){
                    if(mole4.state==0){
                        moleHits++;
                    }else if(mole4.state==1){
                        redMoleHits++;
                    }
                    mole4.state=2;
                    mole4.myTimer=0;
                    mole4.isDetermined=false;
                }
            }
            if(x<=mole5.x+mole5.width && x>=mole5.x){
                if(mole5.displayState){
                    if(mole5.state==0){
                        moleHits++;
                    }else if(mole5.state==1){
                        redMoleHits++;
                    }
                    mole5.state=2;
                    mole5.myTimer=0;
                    mole5.isDetermined=false;
                }
            }if(x<=mole6.x+mole6.width && x>=mole6.x){
                if(mole6.displayState){
                    if(mole6.state==0){
                        moleHits++;
                    }else if(mole6.state==1){
                        redMoleHits++;
                    }
                    mole6.state=2;
                    mole6.myTimer=0;
                    mole6.isDetermined=false;
                }
            }
        }
        return true;
    }
}
