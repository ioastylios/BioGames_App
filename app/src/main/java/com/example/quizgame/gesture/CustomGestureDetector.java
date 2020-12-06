package com.example.quizgame.gesture;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class CustomGestureDetector extends GestureDetector{

    com.example.quizgame.OnSwipeTouchListener.CustomOnGestureListener mListener;

    public CustomGestureDetector(Context context, com.example.quizgame.OnSwipeTouchListener.CustomOnGestureListener listener) {
        super(context, listener);
        mListener = listener;
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean consume = (mListener != null)? mListener.onTouchEvent(ev) : false;
        return consume || super.onTouchEvent(ev);
    }



}