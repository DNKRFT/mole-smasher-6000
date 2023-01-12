package com.example.mole_smasher_6000;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_over);

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        TextView reactionScore = (TextView)findViewById(R.id.reaction);
        TextView focusScore = (TextView)findViewById(R.id.focus);
        TextView overallScore = (TextView)findViewById(R.id.overall);
        TextView restartButton = (TextView)findViewById(R.id.restart);

        Bundle b = getIntent().getExtras();

        //show restart button in "standard mode"
        if(b.getInt("gameMode")==1){

            restartButton.setVisibility(View.VISIBLE);

        }

        float moleCount=b.getInt("moleCount");
        float redMoleCount=b.getInt("redMoleCount");
        float moleHits=b.getInt("moleHits");
        float redMoleHits=b.getInt("redMoleHits");

        float reactionScoreValue=(moleHits/moleCount)*100;
        float focusScoreValue=((redMoleCount-redMoleHits)/redMoleCount)*100;

        float averageFocus=0;
        float averageReaction=0;

        String indicator;
        int difficulty=b.getInt("difficulty");

        int notEmpty=0;

        //take into account existing values for calculations

        for (int i=0;i<5;i++){
            if(sharedPreferences.getFloat("f"+String.valueOf(difficulty)+String.valueOf(i),-1)!=-1){
                averageFocus+=sharedPreferences.getFloat("f"+String.valueOf(difficulty)+String.valueOf(i),0);
                averageReaction+=sharedPreferences.getFloat("r"+String.valueOf(difficulty)+String.valueOf(i),0);
                notEmpty++;
            }

        }
        averageFocus/=notEmpty;
        averageReaction/=notEmpty;

        //set indicator showing comparison of current score to average score

        if(averageReaction<reactionScoreValue){
            indicator="↑";
            reactionScore.setTextColor(Color.GREEN);

        }else if(averageReaction>reactionScoreValue){
            indicator="↓";
            reactionScore.setTextColor(Color.RED);
        }
        else{
            indicator="";
            reactionScore.setTextColor(Color.WHITE);
        }

        reactionScore.setText("REACTION: "+String.format("%.0f",reactionScoreValue)+" % "+indicator);

        if(averageFocus<focusScoreValue){
            indicator="↑";
            focusScore.setTextColor(Color.GREEN);
        }else if(averageFocus>focusScoreValue){
            indicator="↓";
            focusScore.setTextColor(Color.RED);
        }
        else{
            indicator="";
            focusScore.setTextColor(Color.WHITE);
        }

        focusScore.setText("FOCUS: "+String.format("%.0f",focusScoreValue)+" % "+indicator);

        if((averageFocus+averageReaction)/2<(focusScoreValue+reactionScoreValue)/2){
            indicator="↑";
            overallScore.setTextColor(Color.GREEN);
        }else if((averageFocus+averageReaction)/2>(focusScoreValue+reactionScoreValue)/2){
            indicator="↓";
            overallScore.setTextColor(Color.RED);
        }
        else{
            indicator="";
            overallScore.setTextColor(Color.WHITE);
        }

        overallScore.setText("OVERALL: "+String.format("%.0f",(focusScoreValue+reactionScoreValue)/2)+" % "+indicator);

        //save score in "serious mode"

        if(b.getInt("gameMode")==2){
            for (int i=0;i<4;i++){
                myEdit.putFloat("f"+String.valueOf(difficulty)+String.valueOf(i), sharedPreferences.getFloat("f"+String.valueOf(difficulty)+String.valueOf(i+1),-1));
                myEdit.putFloat("r"+String.valueOf(difficulty)+String.valueOf(i), sharedPreferences.getFloat("r"+String.valueOf(difficulty)+String.valueOf(i+1),-1));

            }

            myEdit.putFloat("f"+String.valueOf(difficulty)+"4", focusScoreValue);
            myEdit.putFloat("r"+String.valueOf(difficulty)+"4", reactionScoreValue);

            myEdit.commit();
        }

        findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GameOverActivity.this,  MainActivity.class));
                finish();
            }
        });

        findViewById(R.id.restart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameOverActivity.this,  GameActivity.class);
                b.putInt("gameMode", 1);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });
    }
}