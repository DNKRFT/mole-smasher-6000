package com.example.mole_smasher_6000;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    SharedPreferences sharedPreferences;

    int difficultyLevel;
    long timeLeft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Bundle b = new Bundle();
        TextView difficultyButton = (TextView)findViewById(R.id.difficulty);
        TextView seriousModeButton = (TextView)findViewById(R.id.play2);

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        /*sharedPreferences.edit().clear().commit();*/
        difficultyLevel=sharedPreferences.getInt("difficulty",1);

        if (difficultyLevel == 1) {
            difficultyButton.setText("EASY");
        }else if (difficultyLevel==2){
            difficultyButton.setText("MEDIUM");
        }else{
            difficultyButton.setText("HARD");
        }

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,  GameActivity.class);
                b.putInt("gameMode", 1);
                b.putInt("difficulty", difficultyLevel);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.play2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //player has to wait one minute before being able to play serious mode again
                if((System.nanoTime()-sharedPreferences.getLong("time",1))/1000000000>120){
                    myEdit.putLong("time", System.nanoTime());
                    myEdit.commit();
                    Intent intent = new Intent(MainActivity.this,  GameActivity.class);
                    b.putInt("gameMode", 2);
                    b.putInt("difficulty", difficultyLevel);
                    intent.putExtras(b);
                    startActivity(intent);
                    finish();
                }else{
                    timeLeft=120000000000L-(System.nanoTime()-sharedPreferences.getLong("time",1));
                    seriousModeButton.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                    seriousModeButton.setText("PLAY SERIOUS MODE "+System.getProperty("line.separator")+"(WAIT: "+String.format("%.0f:%.0f",(float)(int)(((timeLeft/1000000000) % 3600) / 60),(float)(int)((timeLeft/1000000000)%60))+")");
                }
            }
        });

        findViewById(R.id.difficulty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(difficultyLevel<3){
                    difficultyLevel++;
                }else{
                    difficultyLevel=1;
                }

                myEdit.putInt("difficulty", difficultyLevel);
                myEdit.commit();

                if (difficultyLevel == 1) {
                    difficultyButton.setText("EASY");
                }else if (difficultyLevel==2){
                    difficultyButton.setText("MEDIUM");
                }else{
                    difficultyButton.setText("HARD");
                }
            }
        });

        findViewById(R.id.score).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,  UserScore.class);
                b.putInt("difficulty", difficultyLevel);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });

    }
}

