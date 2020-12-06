package com.example.quizgame;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.quizgame.Handler.GameStateHandler;
import com.example.quizgame.datamodels.ImageQuiz;
import com.example.quizgame.datamodels.ImageQuizes;
import com.example.quizgame.model.TouchGesture;
import com.example.quizgame.walk.BaseTouchUploadActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Game2Activity extends BaseTouchUploadActivity {
    LinearLayout mImgContainer,mAnsLayout,mHowToPlay,mLLayout,mLinearLayout;
    ImageView [] mImgarray;
    ImageView mImageVans;
    ProgressBar mBar;
    int mQuestionCount,mStars,mCurrQuestion = 1,mCorrectAns = 0,mWrongAns = 0,mTimetoAns=0,mTotal = 0;
    CountDownTimer mCdt;
    boolean mReturn,mTimeUp;
    ArrayList<Integer> mArrayList = new ArrayList<Integer>();
    Resources mResources;
    Context mContext;
    TextView mResult,mMassage,mTvHowtoSolve,mTvInstructions;
    Button mOK,mBack;
    String mQuizType = "",mQuizDifficulty = "";
    ImageQuiz mQuiz;

    long firstTimestamp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2);
        GetQuiz();
        mBack = (Button) findViewById(R.id.backbtn);
        mMassage = findViewById(R.id.massage);
        mTvInstructions = findViewById(R.id.waytosolve);
        mTvHowtoSolve =findViewById(R.id.howtosolve);
        mBar = findViewById(R.id.progressbar);
        mBar.setProgress(100);
        mImgContainer = findViewById(R.id.imgcontainer);
        mLinearLayout = (LinearLayout) findViewById(R.id.root);
        mLLayout = (LinearLayout) findViewById(R.id.fm);
        mHowToPlay = (LinearLayout) findViewById(R.id.howtoans);
        mAnsLayout = findViewById(R.id.anslayout);
        mImageVans = findViewById(R.id.ansimgv);
        mTvInstructions.setText(R.string.tutorialtouch2);
        mResult = findViewById(R.id.result);
        mContext = getApplicationContext();
        mResources = getResources();
        mImgarray = new ImageView[mQuiz.getNoOfImgs()];
        mQuestionCount = 17;
        Random random = new Random();
        while (mArrayList.size() < mQuiz.getNoOfImgs()) { // how many numbers u need - it will 6
            int a = random.nextInt(mQuestionCount)+1; // this will give numbers between 1 and 50.

            if (!mArrayList.contains(a)) {
                mArrayList.add(a);
            }
        }
        for (int i = 0; i <  mQuiz.getNoOfImgs(); i++)
        {
            final int resourceId = mResources.getIdentifier("img"+(i+1), "id", mContext.getPackageName());
            final int imgId = mResources.getIdentifier("img"+(mArrayList.get(i)), "drawable", mContext.getPackageName());
            mImgarray[i] = (ImageView) findViewById(resourceId);
            mImgarray[i].setImageResource(imgId);
        }
        mBack = (Button) findViewById(R.id.backbtn);
        mOK = (Button) findViewById(R.id.okbtn);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mQuiz != null) {
                    mHowToPlay.setVisibility(View.GONE);
                    mTimetoAns = mQuiz.getTime();
                    startCounter();
                }
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mAnsLayout.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeUp() {
                if(mArrayList.contains(mCurrQuestion) && !mTimeUp){
                    mCorrectAns = mCorrectAns + 1;
                }else
                {
                    mWrongAns = mWrongAns + 1;
                    vibrate(150);
                }
                showNextImage();
            }
            public void onSwipeDown() {
                if(!mArrayList.contains(mCurrQuestion) && !mTimeUp){
                    mCorrectAns = mCorrectAns + 1;
                }else
                {
                    mWrongAns = mWrongAns + 1;
                    vibrate(150);
                }
                showNextImage();
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
            }
            @Override
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

    public void startCounter(){
        mTotal = 0;
        /** CountDownTimer starts with 2 minutes and every onTick is 1 second */
        mCdt = new CountDownTimer(mTimetoAns * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                mTotal = (int) (Math.abs((int) millisUntilFinished/(mTimetoAns*10)));
                mBar.setProgress( mTotal);
            }

            public void onFinish() {
                mBar.setProgress(0);
                if(mImgContainer.getVisibility() == View.VISIBLE){
                    final int imgId = mResources.getIdentifier("img"+mCurrQuestion, "drawable", mContext.getPackageName());
                    mImageVans.setImageResource(imgId);
                    mImgContainer.setVisibility(View.GONE);
                    mAnsLayout.setVisibility(View.VISIBLE);
                    mTimetoAns = 10;
                    startCounter();
                }else
                {
                    mTimeUp = true;
                    showNextImage();
                    mCdt = null;
                }


            }
        }.start();
    }
    public void stopCounter(){
        if(mCdt != null){
            mCdt.cancel();
        }
    }

    // Vibrate for 150 milliseconds
    private void vibrate(int duration) {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(duration);
        }
    }
    public void showNextImage(){
        mCurrQuestion = mCurrQuestion + 1;
        if(mCurrQuestion >= mQuestionCount + 1 || mTimeUp){
            if(mCorrectAns > mWrongAns){
                mMassage.setText(R.string.success);
            }else
            {
                mMassage.setText(R.string.failure);
            }
            float percentage = (float)mCorrectAns/mQuestionCount;
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
            mCurrQuestion = 0;
            stopCounter();

        }
        //stopCounter();
        final int imgId = mResources.getIdentifier("img"+mCurrQuestion, "drawable", mContext.getPackageName());
        mImageVans.setImageResource(imgId);
        //mTimetoAns = 5;
        //startCounter();
    }

    public void GetQuiz(){
        Intent intent = getIntent();
        String [] quizTypeAndDiff = intent.getStringExtra("quizTypeAndDiff").split("_");
        mQuizType = quizTypeAndDiff[0];
        mQuizDifficulty = quizTypeAndDiff[1];
        String json = getAssetJsonData(this);
        Gson gson = new Gson();
        ImageQuizes quizes = gson.fromJson(json,ImageQuizes.class);
        mQuiz = quizes.getQuiz(mQuizType,mQuizDifficulty);
    }
    public static String getAssetJsonData(Context context) {
        StringBuilder json = new StringBuilder();
        try {
            InputStream is = context.getAssets().open("TouchQuiz.json");
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
}
