package com.example.mole_smasher_6000;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class UserScore extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_score);

        TextView[] score = new TextView[15];

        score[0]=(TextView)findViewById(R.id.focus1);
        score[1]=(TextView)findViewById(R.id.focus2);
        score[2]=(TextView)findViewById(R.id.focus3);
        score[3]=(TextView)findViewById(R.id.focus4);
        score[4]=(TextView)findViewById(R.id.focus5);

        score[5]=(TextView)findViewById(R.id.reaction1);
        score[6]=(TextView)findViewById(R.id.reaction2);
        score[7]=(TextView)findViewById(R.id.reaction3);
        score[8]=(TextView)findViewById(R.id.reaction4);
        score[9]=(TextView)findViewById(R.id.reaction5);

        score[10]=(TextView)findViewById(R.id.overall1);
        score[11]=(TextView)findViewById(R.id.overall2);
        score[12]=(TextView)findViewById(R.id.overall3);
        score[13]=(TextView)findViewById(R.id.overall4);
        score[14]=(TextView)findViewById(R.id.overall5);

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        Bundle b = getIntent().getExtras();

        float overall;

        int difficulty=b.getInt("difficulty");

        //load score values, show "-" if value does not exist

        for (int i=0;i<5;i++){
            if(sharedPreferences.getFloat("f"+String.valueOf(difficulty)+String.valueOf(i),-1)==-1){
                score[i].setText("-");
            }else{
                score[i].setText(String.format("%.0f",sharedPreferences.getFloat("f"+String.valueOf(difficulty)+String.valueOf(i),0)));
            }
        }

        for (int i=5;i<10;i++){
            if(sharedPreferences.getFloat("r"+String.valueOf(difficulty)+String.valueOf(i-5),-1)==-1){
                score[i].setText("-");
            }else{
                score[i].setText(String.format("%.0f",sharedPreferences.getFloat("r"+String.valueOf(difficulty)+String.valueOf(i-5),0)));
            }
        }

        for (int i=10;i<15;i++){
            if(sharedPreferences.getFloat("r"+String.valueOf(difficulty)+String.valueOf(i-10),-1)==-1 || sharedPreferences.getFloat("f"+String.valueOf(difficulty)+String.valueOf(i-10),0)==-1){
                score[i].setText("-");
            }else{
                overall=(sharedPreferences.getFloat("r"+String.valueOf(difficulty)+String.valueOf(i-10),0)+sharedPreferences.getFloat("f"+String.valueOf(difficulty)+String.valueOf(i-10),0))/2;
                score[i].setText(String.format("%.0f",overall));
            }
        }

        findViewById(R.id.close_summary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserScore.this,  MainActivity.class));
                finish();
            }
        });

    }
}