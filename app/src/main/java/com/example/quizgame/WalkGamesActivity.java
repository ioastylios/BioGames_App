package com.example.quizgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
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


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.quizgame.Handler.GameStateHandler;
import com.example.quizgame.datamodels.WalkGame;
import com.example.quizgame.datamodels.WalkGames;
import com.example.quizgame.model.Constant;
import com.example.quizgame.model.CustomRequest;
import com.example.quizgame.model.WalkGesture;
import com.example.quizgame.walk.StepDetector;
import com.example.quizgame.walk.StepListener;
import com.example.quizgame.PostHnadler.PostResult;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

public class WalkGamesActivity extends AppCompatActivity implements SensorEventListener, StepListener {

    LinearLayout mHowToPlay,mLLayout,mLinearLayout;
    TextView mTvCount,mTvQuestion,textView,mResult,mMassage,mTvHowtoSolve,mTvInstructions, txtTime;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel, gyro;
    private String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps;
    int mTimetoAns=0,mTotal = 0,mStars = 0;
    WalkGame mGame;
    CountDownTimer mCdt;
    boolean mTimeUp;
    ProgressBar mBar;
    Button mOK,mBack;
    String mQuizType = "",mQuizDifficulty = "";
    float mX,mY,mZ;
    Timer t;
    TimerTask task;

    int MAX_TIME = 30;

    List<WalkGesture> arrGestures = new ArrayList<>();

    long firstTimestamp = 0;

    boolean bUploaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_games);
        GetGame();
        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        //simpleStepDetector.registerListener(this);
        simpleStepDetector.registerListener(this);
        textView = findViewById(R.id.tv_steps);
        mMassage = findViewById(R.id.massage);
        txtTime = findViewById(R.id.tv_times);

        mTvInstructions = findViewById(R.id.waytosolve);
        mTvHowtoSolve = findViewById(R.id.howtosolve);
        mBar = findViewById(R.id.progressbar);
        mBar.setProgress(100);
        mLLayout = (LinearLayout) findViewById(R.id.fm);
        mHowToPlay = (LinearLayout) findViewById(R.id.howtoans);
        mTvInstructions.setText(R.string.tutorialtouch4);
        mResult = findViewById(R.id.result);
        if(mQuizType.equals("walk2")) {
            mTvInstructions.setText(getResources().getString(R.string.tutorialwalk2) + mGame.getTime() / 60 + " minute");
            TEXT_NUM_STEPS = "Number of Jumps: ";
            mTvHowtoSolve.setText(R.string.walk_method2);
            //mTvHowtoSolve.setText("Do " +mGame.getNoOfStep() +" Jumps to complete the game:");

        }else if(mQuizType.equals("walk3")){
            mTvInstructions.setText(getResources().getString(R.string.tutorialwalk3) + mGame.getTime() / 60 + " minute");
            TEXT_NUM_STEPS = "Number of Squats: ";
            mTvHowtoSolve.setText(R.string.walk_method3);
            //mTvHowtoSolve.setText("Do " +mGame.getNoOfStep() +" Squats to complete the game:");
        }else if(mQuizType.equals("walk4")){
            mTvInstructions.setText(getResources().getString(R.string.tutorialwalk4) + mGame.getTime() / 60 + " minute");
            TEXT_NUM_STEPS = "Number of Squats: ";
            mTvHowtoSolve.setText(R.string.walk_method4);

            //mTvHowtoSolve.setText("Take " +mGame.getNoOfStep() +" Squats to complete the game:");
        }else
        {
            mTvInstructions.setText(getResources().getString(R.string.tutorialwalk1) + mGame.getTime() / 60 + " minute");
            TEXT_NUM_STEPS = "Number of Steps: ";
            mTvHowtoSolve.setText(R.string.walk_method1);

            //mTvHowtoSolve.setText("Take " +mGame.getNoOfStep() +" Steps to complete the game:");
        }
        mBack = (Button) findViewById(R.id.backbtn);
        mOK = (Button) findViewById(R.id.okbtn);
        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mGame != null) {
                    mHowToPlay.setVisibility(View.GONE);
                    mTimetoAns = mGame.getTime();
                    numSteps = 0;
                    textView.setText(TEXT_NUM_STEPS + numSteps);
                    sensorManager.registerListener(WalkGamesActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
                    gyro=sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
                    if(gyro!=null){
                        sensorManager.registerListener(WalkGamesActivity.this,gyro, SensorManager.SENSOR_DELAY_NORMAL);
                    }
                    startCounter();
                }
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //startTimer();
        new PostResult("","");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uploadData();
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

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //convert to data time.
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            System.out.println(sdf.format(new Date(event.timestamp / 1000000)));

            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2],mQuizType);
            mX = event.values[0];
            mY = event.values[1];
            mZ = event.values[2];

            addNewGesture(event);

        } else if (event.sensor.getType() == event.sensor.TYPE_GYROSCOPE)
        {
            addNewGesture(event);
        }
    }

    private void addNewGesture(SensorEvent event) {
        WalkGesture newGesture = new WalkGesture();
        if (arrGestures.size() < 1)
        {
            firstTimestamp = System.currentTimeMillis();
            newGesture.timestamp = 0;
        } else {
            newGesture.timestamp = System.currentTimeMillis() - firstTimestamp;
        }

        newGesture.x = event.values[0];
        newGesture.y = event.values[1];
        newGesture.z = event.values[2];
        newGesture.strType = event.sensor.getType() == event.sensor.TYPE_GYROSCOPE ? "GYROSCOPE" : "ACCELEROMETER";
        arrGestures.add(newGesture);
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        textView.setText(TEXT_NUM_STEPS + numSteps);
        if(numSteps > mGame.getNoOfStep() || mTimeUp){
            stopCounter();
//            checkForComplete();
        }
    }

    public void checkForComplete(){
//        if(numSteps >= mGame.getNoOfStep() && !mTimeUp){
//            mMassage.setText(R.string.success);
//            mStars = 3;
//        }else {
//            mMassage.setText(R.string.failure);
//            mStars = 0;
//        }
        mMassage.setText(R.string.success);
        mStars = Math.min(3, numSteps / 10);
        mResult.setText("Congratulations! you burned 100 calories");

//        if (mQuizType.equals(("walk1")) || mQuizType.equals(("walk2"))) {
//            mResult.setText("Congratulations on the steps you took to burn 100 calories");
//        } else {
//            mResult.setText("Congratulations you made " + mGame.getNoOfStep() + " steps in " + mTimetoAns + " seconds and you burnt 100 calories");
//        }

//        mResult.setText("You have got "+mStars+" Stars");
        String Key = mQuizType+"_"+mQuizDifficulty;

        uploadData();
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
        mLLayout.setVisibility(View.VISIBLE);

    }

    private void uploadData() {

        if (bUploaded)
            return;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, Constant.url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Keystore Success:", "");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Keystore error:", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sp = getApplicationContext().getSharedPreferences("UserInfo", MODE_PRIVATE);
                params.put("userid", sp.getString("userid", ""));
                params.put("username", sp.getString("username", ""));
                params.put("stage", mQuizType+"_"+mQuizDifficulty);
                params.put("contents", getContents());
                params.put("score", "" + mStars);

                return params;
            }
        };

        requestQueue.add(jsonReq);
        bUploaded = true;
    }

    private String getContents()
    {
        String strContent = "[";
        int i = 0, nCnt = arrGestures.size();

        for (i = 0 ; i < nCnt; i ++)
        {
            strContent += arrGestures.get(i).toString();
            if (i != nCnt - 1)
                strContent +=  ", ";
        }
        strContent += "]";
        return strContent;
    }

    long startTime, timeInSecond = 0;
    Handler customHandler = new Handler();
    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInSecond = (SystemClock.uptimeMillis() - startTime)/1000;
            txtTime.setText("Time elapsed - " + timeInSecond / 3600 + " : " + timeInSecond / 60 + " : " + timeInSecond % 60);
            customHandler.postDelayed(this, 1000);
        }
    };

    public void startCounter(){
        mTotal = 0;
        mCdt = new CountDownTimer(mTimetoAns * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                mTotal = (int) (Math.abs((int) millisUntilFinished/(mTimetoAns*10)));
                mBar.setProgress( mTotal);
                int elapsedSecond = (int)(mTimetoAns - millisUntilFinished / 1000);
                txtTime.setText("Time elapsed - " + elapsedSecond / 60 + " : " + elapsedSecond % 60);
            }

            public void onFinish() {
                mBar.setProgress(0);
                mTimeUp = true;
                checkForComplete();
                mCdt = null;
            }
        }.start();
    }
