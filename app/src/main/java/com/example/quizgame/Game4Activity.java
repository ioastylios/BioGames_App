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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
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

public class Game4Activity extends BaseTouchUploadActivity {
    LinearLayout mHowToPlay,mLLayout,mImageContainer;
    Switch [] mSwitcharray;
    ImageView mImageVans;
    ProgressBar mBar;
    int mStars,mCurrQuestion = 0,mTimetoAns=0,mTotal = 0;
    CountDownTimer mCdt;
    boolean mTimeUp,mCheckedArray[];
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
        setContentView(R.layout.activity_game4);
        GetQuiz();
        mBack = (Button) findViewById(R.id.backbtn);
        mMassage = findViewById(R.id.massage);
        mTvInstructions = findViewById(R.id.waytosolve);
        mTvHowtoSolve = findViewById(R.id.howtosolve);
        mBar = findViewById(R.id.progressbar);
        mBar.setProgress(100);
        mLLayout = (LinearLayout) findViewById(R.id.fm);
        mHowToPlay = (LinearLayout) findViewById(R.id.howtoans);
        mImageContainer = findViewById(R.id.imgcontainer);
        mImageVans = findViewById(R.id.ansimgv);
        mTvInstructions.setText(R.string.tutorialtouch4);
        mResult = findViewById(R.id.result);
        mContext = getApplicationContext();
        mResources = getResources();
        mSwitcharray = new Switch[mQuiz.getNoOfImgs()];
        mCheckedArray = new boolean[mQuiz.getNoOfImgs()];
        mBack = (Button) findViewById(R.id.backbtn);
        mOK = (Button) findViewById(R.id.okbtn);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mQuiz != null) {
                    mHowToPlay.setVisibility(View.GONE);
                    mTimetoAns = mQuiz.getTime();
                    StartGame();
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

    public void StartGame(){
        Random random = new Random();
        while (mArrayList.size() < mQuiz.getNoOfImgs()) {
            int a = random.nextInt(20) + 1;
            if (!mArrayList.contains(a)) {
                mArrayList.add(a);
            }
        }
        for (int i = 0; i < mQuiz.getNoOfImgs(); i++) {
            final int resourceId = mResources.getIdentifier("switch" + mArrayList.get(i), "id", mContext.getPackageName());
            final int index = i;
            mSwitcharray[i] = (Switch) findViewById(resourceId);
            mSwitcharray[i].setVisibility(View.VISIBLE);
            if(i == 2 || i == 5 || i == 0){
                mSwitcharray[i].setRotation(90);
            }
            mSwitcharray[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (mCurrQuestion < mQuiz.getNoOfImgs() && !mTimeUp && !mCheckedArray[index]) {
                        mCurrQuestion = mCurrQuestion + 1;
                        mCheckedArray[index] = true;
                    }
                    checkStar();
                }
            });

            mSwitcharray[i].setOnTouchListener(new OnSwipeTouchListener1(this) {
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


        }
    }

    private void checkStar() {

        if (mCurrQuestion >= mQuiz.getNoOfImgs() || mTimeUp) {
            float percentage = (float)mCurrQuestion/mQuiz.getNoOfImgs();
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

            if (mStars == 0) {
                vibrate(150);
                mMassage.setText(R.string.failure);
            } else {
                mMassage.setText(R.string.success);
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
                checkStar();
                mCdt = null;
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

