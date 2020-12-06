package com.example.quizgame.Handler;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.quizgame.Application;
import com.example.quizgame.model.Constant;
import com.example.quizgame.model.CustomRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GameStateHandler {
    private static final GameStateHandler Instance = new GameStateHandler();
    public static GameStateHandler getInstance() {
        return Instance;
    }
    private SharedPreferences sharedpreferences;
    private GameStateHandler() {
        sharedpreferences = Application.getContext().getSharedPreferences("GameState", Context.MODE_PRIVATE);
    }
    public String getGameStete(String Key){
        return sharedpreferences.getString(Key, "");
    }
    public void setGameStete(String Key,String value){
        SharedPreferences.Editor mEditor = sharedpreferences.edit();
        if(sharedpreferences.contains(Key)){
            mEditor.remove(Key);
        }
        mEditor.putString(Key, value);
        mEditor.apply();
    }
    public boolean isKeyExist(String Key){
        return sharedpreferences.contains(Key);
    }

    public String getAllGameStates() {
        Map<String, ?> map = sharedpreferences.getAll();
        String strStates = map.toString();
        return strStates;
    }

    public void ClearGameState() {
        sharedpreferences.edit().clear().commit();
        final SharedPreferences sp = Application.getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }

    public void InitializeGame(final String id) {
        ClearGameState();
        final SharedPreferences sp = Application.getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        RequestQueue requestQueue = Volley.newRequestQueue(Application.getContext());

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, Constant.get_user_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Get User Info:", response.toString());
                        try {
                            String id = response.getString("id");
                            String name = response.getString("name");
                            String age = response.getString("age");
                            String gender = response.getString("gender");
                            String game_state = response.getString("game_state");
                            JSONObject game_state_obj = new JSONObject(game_state);

                            SharedPreferences.Editor editor = sp.edit();

                            editor.putString("userid", id);
                            editor.putString("username", name);
                            editor.putString("userage", age);
                            editor.putString("usergender", gender);

                            editor.apply();

                            Iterator<String> iter = game_state_obj.keys();
                            while (iter.hasNext()) {
                                String key = iter.next();
                                String value = String.valueOf(game_state_obj.get(key));
                                setGameStete(key, value);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Get User Info Fail:", error.toString());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                return params;
            }
        };

        requestQueue.add(jsonReq);
    }

    public void uploadUserInfo() {
        final SharedPreferences sp = Application.getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        final String id = sp.getString("userid", "-1");
        final String name = sp.getString("username", "");
        final String age = sp.getString("userage", "");
        final String gender = sp.getString("usergender", "");
        final String game_state = getAllGameStates();

        RequestQueue requestQueue = Volley.newRequestQueue(Application.getContext());

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, Constant.update_user_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Users Upload sucess:", "");
                        try {
                            String id = response.getString("id");
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("userid", id);
                            editor.apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Users Upload Fail:", error.toString());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("name", name);
                params.put("age", age);
                params.put("gender", gender);
                params.put("game_state", game_state);
                return params;
            }
        };

        requestQueue.add(jsonReq);

    }

}
