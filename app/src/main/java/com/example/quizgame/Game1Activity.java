package com.example.quizgame;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.quizgame.Handler.GameStateHandler;
import com.example.quizgame.datamodels.Quiz;
import com.example.quizgame.datamodels.Quizes;
import com.example.quizgame.model.TouchGesture;
import com.example.quizgame.walk.BaseTouchUploadActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class Game1Activity extends BaseTouchUploadActivity {
    LinearLayout mHowToPlay,mLLayout,mLinearLayout;
    TextView mTvCount,mTvQuestion,mResult,mMassage,mTvHowtoSolve,mTvInstructions;;
    int mStars,mCurrQuestion = 0,mCorrectAns = 0,mWrongAns = 0,mTimetoAns=0,mTotal = 0;
    Quiz mQuiz;
    CountDownTimer mCdt;
    boolean mStart,mReturn,mTimeUp;
    ProgressBar mBar;
    Button mOK,mBack;
    String mQuizType = "",mQuizDifficulty = "";

    long firstTimestamp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game1);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        String [] quizTypeAndDiff = intent.getStringExtra("quizTypeAndDiff").split("_");
        mQuizType = quizTypeAndDiff[0];
        mQuizDifficulty = quizTypeAndDiff[1];
        String json = getAssetJsonData(this);
        Gson gson = new Gson();
        Quizes quizes = gson.fromJson(json,Quizes.class);
        mBack = (Button) findViewById(R.id.backbtn);
        mMassage = findViewById(R.id.massage);
        mTvInstructions = findViewById(R.id.waytosolve);
        mTvHowtoSolve =findViewById(R.id.howtosolve);
        mTvCount = findViewById(R.id.tv1);
        mTvQuestion = findViewById(R.id.tvquestion);
        mLinearLayout = findViewById(R.id.root);
        mLLayout = findViewById(R.id.fm);
        mHowToPlay = findViewById(R.id.howtoans);
        mResult = findViewById(R.id.result);
        mTvInstructions.setText(R.string.tutorialtouch1);
        mBar = (ProgressBar) findViewById(R.id.progressbar);
        mBar.setProgress(100);
        for(int i = 0;i< quizes.getQuizes().size();i++){
            mQuiz = quizes.getQuiz(i);
            String quizType = mQuiz.getQuizType();
            String quizDifficulty = mQuiz.getQuizDifficulty();
            if(quizType.equalsIgnoreCase(mQuizType) && quizDifficulty.equalsIgnoreCase(mQuizDifficulty)){
                break;
            }
        }
        mOK = (Button) findViewById(R.id.okbtn);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mQuiz != null) {
                    setTextViews();
                    mHowToPlay.setVisibility(View.GONE);
                    mStart = true;
                    mTimetoAns = mQuiz.getTime();
                    startCounter();
                    //fragGesture.toggle_switch(true);
                }
            }
        });
        mLinearLayout.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                if(mStart) {
                    if (!Boolean.parseBoolean(mQuiz.getQuestions().get(mCurrQuestion).getAnswer()) && !mTimeUp) {
                        mCorrectAns = mCorrectAns + 1;
                    } else {
                        mWrongAns = mWrongAns + 1;
                        vibrate(150);
                    }
                    showNextQuestion();
                }
            }
            public void onSwipeRight() {
                if(mStart) {
                    if (Boolean.parseBoolean(mQuiz.getQuestions().get(mCurrQuestion).getAnswer()) && !mTimeUp) {
                        mCorrectAns = mCorrectAns + 1;
                    } else {
                        mWrongAns = mWrongAns + 1;
                        vibrate(150);
                    }
                    showNextQuestion();
                }
            }

            @Override
            public void OnDown(float x, float y, float p){
                TouchGesture newGesture = new TouchGesture();
                newGesture.x = x;
                newGesture.y = y;
                if (arrTouchGesture.size() < 1)
                {
                    firstTimestamp = System.currentTimeMillis();
                    newGesture.t = 0;
                } else {
                    newGesture.t = System.currentTimeMillis() - firstTimestamp;
                }

                newGesture.p = p;
                newGesture.action = "Action_Down";
                arrTouchGesture.add(newGesture);
            }
            @Override
            public void OnUp(float x, float y, float p){
                TouchGesture newGesture = new TouchGesture();
                newGesture.x = x;
                newGesture.y = y;
                if (arrTouchGesture.size() < 1)
                {
                    firstTimestamp = System.currentTimeMillis();
                    newGesture.t = 0;
                } else {
                    newGesture.t = System.currentTimeMillis() - firstTimestamp;
                }

                newGesture.p = p;
                newGesture.action = "Action_Up";
                arrTouchGesture.add(newGesture);
            }@Override
            public void OnMove(float x, float y, float p){
                TouchGesture newGesture = new TouchGesture();
                newGesture.x = x;
                newGesture.y = y;
                if (arrTouchGesture.size() < 1)
                {
                    firstTimestamp = System.currentTimeMillis();
                    newGesture.t = 0;
                } else {
                    newGesture.t = System.currentTimeMillis() - firstTimestamp;
                }

                newGesture.p = p;
                newGesture.action = "Action_Move";
                arrTouchGesture.add(newGesture);
            }
        });
        mBack = (Button) findViewById(R.id.backbtn);
        mBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });


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

    public void setTextViews(){
        mTvCount.setText("Question:"+" "+ (mCurrQuestion + 1)+"/"+mQuiz.getQuestions().size());
        mTvQuestion.setText(mQuiz.getQuestions().get(mCurrQuestion).getQuestionStatement());
    }
    // Vibrate for 150 milliseconds
    private void vibrate(int duration) {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(duration);
        }
    }

    public static String getAssetJsonData(Context context) {
        StringBuilder json = new StringBuilder();
        try {
            InputStream is = context.getAssets().open("MathsQuiz.json");
            while(true){
                int ch = is.read();
                if(ch == -1)break;
                else json.append((char)ch);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json.toString();

    }
    public void showNextQuestion(){
        mCurrQuestion = mCurrQuestion + 1;
        if(mCurrQuestion >= mQuiz.getQuestions().size() || mTimeUp){
            if(mCorrectAns >= mWrongAns){
                mMassage.setText(R.string.success);
            }else
            {
                mMassage.setText(R.string.failure);
            }
            float percentage = (float)mCorrectAns/mQuiz.getQuestions().size();
            if(percentage >= 1.0f)
            {
                mStars = 3;
            } else if(percentage >= 0.75f && percentage < 1.0f)
            {
                mStars = 2;
            }else if(percentage >= 0.5f && percentage < 0.75f){
                mStars = 1;
            }else
            {
                mStars = 0;
            }mResult.setText("You have got "+mStars+" Stars");
            String Key = mQuizType+"_"+mQuizDifficulty;

            //upload game state to server
            uploadData(Key, "" + mStars);
            GameStateHandler.getInstance().uploadUserInfo();

            String [] gameStateStar = GameStateHandler.getInstance().getGameStete(Key).split("_");
            if(mStars > Integer.parseInt(gameStateStar[1])){
                GameStateHandler.getInstance().setGameStete(mQuizType+"_"+mQuizDifficulty,"unlocked_"+mStars);
            }else{
                mStars = Integer.parseInt(gameStateStar[1]);
            }
            if((Integer.parseInt(mQuizDifficulty) + 1) <= 4 && mStars > 0){
                String nextKey = mQuizType+"_"+(Integer.parseInt(mQuizDifficulty) + 1);
                gameStateStar = GameStateHandler.getInstance().getGameStete(nextKey).split("_");
                if(gameStateStar[0].equals("locked")) {
                    GameStateHandler.getInstance().setGameStete(nextKey,"unlocked_"+0);
                }
            }
            mLLayout.setVisibility(View.VISIBLE);
            mStart = false;
            mCurrQuestion = 0;
            stopCounter();
        }
        setTextViews();
    }
    public void startCounter(){
        mTotal = 0;
        mCdt = new CountDownTimer(mTimetoAns * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                mTotal = (int) (Math.abs((int) millisUntilFinished/(mTimetoAns*10)));
                mBar.setProgress( mTotal);
            }

            public void onFinish() {
                mBar.setProgress(0);
                mTimeUp = true;
                showNextQuestion();
                mCdt = null;
            }
        }.start();
    }
    public void stopCounter(){
        if(mCdt != null){
            mCdt.cancel();
        }
    }
}
