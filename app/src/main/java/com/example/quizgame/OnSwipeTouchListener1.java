package com.example.quizgame;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;

import com.example.quizgame.PostHnadler.GlobalValues;
import com.example.quizgame.gesture.CustomGestureDetector;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Detects left and right swipes across a view.
 */
public class OnSwipeTouchListener1 implements View.OnTouchListener {

    private class CustomGestureDetector1 extends GestureDetector{

        com.example.quizgame.OnSwipeTouchListener1.CustomOnGestureListener mListener;

        public CustomGestureDetector1(Context context, com.example.quizgame.OnSwipeTouchListener1.CustomOnGestureListener listener) {
            super(context, listener);
            mListener = listener;
        }
        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            boolean consume = (mListener != null)? mListener.onTouchEvent(ev) : false;
            return consume || super.onTouchEvent(ev);
        }



    }

    private final CustomGestureDetector1 mGestureDetector;

    public OnSwipeTouchListener1(Context context) {
        mGestureDetector = new CustomGestureDetector1(context, new CustomOnGestureListener(context));
    }
    public void OnDown(float x, float y, float p){}
    public void OnPointerDown(float x, float y){}
    public void OnPointerUp(){}
    public void OnUp(float x, float y, float p){}
    public void OnMove(float x, float y, float p){}
    public void onSwipeLeft() {
    }

    public void onSwipeRight() {
    }

    public void onSwipeUp() {
    }

    public void onSwipeDown() {
    }

    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    private class CustomOnGestureListener implements GestureDetector.OnGestureListener,
            GestureDetector.OnDoubleTapListener {

        //, OnSwipeTouchListener{

        public final String TAG = CustomOnGestureListener.class.getSimpleName();

        public static final int TAP = 0;
        public static final int DOUBLE_TAP = 1;

        public  final long DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
        public  float mViewScaledTouchSlop;
        TextView mGestureName;

        private MotionEvent mCurrentDownEvent;
        private float[] pinchDistance = new float[]{1010101010,0101010101};

        private int mPtrCount = 0;
        private int totalFingerCounts;

        private float mPrimStartTouchEventX = 0;
        private float mPrimStartTouchEventY = 0;
        private float mSecStartTouchEventX = 0;
        private float mSecStartTouchEventY = 0;
        private float mPrimSecStartTouchDistance = 0;

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
        private int gestureAction = -1;
        private int noOfFingers = -1;
        private String fingerCoOrdinates = "";
        private String  fingerPressure = "";



        long downTimestamp = System.currentTimeMillis();

        private class GestureHandler extends Handler {
            GestureHandler() {
                super();
            }

            GestureHandler(Handler handler) {
                super(handler.getLooper());
            }

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case TAP:
                        // prepend("TAP Finger Count "+mCurrentDownEvent.getPointerCount());
                        onSingleTapConfirmed(mCurrentDownEvent);
                        break;
                    case DOUBLE_TAP:
                        // prepend("Double Tap "+mCurrentDownEvent.getPointerCount());
                        onDoubleTapConfirmed(mCurrentDownEvent);
                        break;
                    default:
                        throw new RuntimeException("Unknown message " + msg); // never
                }
            }
        }

        GestureHandler mHandler;
        public CustomOnGestureListener(Context context) {
            // mGestureName = (TextView) gestureName;
            mHandler = new GestureHandler();

            final ViewConfiguration viewConfig = ViewConfiguration.get(context);
            mViewScaledTouchSlop = viewConfig.getScaledTouchSlop();
        }
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return false;
        }
        public boolean onTouchEvent(MotionEvent ev) {
            int action = (ev.getAction() & MotionEvent.ACTION_MASK);



            totalFingerCounts = ev.getPointerCount();
            noOfFingers = totalFingerCounts;
            Log.d("Total Finger Counts", totalFingerCounts + "");

            StringBuilder s = new StringBuilder();
            StringBuilder cord = new StringBuilder();
            StringBuilder pressure = new StringBuilder();

            s.append( '\n');
            s.append("No of fingers on Screen" );
            s.append(ev.getPointerCount());
            s.append( '\n');
            for(int i = 0; i< ev.getPointerCount(); i++){

                s.append("Finger ");
                s.append(i ) ;
                s.append(" coordinates");
                s.append( '\n');
                s.append("X:" );
                s.append(ev.getX(i) );
                s.append("  Y:" );
                s.append(ev.getY(i) );
                cord.append(ev.getX(i));
                cord.append(" ");
                cord.append(ev.getY(i) );
                cord.append(" ");
                s.append( '\n');
                s.append("Pressure:" );
                s.append(ev.getPressure(i) );
                pressure.append(ev.getPressure(i));
                pressure.append(" ");
                s.append( '\n');

            }

            fingerCoOrdinates = cord.toString();
            fingerPressure =  pressure.toString();





            SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:mm:ss");
            String formattedDate = df.format(Calendar.getInstance().getTime());

            s.append("Time:" );
            s.append( formattedDate);

            //mGestureName.setText(s.toString());

            // prepend("onTouchEvent() ptrs:" + ev.getPointerCount() + " "
            // + actionToString(action));
            switch (action) {
                case MotionEvent.ACTION_POINTER_DOWN:
                    mPtrCount++;
                    Log.d("ACTIONACTION", "ACTION_POINTER_DOWN");
                    OnPointerDown(ev.getX(mPtrCount-1),ev.getY(mPtrCount-1));
                    if (ev.getPointerCount() > 1) {
                        mSecStartTouchEventX = ev.getX(1);
                        mSecStartTouchEventY = ev.getY(1);
                        mPrimSecStartTouchDistance = distance(ev, 0, 1);

                        if (mCurrentDownEvent != null)
                            mCurrentDownEvent.recycle();
                        mCurrentDownEvent = MotionEvent.obtain(ev);

                        if (System.currentTimeMillis() - downTimestamp > 50) {

                            if (!mHandler.hasMessages(TAP)) {
                                mHandler.sendEmptyMessageDelayed(TAP,
                                        DOUBLE_TAP_TIMEOUT);
                            } else {
                                mHandler.removeMessages(TAP);
                                mHandler.sendEmptyMessageDelayed(DOUBLE_TAP,
                                        DOUBLE_TAP_TIMEOUT);
                            }

                        }

                        downTimestamp = System.currentTimeMillis();

                        // return false to prevent other actions.
                        return false;
                    }
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    Log.d("ACTIONACTION", "ACTION_POINTER_UP");
                    OnPointerUp();
                    mPtrCount--;
                    if(mPtrCount == 0){
                        Log.d("NOACTIONS1", "NOACTIONS1");
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    Log.d("ACTIONACTION", "ACTION_DOWN");
                    OnDown(ev.getX(),ev.getY(), ev.getPressure());
                    //Log.d("FinallText", s.toString());
                    mPtrCount++;
                    break;
                case MotionEvent.ACTION_UP:
                    Log.d("ACTIONACTION", "ACTION_UP");
                    OnUp(ev.getX(),ev.getY(), ev.getPressure());
                    mPtrCount--;
                    if(mPtrCount == 0){
                        Log.d("NOACTIONS", "NOACTIONS");
                        pinchDistance = new float[]{1010101010,0101010101};

                        //mGestureName.setText("No Fingers" + getGestureAction());
                        //mGestureName.setText("");
                        //setFingerCoOrdinates();
                        reset();
                        setGestureAction(-1);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.d("ACTIONACTION", "ACTION_MOVE");
                    OnMove(ev.getX(),ev.getY(), ev.getPressure());
                    break;

            }

            return false;
        }


        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // prepend("onDoubleTap() ptrs:" + e.getPointerCount());
            if (mCurrentDownEvent.getPointerCount() == 1) {
                //prepend("onDoubleTap() ptrs:" + mCurrentDownEvent.getPointerCount());
                mHandler.sendEmptyMessageDelayed(DOUBLE_TAP, DOUBLE_TAP_TIMEOUT);
            }
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            // prepend("onDoubleTapEvent() ptrs:" + e.getPointerCount());

            return false;
        }

        /**
         * We don't want to ignore the old double tap method. Or in onDoubleTap
         * ignore MotionEvent and use our mCurrentDownEvent and don't call double
         * tap in the handler
         *
         * @param e
         * @return
         */
        public boolean onDoubleTapConfirmed(MotionEvent e) {


            if (mPtrCount == 1) {
                prepend("onDoubleTapConfirmed(): tap and a half");
            }else{
                prepend("onDoubleTapConfirmed() ptrs:" + e.getPointerCount());
            }
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (!mHandler.hasMessages(TAP)
                    && mCurrentDownEvent.getPointerCount() == e.getPointerCount())
                prepend("onSingleTapConfirmed() ptrs:" + e.getPointerCount());

            if (mPtrCount == 1 && mCurrentDownEvent.getPointerCount() == 2) {
                // one finger is still down and a single tap occured
                prepend("onSingleTapConfirmed(): One finger down, one finger tap");
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            // prepend("onDown() ptrs:" + e.getPointerCount());
            if (mCurrentDownEvent != null)
                mCurrentDownEvent.recycle();
            mCurrentDownEvent = MotionEvent.obtain(e);

            mPrimStartTouchEventX = e.getX();
            mPrimStartTouchEventY = e.getY();

            return false;
        }



        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {


            Log.d("SWIPEFINGERS", "count" + e1.getPointerCount()+"_" + e2.getPointerCount());

            boolean result = false;

            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeDown();
                        } else {
                            onSwipeUp();
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }

//        public void onSwipeRight()
//        {
//
//            showToast("right");
//            gestureAction = GlobalValues.GESTURE_RIGHT;
//        }
//
//        public void onSwipeLeft(){
//
//            showToast("left");
//            gestureAction = GlobalValues.GESTURE_RIGHT;
//        }
//
//        public void onSwipeUp() {
//
//            showToast("up");
//            gestureAction = GlobalValues.GESTURE_UP;
//        }
//
//        public void onSwipeDown() {
//
//            showToast("down");
//            gestureAction = GlobalValues.GESTURE_DOWN;
//        }

//        private void showToast(String action){
//            Toast.makeText(Application.getContext(), action, Toast.LENGTH_SHORT).show();
//        }



        @Override
        public void onLongPress(MotionEvent e) {
            prepend("onLongPress() ptrs:" + e.getPointerCount());

        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                float distanceY) {
            // e1 The first down motion event that started the scrolling.
            // e2 The move motion event that triggered the current onScroll.


            boolean scroll = isScrollGesture(e2);
            boolean pinch = isPinchGesture(e2);

            if(pinch){
                prepend( " pinch = " + pinch);

                if(pinchDistance [0] > pinchDistance[1]){

                    prepend( " pinch = pinch in" );
                    gestureAction = GlobalValues.GESTURE_ZOOM_IN;
                }else{

                    prepend( " pinch = zoom" );
                    gestureAction = GlobalValues.GESTURE_ZOOM_OUT;
                }
            }else if(scroll){
                //prepend("onScroll() scroll = " + scroll );

            }



            if (scroll || pinch)
                cancelAll();

            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // prepend("onShowPress() ptrs:" + e.getPointerCount());

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // prepend("onSingleTapUp() ptrs:" + e.getPointerCount());
            return false;
        }

        private void prepend(String method) {
//        StringBuilder s = new StringBuilder(mGestureName.getText());
//
//        s.insert(0, '\n');
//        s.insert(0, method);
            //mGestureName.setText(method);
        }

        private void cancelAll() {
            if (mHandler.hasMessages(TAP))
                mHandler.removeMessages(TAP);
        }

        private boolean isPinchGesture(MotionEvent event) {
            if (event.getPointerCount() == 2) {
                final float distanceCurrent = distance(event, 0, 1);



                final float diffPrimX = mPrimStartTouchEventX - event.getX(0);
                final float diffPrimY = mPrimStartTouchEventY - event.getY(0);
                final float diffSecX = mSecStartTouchEventX - event.getX(1);
                final float diffSecY = mSecStartTouchEventY - event.getY(1);


                if (// if the distance between the two fingers has increased past
                    // our threshold
                        Math.abs(distanceCurrent - mPrimSecStartTouchDistance) > mViewScaledTouchSlop
                                // and the fingers are moving in opposing directions
                                && (diffPrimY * diffSecY) <= 0
                                && (diffPrimX * diffSecX) <= 0) {
                    // mPinchClamp = false; // don't clamp initially
                    Log.d("ZOOOOOM", distanceCurrent +  "\n"
                            + diffPrimX + "\n"
                            +	diffPrimY + "\n"
                            +	diffSecY);
                    if(pinchDistance[0]== 1010101010) {
                        pinchDistance[0] = distanceCurrent;
                    }
                    pinchDistance[1] = distanceCurrent;
                    return false;
                }
            }

            return false;
        }

        public float distance(MotionEvent event, int first, int second) {
            if (event.getPointerCount() >= 2) {
                final float x = event.getX(first) - event.getX(second);
                final float y = event.getY(first) - event.getY(second);

                return (float) Math.sqrt(x * x + y * y);
            } else {
                return 0;
            }
        }

        private boolean isScrollGesture(MotionEvent event) {
            if (event.getPointerCount() == 2) {
                final float diffPrim = mPrimStartTouchEventY - event.getY(0);
                final float diffSec = mSecStartTouchEventY - event.getY(1);

                if (// make sure both fingers are moving in the same direction
                        diffPrim * diffSec > 0
                                // make sure both fingers have moved past the scrolling
                                // threshold
                                && Math.abs(diffPrim) > mViewScaledTouchSlop
                                && Math.abs(diffSec) > mViewScaledTouchSlop) {
                    return false;
                }
            } else if (event.getPointerCount() == 1) {
                final float diffPrim = mPrimStartTouchEventY - event.getY(0);
                if (// make sure finger has moved past the scrolling threshold
                        Math.abs(diffPrim) > mViewScaledTouchSlop) {
                    return false;
                }
            }

            return false;
        }

        public void setGestureAction(int action){
            gestureAction = action;
        }

        public int getGestureAction(){
            return gestureAction;
        }

        public void setNoOfFingers(){
            noOfFingers = -1;
        }

        public int getNoOfFingers(){
            return noOfFingers;
        }


        public void setFingerCoOrdinates() {
            fingerCoOrdinates = "";

        }

        public String getFingerCoOrdinates() {
            return  fingerCoOrdinates;

        }

        public void setFingerPressure() {
            fingerPressure = "";

        }

        public String getFingerPressure() {
            return  fingerPressure;

        }



        public void reset(){
            //gestureAction = -1;
            fingerCoOrdinates = "";
            fingerPressure = "";
            noOfFingers = -1;
        }


    }
}

