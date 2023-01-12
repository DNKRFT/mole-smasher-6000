package com.example.mole_smasher_6000;

import static com.example.mole_smasher_6000.GameView.screenRatioX;
import static com.example.mole_smasher_6000.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.Random;

public class Mole {

    int x,y, width, height;
    float myTimer=-0.1f;
    Bitmap mole,redMole,hole;
    long t0=System.nanoTime();
    int state;
    float displayTime1,displayTime2;
    boolean isDetermined=false;
    boolean displayState=false;
    int moleState=0, redMoleState=0;
    boolean stateSaved=false;
    float multiplier;


    Mole(int screenY,int screenX, Resources res, int difficulty){
        mole=BitmapFactory.decodeResource(res,R.drawable.mole);
        redMole=BitmapFactory.decodeResource(res,R.drawable.red_mole);
        hole=BitmapFactory.decodeResource(res,R.drawable.hole);

        width=mole.getWidth();
        height=mole.getHeight();

        width/=12;
        height/=12;

        width*=(int)screenRatioX;
        height*=(int)screenRatioY;

        mole= Bitmap.createScaledBitmap(mole,width,height,false);
        redMole= Bitmap.createScaledBitmap(redMole,width,height,false);
        hole= Bitmap.createScaledBitmap(hole,width,height,false);

        y=screenY;
        x=screenX-mole.getWidth()/2;

        //set mole display time divider related with difficulty

        if(difficulty==1){
            multiplier=1;
        }else if(difficulty==2){
            multiplier=2;
        }else{
            multiplier=3;
        }
    }


    Bitmap getMole(){

        //display hole and random type of mole alternately (mole, fake mole), set random mole display times

        myTimer+=0.1;
        if(myTimer>=displayTime1){
            if(displayState==false){
                myTimer+=myTimer;
            }
            displayState=true;

            if(myTimer>=displayTime2){
                myTimer=0;
                isDetermined=false;
            }

            if(state==0){
                if(!stateSaved){
                    moleState++;
                    stateSaved=true;
                }
                return  mole;
            }if(state==1){
                if(!stateSaved){
                    redMoleState++;
                    stateSaved=true;
                }
                return  redMole;
            }else{
                return hole;
            }

        }else{
            System.out.println(displayTime2);
            displayState=false;
            stateSaved=false;
            if(!isDetermined){
                state=new Random().nextInt(2);
                displayTime1=(new Random().nextInt(8)+4)/multiplier;
                displayTime2=(2*displayTime1+displayTime1*(new Random().nextInt(50)+50)/100);
                isDetermined=true;

            }

            return hole;
        }
    }
}
