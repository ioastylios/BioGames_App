package com.example.quizgame;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.location.Location;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.quizgame.Handler.GameStateHandler;
import com.example.quizgame.datamodels.ImageQuiz;
import com.example.quizgame.datamodels.ImageQuizes;
import com.example.quizgame.model.TouchGesture;
import com.example.quizgame.walk.BaseTouchUploadActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TouchAndTapGame extends BaseTouchUploadActivity {
    RelativeLayout mRLayout;
    LinearLayout mHowToPlay,mLLayout;
    ImageView [] mIvArray = new ImageView[20];
    ImageView mIv,mIv1;
    Rect mIvR,mIvR1 = null;
    Rect [] mIvRectArray = new Rect[20];
    int mCounter,mRandomNumToShow;
    Resources mResources;
    Context mContext;
    boolean mTapped;
    ProgressBar mBar;
    Button mOK,mBack;
    boolean mCheckedAns,mReturn,mTimeUp;
    String mQuizType = "",mQuizDifficulty = "";
    ImageQuiz mQuiz;
    int mStars,mCurrQuestion = 0,mCorrectAns = 0,mWrongAns = 0,mTimetoAns=0,mTotal = 0;
    CountDownTimer mCdt;
    TextView mResult,mMassage,mTvHowtoSolve,mTvInstructions;

    long firstTimestamp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_and_tap_game);
        GetQuiz();
        mRLayout = findViewById(R.id.root);
        mContext = getApplicationContext();
        mIv = (ImageView) findViewById(R.id.iv);
        mIv1 = (ImageView) findViewById(R.id.iv1);
        mBack = (Button) findViewById(R.id.backbtn);
        mMassage = findViewById(R.id.massage);
        mTvInstructions = findViewById(R.id.waytosolve);
        mTvHowtoSolve = findViewById(R.id.howtosolve);
        mBar = findViewById(R.id.progressbar);
        mBar.setProgress(100);
        mLLayout = (LinearLayout) findViewById(R.id.fm);
        mHowToPlay = (LinearLayout) findViewById(R.id.howtoans);
        mTvInstructions.setText(R.string.tutorialtouch5);
        mResult = findViewById(R.id.result);
        mResources = getResources();
        mRLayout.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void OnDown(float x, float y, float p){
                if(mIvR1.contains((int)x,(int)y) || mIvR.contains((int)x,(int)y)){
                    mCounter++;
                    mTapped = true;
                }
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
                arrTouchGesture.add(newGesture);
            }
            public void OnPointerDown(float x, float y){
                mCounter++;
                if((mIvR1.contains((int)x,(int)y) || mIvR.contains((int)x,(int)y)) && mTapped && mCounter == 2){
                    //mIv1.setVisibility(View.INVISIBLE);
                    mIvR1 = null;
                    checkForAns();
                    startGame();
                }
            }
            public void OnPointerUp(){
                mCounter--;
            }
            public void OnUp(){
                mTapped = false;
                mCounter  = 0;
            }
            public void OnMove(float x, float y){
                if((!(mIvR1.contains((int)x,(int)y) || mIvR.contains((int)x,(int)y)) && mTapped)){
                    mTapped = false;
                    mCounter = 0;
                }else if(((mIvR1.contains((int)x,(int)y) || mIvR.contains((int)x,(int)y)) && !mTapped)){
                    mTapped = true;
                    mCounter = 1;
                }
            }

        });

        mBack = (Button) findViewById(R.id.backbtn);
        mOK = (Button) findViewById(R.id.okbtn);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mQuiz != null) {
                    mHowToPlay.setVisibility(View.GONE);
                    mBar.setVisibility(View.VISIBLE);
                    mIv.setVisibility(View.VISIBLE);
                    mIv1.setVisibility(View.VISIBLE);
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

    public void checkForAns(){
        if(mTimeUp) {
            mWrongAns = mWrongAns + 1;
            vibrate(150);
        }else{
            mCorrectAns = mCorrectAns + 1;
        }
        mCurrQuestion = mCurrQuestion + 1;
        if(mCurrQuestion >= mQuiz.getNoOfImgs() || mTimeUp){
            if(mCorrectAns > mWrongAns){
                mMassage.setText(R.string.success);
            }else
            {
                mMassage.setText(R.string.failure);
            }
            float percentage = (float)mCorrectAns/mQuiz.getNoOfImgs();
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
            }
            mResult.setText("You have got "+mStars+" Stars");
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
                Key = mQuizType+"_"+(Integer.parseInt(mQuizDifficulty) + 1);
                gameStateStar = GameStateHandler.getInstance().getGameStete(Key).split("_");
                if(gameStateStar[0].equals("locked")) {
                    GameStateHandler.getInstance().setGameStete(Key,"unlocked_"+0);
                }
            }
            mBar.setVisibility(View.INVISIBLE);
            mIv.setVisibility(View.INVISIBLE);
            mIv1.setVisibility(View.INVISIBLE);
            mLLayout.setVisibility(View.VISIBLE);
            mCurrQuestion = 0;
            stopCounter();
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
    public void startGame()
    {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        Random random = new Random();
        int x = width;
        int y = height;
        int imageX = random.nextInt(x-300)+50;
        int imageY = random.nextInt(y-780)+200;
        Log.d("lol", "startGame:imageX " + imageX);
        Log.d("lol", "startGame:imageY " + imageY);
        mIv1.setX(imageX);
        mIv1.setY(imageY);
        mIvR1 = new Rect((int)mIv1.getX(), (int)mIv1.getY(), (int)mIv1.getX()+mIv1.getWidth(), (int)mIv1.getY()+mIv1.getHeight());
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mIvR = new Rect(mIv.getLeft(), mIv.getTop(), mIv.getRight(),mIv.getBottom());
            mIvR1 =new Rect((int)mIv1.getX(), (int)mIv1.getY(), (int)mIv1.getX()+mIv1.getWidth(), (int)mIv1.getY()+mIv1.getHeight());
        }
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
                checkForAns();
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
