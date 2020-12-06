package com.example.quizgame.PostHnadler;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.example.quizgame.Application;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class PostResult {
    public PostResult(String method,String data){
//        fetchid();
//        String phone = Build.MANUFACTURER
//                + " " + Build.MODEL + " " + Build.VERSION.RELEASE
//                + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();
//
//
//        String imei = Settings.Secure.ANDROID_ID;
//
//
//        insert_events(phone,imei);
        SharedPreferences preferences = Application.getContext().getSharedPreferences("prefName", Context.MODE_PRIVATE);
        int id1=preferences.getInt("fetchID",-1);
        Log.d("FetchIDFromGTS",Integer.toString(id1));
        String accelerometerInsert = "";
        String gestureInsert = "";
        if(method.equals("accelerometer")){
            accelerometerInsert = data;

        }else if(method.equals("gesture")){
            gestureInsert = data;
        }
        if(id1 !=-1) {
            insert_acc(id1, "accelerometer", accelerometerInsert);
            insert_gesture(id1,"gesture",gestureInsert);
        }
    }
    void insert_acc(int id,String type,String value){

        String method="accelerometer";
        String sid=Integer.toString(id);
        BackgroundTask backgroundTask =new BackgroundTask(Application.getContext());
        backgroundTask.execute(method,sid,type,value);
    }
    void insert_gesture(int id,String type,String value){

        String method="gesture";
        String sid=Integer.toString(id);
        BackgroundTask backgroundTask =new BackgroundTask(Application.getContext());
        backgroundTask.execute(method,sid,type,value);
    }
    public void fetchid(){
        String method="fetchid";
        BackgroundTask backgroundTask =new BackgroundTask(Application.getContext());
        backgroundTask.execute(method);
    }
    void insert_events(String phone,String imei){

        String method="insert events";
        BackgroundTask backgroundTask =new BackgroundTask(Application.getContext());
        backgroundTask.execute(method,phone,imei);
    }
}