//
//    public void startCounter(){
//        if (mTimetoAns < 1) {
//            mBar.setVisibility(View.GONE);
//            mTimetoAns = MAX_TIME;
//        }
//        startTime = SystemClock.uptimeMillis();
//        customHandler.postDelayed(updateTimerThread, 0);
//    }

    public void stopCounter(){
        if(customHandler != null){
            customHandler.removeCallbacks(updateTimerThread);
        }
        mBar.setProgress(0);
        mTimeUp = true;
        checkForComplete();

    }

    // Vibrate for 150 milliseconds
    private void vibrate(int duration) {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(duration);
        }
    }

    public void GetGame(){
        Intent intent = getIntent();
        String [] quizTypeAndDiff = intent.getStringExtra("quizTypeAndDiff").split("_");
        mQuizType = quizTypeAndDiff[0];
        mQuizDifficulty = quizTypeAndDiff[1];
        String json = getAssetJsonData(this);
        Gson gson = new Gson();
        WalkGames games = gson.fromJson(json,WalkGames.class);
        mGame = games.getQuiz(mQuizType,mQuizDifficulty);
    }
    public static String getAssetJsonData(Context context) {
        StringBuilder json = new StringBuilder();
        try {
            InputStream is = context.getAssets().open("WalkQuiz.json");
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

    public void startTimer(){
        t = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
                        Date date = new Date();
                        String time=dateFormat.format(date);
                        Log.d("CurTime","Time is : " + time);
                        JSONObject accelerometerobj = new JSONObject();
                        try{

                            accelerometerobj.put("X", Float.toString(mX));
                            accelerometerobj.put("Y",Float.toString(mY));
                            accelerometerobj.put("Z",Float.toString(mZ));
                        }catch(JSONException e){
                            Log.d(TAG, "JSONexception"+e);
                        }
                        String accelerometerInsert=accelerometerobj.toString();
                        Log.d(TAG, accelerometerInsert);
                        new PostResult("accelerometer",accelerometerobj.toString());

                    }
                });
            }
        };
        t.scheduleAtFixedRate(task, 0, 300);
    }
}
