package com.example.mole_smasher_6000;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {

    int x=0,y=0;

    Bitmap background;

    Background(int screenX, int screenY, Resources res, int gM){
        if(gM==1){
            background= BitmapFactory.decodeResource(res,R.drawable.background2);
        }else {
            background= BitmapFactory.decodeResource(res,R.drawable.background1);
        }

        background= Bitmap.createScaledBitmap(background,screenX,screenY,false);
    }
}
