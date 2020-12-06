package com.example.quizgame.walk;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.quizgame.model.Constant;
import com.example.quizgame.model.CustomRequest;
import com.example.quizgame.model.TouchGesture;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseTouchUploadActivity extends AppCompatActivity {

    protected List<TouchGesture> arrTouchGesture = new ArrayList<>();

    protected void uploadData(final String strStage, final String strScore) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, Constant.url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Touch Success:", "");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Touch error:", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sp = getApplicationContext().getSharedPreferences("UserInfo", MODE_PRIVATE);
                params.put("userid", sp.getString("userid", ""));
                params.put("username", sp.getString("username", ""));
                params.put("stage", strStage);
                params.put("contents", arrTouchGesture.toString() + " ");
                params.put("score", strScore);
                return params;
            }
        };

        requestQueue.add(jsonReq);
    }
}
