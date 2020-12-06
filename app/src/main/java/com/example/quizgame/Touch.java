package com.example.quizgame;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.quizgame.Handler.GameStateHandler;

public class Touch extends AppCompatActivity {
    ImageView[] mEasy = new ImageView[4];
    ImageView[] mMedium = new ImageView[4];
    ImageView[] mHard = new ImageView[4];
    ImageView[] mHardcore = new ImageView[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);
        Context context = getApplicationContext();
        Resources resources = getResources();

        for (int i = 0; i < 4; i++)
        {
            final int resourceId = resources.getIdentifier("touch"+(i+1)+"_1", "id", context.getPackageName());
            final int index = i+1;
            mEasy[i] = findViewById(resourceId);
            mEasy[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    passQuizTypeAndDifficulty("touch"+(index)+"_1");
                }
            });
        }
        for (int i = 0; i < 4; i++)
        {
            final int resourceId = resources.getIdentifier("touch"+(i+1)+"_2", "id", context.getPackageName());
            final int index = i+1;
            mMedium[i] = findViewById(resourceId);
            mMedium[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    passQuizTypeAndDifficulty("touch"+(index)+"_2");
                }
            });
        }
        for (int i = 0; i < 4; i++)
        {
            final int resourceId = resources.getIdentifier("touch"+(i+1)+"_3", "id", context.getPackageName());
            final int index = i+1;
            mHard[i] = findViewById(resourceId);
            mHard[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    passQuizTypeAndDifficulty("touch"+(index)+"_3");
                }
            });
        }
        for (int i = 0; i < 4; i++)
        {
            final int resourceId = resources.getIdentifier("touch"+(i+1)+"_4", "id", context.getPackageName());
            final int index = i+1;
            mHardcore[i] = findViewById(resourceId);
            mHardcore[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    passQuizTypeAndDifficulty("touch"+(index)+"_4");
                }
            });
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onResume(){
        super.onResume();
        for (int i = 0; i < 4; i++)
        {

            String Key = "touch"+(i+1)+"_1";
            if(!GameStateHandler.getInstance().isKeyExist(Key)){
                GameStateHandler.getInstance().setGameStete(Key,"unlocked_0");
                mEasy[i].setImageResource(R.drawable.level_playing);
            }else
            {
                String [] gameStateStar = GameStateHandler.getInstance().getGameStete(Key).split("_");
                if (gameStateStar[0].equals("unlocked") && !gameStateStar[1].equals("0")){
                    int nStar = Integer.parseInt(gameStateStar[1]);
                    if (nStar > 2)
                        mEasy[i].setImageResource(R.drawable.level_sucess);
                    else if (nStar == 1)
                        mEasy[i].setImageResource(R.drawable.level_sucess_1);
                    else
                        mEasy[i].setImageResource(R.drawable.level_sucess_2);
                }else
                {
                    mEasy[i].setImageResource(R.drawable.level_playing);
                }
            }
        }
        for (int i = 0; i < 4; i++)
        {
            String Key = "touch"+(i+1)+"_2";
            checkGameState(Key,mMedium[i]);
        }
        for (int i = 0; i < 4; i++)
        {
            String Key = "touch"+(i+1)+"_3";
            checkGameState(Key,mHard[i]);
        }
        for (int i = 0; i < 4; i++)
        {
            String Key = "touch"+(i+1)+"_4";
            checkGameState(Key,mHardcore[i]);
        }
    }
    public void passQuizTypeAndDifficulty(String quizTypeAndDiff){
        Intent i;
        if(quizTypeAndDiff.contains("touch1")){
            i = new Intent(this, Game1Activity.class);
        }else if(quizTypeAndDiff.contains("touch2")){
            i = new Intent(this, Game2Activity.class);
        }else if(quizTypeAndDiff.contains("touch3"))
        {
            i = new Intent(this, Game3Activity.class);
        }
        else if(quizTypeAndDiff.contains("touch4")) {
            i = new Intent(this, Game4Activity.class);
        }else
        {
            i = new Intent(this, TouchAndTapGame.class);
        }
        i.putExtra("quizTypeAndDiff",quizTypeAndDiff);
        startActivity(i);
    }
    public void checkGameState(String Key,ImageView Btn){
        if(!GameStateHandler.getInstance().isKeyExist(Key)){
            GameStateHandler.getInstance().setGameStete(Key,"locked_0");
            Btn.setClickable(false);
            Btn.setImageResource(R.drawable.level_lock);
        }else
        {
            String [] gameStateStar = GameStateHandler.getInstance().getGameStete(Key).split("_");
            if(gameStateStar[0].equals("locked")){
                Btn.setClickable(false);
                Btn.setImageResource(R.drawable.level_lock);
            }else if (gameStateStar[0].equals("unlocked") && gameStateStar[1].equals("0")){
                Btn.setImageResource(R.drawable.level_playing);
                Btn.setClickable(true);
            }else
            {
                int nStar = Integer.parseInt(gameStateStar[1]);
                if (nStar > 2)
                    Btn.setImageResource(R.drawable.level_sucess);
                else if (nStar == 1)
                    Btn.setImageResource(R.drawable.level_sucess_1);
                else
                    Btn.setImageResource(R.drawable.level_sucess_2);
                Btn.setClickable(true);
            }
        }
    }
}
