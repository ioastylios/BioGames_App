package com.example.quizgame.PostHnadler;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundTask extends AsyncTask<String,Void,String>{


    Context context;

    String accurl,eventsurl,updateurl,gestureurl,gyrourl,fetchurl,gpsurl;
    public int fid;
//    GlobalTouchService gts;


    BackgroundTask(Context ctx){

        this.context=ctx;


    }


//    public void setMainGS()
//    {
//        gts = (GlobalTouchService) this.context;
//    }

    @Override
    protected void onPreExecute() {

        //String strAPIRoot="http://10.0.2.2/SensorTest/";
        String strAPIRoot = "http://icsdweb.aegean.gr/project/stylios/";
        //String strAPIRoot = "http://lordsofts.tk/dev/sensorTest/";
        //String strAPIRoot = "http://18.222.142.220/stylious/";
        accurl=strAPIRoot + "events_meta_acc.php";
        eventsurl=strAPIRoot+"insert_events.php";
        updateurl=strAPIRoot+"update.php";
        gestureurl=strAPIRoot+"events_meta_ges.php";
        gyrourl=strAPIRoot+"events_meta_gyro.php";
        fetchurl=strAPIRoot+"fetchid.php";
        gpsurl=strAPIRoot+"events_meta_gps.php";

    }


    @Override
    protected String doInBackground(String... params) {

        String method=params[0];

        if(method.equals("accelerometer")){

            String id=params[1];
            String type=params[2];
            String value=params[3];

            try {
                Log.d("lol", "doInBackground: 1");
                URL url =new URL(accurl);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String data= URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8")+"&"+
                        URLEncoder.encode("type","UTF-8")+"="+URLEncoder.encode(type,"UTF-8")+"&"+
                        URLEncoder.encode("value","UTF-8")+"="+URLEncoder.encode(value,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream=httpURLConnection.getInputStream();
                inputStream.close();
                Log.d("lol", "doInBackground: 2");
                return  "Accelerometer success";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        else if(method.equals("gps")){

            String id=params[1];
            String type=params[2];
            String value=params[3];

            try {
                URL url =new URL(gpsurl);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String data= URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8")+"&"+
                        URLEncoder.encode("type","UTF-8")+"="+URLEncoder.encode(type,"UTF-8")+"&"+
                        URLEncoder.encode("value","UTF-8")+"="+URLEncoder.encode(value,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream=httpURLConnection.getInputStream();
                inputStream.close();
                return  "GPS Success";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if(method.equals("keyboard")) {

            String id = params[1];
            String type = params[2];
            String value = params[3];

            try {
                URL url = new URL(gpsurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&" +
                        URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8") + "&" +
                        URLEncoder.encode("value", "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();
                return "Keyboard Success";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        else if(method.equals("gyroscope")){
            String id=params[1];
            String type=params[2];
            String value=params[3];

            try {
                URL url =new URL(gyrourl);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String data= URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8")+"&"+
                        URLEncoder.encode("type","UTF-8")+"="+URLEncoder.encode(type,"UTF-8")+"&"+
                        URLEncoder.encode("value","UTF-8")+"="+URLEncoder.encode(value,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream=httpURLConnection.getInputStream();
                inputStream.close();
                return  "gyroscope success";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        else if(method.equals("gesture")){
            String id=params[1];
            String type=params[2];
            String value=params[3];

            try {
                URL url =new URL(gestureurl);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String data= URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8")+"&"+
                        URLEncoder.encode("type","UTF-8")+"="+URLEncoder.encode(type,"UTF-8")+"&"+
                        URLEncoder.encode("value","UTF-8")+"="+URLEncoder.encode(value,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream=httpURLConnection.getInputStream();
                inputStream.close();
                return  "gesture success";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        else if(method.equals("update")){
            String id=params[1];


            try {
                Log.d("update",id);
                URL url =new URL(updateurl);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String data= URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream=httpURLConnection.getInputStream();
                inputStream.close();
                return  "update success";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        else if(method.equals("insert events")){
            String phone=params[1];
            String imei=params[2];

            try {
                URL url =new URL(eventsurl);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String data= URLEncoder.encode("phone","UTF-8")+"="+URLEncoder.encode(phone,"UTF-8")
                        +"&"+URLEncoder.encode("imei","UTF-8")+"="+URLEncoder.encode(imei,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream=httpURLConnection.getInputStream();

                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String response=bufferedReader.readLine();

                bufferedReader.close();

                inputStream.close();
                Log.d("events",response);
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        else if(method.equals("fetchid")){

            try {
                Log.d("lol", "doInBackground:hahaha ");
                URL url =new URL(fetchurl);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String response=bufferedReader.readLine();

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("lolfetchid",response);
                return  response;//+":event_code";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        if(result == null){
            return;
        }
        if(result.equals("Accelerometer success")){

            Log.d("Accelerometer",result);
        }
        else if(result.equals("gyroscope success")){

            Log.d("gyroscope",result);
        }

        else if(result.equals("gesture success")){
            Log.d("gesture",result);
        }
        else if(result.equals("update success")){
            Log.d("update",result);
        }
        else if(result.contains("event_code")){
            fid=Integer.parseInt(result.split("event_code:")[1]);
            SharedPreferences preferences = context.getSharedPreferences("prefName", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit= preferences.edit();

            edit.putInt("fetchID", fid);
            edit.apply();

            Log.d("insert_events",result);
        }

    }
}

