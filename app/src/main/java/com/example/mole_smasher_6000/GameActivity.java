package com.example.mole_smasher_6000;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class GameActivity extends AppCompatActivity {
    private GameView gameView;
    public int gameMode;
    public int difficulty;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle b = getIntent().getExtras();
        gameMode=b.getInt("gameMode");
        difficulty=b.getInt("difficulty");
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        gameView=new GameView(this,point.x,point.y);

        setContentView(gameView);
        if(gameMode==1){
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.standard);
        }else{
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.serious);
        }

        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}