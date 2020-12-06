package com.example.quizgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.quizgame.Handler.GameStateHandler;
import com.example.quizgame.datamodels.Quiz;
import com.example.quizgame.datamodels.Quizes;
import com.example.quizgame.model.Constant;
import com.example.quizgame.model.CustomRequest;
import com.example.quizgame.model.KeyGesture;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeystrokeQuizActivity extends AppCompatActivity  implements KeyboardView.OnKeyboardActionListener, View.OnTouchListener{
    LinearLayout mHowToPlay,mLLayout;
    RelativeLayout mLinearLayout;
    TextView mTvCount,mTvQuestion,mResult,mMassage,mTvHowtoSolve,mTvInstructions;
    EditText mAnsEt;
    Button mDone,mOK,mBack;
    int mCurrQuestion = 0,mCorrectAns = 0,mWrongAns = 0,mTimetoAns=0,mTotal = 0,mStars = 0;
    Quiz mQuiz;
    String mQuizType = "",mQuizDifficulty = "";
    String[] mSeparatedStr;
    boolean mReturn,mTimeUp;
    ProgressBar mBar;
    CountDownTimer mCdt;

    List<KeyGesture> arrKeyGestures = new ArrayList<>();

    long firstTimestamp = 0;

    //The keyboard view
    Keyboard keyboard;
    //The whole view
    KeyboardView keyboardView;

    private long lastPress = 0;
    private long pressDiff = 0;
    private long presstime = 0;
    float posX;
    float posY;
    int keyPressed = ' ';
    private boolean caps = false;
    float pressure;

    //the state, which controlls, if keypress or keytouch manages the event
    int eventState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keystroke_quiz);
        mTimeUp = false;
        Intent intent = getIntent();
        String [] quizTypeAndDiff = intent.getStringExtra("quizTypeAndDiff").split("_");
        mQuizType = quizTypeAndDiff[0];
        mQuizDifficulty = quizTypeAndDiff[1];
        String json = getAssetJsonData(this);
        Gson gson = new Gson();
        Quizes quizes = gson.fromJson(json,Quizes.class);
        mTvCount = (TextView) findViewById(R.id.tv1);
        mTvQuestion = (TextView) findViewById(R.id.tvquestion);
        mResult = (TextView) findViewById(R.id.result);
        mMassage = (TextView) findViewById(R.id.massage);
        mTvInstructions = (TextView) findViewById(R.id.waytosolve);
        mTvHowtoSolve = (TextView) findViewById(R.id.howtosolve);
        mLinearLayout = findViewById(R.id.root);
        mLLayout = (LinearLayout) findViewById(R.id.fm);
        mHowToPlay = (LinearLayout) findViewById(R.id.howtoans);
        mBar = (ProgressBar) findViewById(R.id.progress);
        mBar.setProgress(100);
        mAnsEt = findViewById(R.id.ans);
        mDone = (Button) findViewById(R.id.done);
        mDone.setVisibility(View.GONE);
        mBack = (Button) findViewById(R.id.backbtn);
        if(mQuizType.equals("keystroke2")) {
            mTvInstructions.setText(R.string.tutorialkey2);
            mTvHowtoSolve.setText(" Remember all the shown number and enter the digits separated by space ' ' :");
            //mAnsEt.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        }else if(mQuizType.equals("keystroke3")){
            mTvInstructions.setText(R.string.tutorialkey3);
            mTvHowtoSolve.setText("Remember all the shown words and write them separated by space ' ' :");
        }else if(mQuizType.equals("keystroke4")){
            mTvInstructions.setText(R.string.tutorialkey4);
            mTvHowtoSolve.setText("Remember the shown sentence and write it:");
        }else
        {
            mTvInstructions.setText(R.string.tutorialkey1);
            mTvHowtoSolve.setText("Write the shown number in words: ");
        }
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
                    if (mQuizType.equals("keystroke2") || mQuizType.equals("keystroke3") || mQuizType.equals("keystroke4")) {
                        mReturn = false;
                        mSeparatedStr = null;
                        setTextViewsDelay();
                    } else {
                        setTextViews();
                        openKeyBoard();
                    }
                    mTimetoAns = mQuiz.getTime();
                    startCounter();
                }
                mHowToPlay.setVisibility(View.GONE);

            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        keyboardView = (KeyboardView) findViewById(R.id.keyboard_view);

        //creating the keyboard and setting the listeners
        keyboard = new Keyboard(this,R.xml.keys_layout);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);
        keyboardView.setOnTouchListener(this);
        keyboardView.setPreviewEnabled(false);
        lastPress = Calendar.getInstance().getTimeInMillis();
        closeKeyBoard();
        mAnsEt.setInputType(InputType.TYPE_NULL);
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
    public void setTextViewsDelay(){
        closeKeyBoard();
        mDone.setVisibility(View.GONE);
        mAnsEt.setVisibility(View.GONE);
        mTvCount.setText("Question:"+" "+ (mCurrQuestion + 1)+"/"+mQuiz.getQuestions().size());
        if(mQuizType.equals("keystroke4")){
            mSeparatedStr = mQuiz.getQuestions().get(mCurrQuestion).getQuestionStatement().split("=");
        }else {
            mSeparatedStr = mQuiz.getQuestions().get(mCurrQuestion).getQuestionStatement().split(" ");
        }
        mTvQuestion.post(new Runnable() {
            int i = 0;
            @Override
            public void run() {
                if (i >= mSeparatedStr.length) {
                    mReturn = true;
                }else
                {
                    mTvQuestion.setText(mSeparatedStr[i]);
                    i++;
                }
                mTvQuestion.postDelayed(this, 2000);
                if(mReturn){
                    if(mQuizType.equals("keystroke2")) {
                        mTvQuestion.setText("Write all the shown number separated by space ' ' :");
                    }else if(mQuizType.equals("keystroke3")){
                        mTvQuestion.setText("Write all the shown words separated by space ' ' :");
                    }else if(mQuizType.equals("keystroke4")){
                        mTvQuestion.setText("Write the shown sentence:");
                    }
                    //mDone.setVisibility(View.VISIBLE);
                    mAnsEt.setVisibility(View.VISIBLE);
                    openKeyBoard();
                    i=0;
                    mTvQuestion.removeCallbacks(this);
                }
            }
        });
    }
    // Vibrate for 150 milliseconds
    private void vibrate(int duration) {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(duration);
        }
    }
    public void checkAnswer(){
        if(mQuiz.getQuestions().get(mCurrQuestion).getAnswer().replaceFirst("\\s++$", "").equalsIgnoreCase(mAnsEt.getText().toString().replaceFirst("\\s++$", ""))&& !mTimeUp){
            mCorrectAns = mCorrectAns + 1;
        }else
        {
            mWrongAns = mWrongAns + 1;
            vibrate(150);
        }
        mCurrQuestion = mCurrQuestion + 1;
        if(mCurrQuestion >= mQuiz.getQuestions().size() || mTimeUp){
            closeKeyBoard();
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
            }
            mResult.setText("You have got "+mStars+" Stars");
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
            mCurrQuestion = 0;
            stopCounter();
        }
        if(mQuizType.equals("keystroke2") || mQuizType.equals("keystroke3")  || mQuizType.equals("keystroke4")) {
            mReturn = false;
            mSeparatedStr = null;
            setTextViewsDelay();
        }else{
            setTextViews();
        }
        mAnsEt.setText("");
    }

    private void uploadData() {

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
                params.put("contents", arrKeyGestures.toString() + " ");
                params.put("score", "" + mStars);

                return params;
            }
        };

        requestQueue.add(jsonReq);
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
                checkAnswer();
                mCdt = null;
            }
        }.start();
    }
    public void stopCounter(){
        if(mCdt != null){
            mCdt.cancel();
        }
    }
    public void openKeyBoard(){
        keyboardView.setVisibility(View.VISIBLE);
    }
    public void closeKeyBoard(){
        keyboardView.setVisibility(View.GONE);
    }

    @Override
    public void onPress(int i) {

    }

    @Override
    public void onRelease(int i) {

    }

    @Override
    public void onKey(int i, int[] ints) {

        keyPressed = i;

        eventState++;
        //both finished the event
        if(eventState > 1)
        {
            eventState = 0;
            handleDataAfterBothEvents();
        }
    }

    @Override
    public void onText(CharSequence charSequence) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            //reset the eventState
            eventState = 0;

            //calculating the time between up and down
            pressDiff = Calendar.getInstance().getTimeInMillis() - lastPress;
        }
        if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
            //collecting the values
            lastPress = Calendar.getInstance().getTimeInMillis();

            presstime = motionEvent.getEventTime() - motionEvent.getDownTime();

            posX = motionEvent.getX();
            posY = view.getHeight() - motionEvent.getY();
            pressure = motionEvent.getPressure();
            //find out, if both events are finished
            eventState++;
            //both finished the event
            if(eventState > 1)
            {
                eventState = 0;
                handleDataAfterBothEvents();
            }
        }

        return false;
    }


    /**
     * handling the collected data after both events
     */
    private void handleDataAfterBothEvents()
    {
        String key;
        switch (keyPressed) {
            case Keyboard.KEYCODE_DELETE:
                String text = String.valueOf(mAnsEt.getText());

                if (text.length() > 0) {
                    mAnsEt.setText(text.substring(0, text.length() - 1));
                }
                key = "Delete";
                break;
            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboard.setShifted(caps);
                keyboardView.invalidateAllKeys();
                key = "Caps";
                break;
            case Keyboard.KEYCODE_DONE:
                checkAnswer();
                return;
            default:
                char code = (char) keyPressed;
                if (Character.isLetter(code) && caps) {
                    code = Character.toUpperCase(code);
                }
                mAnsEt.setText(mAnsEt.getText() + String.valueOf(code));
                break;
        }

        //send data to db
        KeyGesture newKey = new KeyGesture();
        newKey.keyCode = (char)keyPressed;
        newKey.pressure = pressure;
        newKey.tp = presstime;
        newKey.ts = pressDiff;
        newKey.posX = posX;
        newKey.posY = posY;
        arrKeyGestures.add(newKey);

    }

}
