package com.example.quizgame;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.quizgame.Handler.GameStateHandler;
import com.example.quizgame.model.Constant;
import com.example.quizgame.model.CustomRequest;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String  USER_INFO = "UserInfo";
    String  USER_NAME = "username";
    String  USER_AGE = "userage";
    String  USER_GENDER = "usergender";
    Button mKeyStroke,mTouch,mWalk,mTest;
    int selectedUserIdx = -1;

    Dialog m_dialog = null;
    Dialog m_TCdialog = null;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_main);
        mTouch = (Button) findViewById(R.id.game1);
        mTouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchActivity();
            }
        });
        mKeyStroke = (Button) findViewById(R.id.game2);
        mKeyStroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyStrokeActivity();
            }
        });
        mWalk = (Button) findViewById(R.id.game3);
        mWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walkActivity();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTouch = (Button) findViewById(R.id.game1);
        mTouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchActivity();
            }
        });
        mKeyStroke = (Button) findViewById(R.id.game2);
        mKeyStroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyStrokeActivity();
            }
        });
        mWalk = (Button) findViewById(R.id.game3);
        mWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walkActivity();
            }
        });

        showDialog();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onDestroy() {

        hideDialog();
        super.onDestroy();

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
    public void touchActivity(){
        Intent i = new Intent(this,Touch.class);
        i.putExtra("Touch","touch");
        startActivity(i);
    }
    public void keyStrokeActivity(){
        Intent i = new Intent(this, Keystroke.class);
        i.putExtra("Keystroke","maths");
        startActivity(i);
    }
    public void walkActivity(){
        Intent i = new Intent(this, Walk.class);
        i.putExtra("Walk","walk");
        startActivity(i);
    }

    private void writeUserName(String strName) {
        SharedPreferences sp = getApplicationContext().getSharedPreferences(USER_INFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(USER_NAME, strName);

        editor.apply();
    }

    private void writeUserInfo(String strName, String strAge, String strGender) {
        SharedPreferences sp = getApplicationContext().getSharedPreferences(USER_INFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(USER_NAME, strName);
        editor.putString(USER_AGE, strAge);
        editor.putString(USER_GENDER, strGender);

        editor.apply();
    }

    private String readUserName() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences(USER_INFO, MODE_PRIVATE);
        return sp.getString(USER_NAME, null);
    }

    private String readUserAge() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences(USER_INFO, MODE_PRIVATE);
        return sp.getString(USER_AGE, null);
    }

    private String readUserGender() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences(USER_INFO, MODE_PRIVATE);
        return sp.getString(USER_GENDER, null);
    }

    JSONArray ArrUsers;
    List<String> ArrUserName = new ArrayList<String>();
    ArrayAdapter<String> dataAdapter;

    int nCnt = 0;

    private void readUsers() {
        RequestQueue requestQueue = Volley.newRequestQueue(Application.getContext());

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, Constant.get_all_users_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Users get sucess:", response.toString());
                        try {
                            ArrUsers = response.getJSONArray("users");
                            for (int i = 0; i < ArrUsers.length(); i ++ ) {
                                JSONObject user = new JSONObject(ArrUsers.get(i).toString());
                                ArrUserName.add(user.getString("name"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(dataAdapter!=null) {
                            dataAdapter.notifyDataSetChanged();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Get All User Fail:", error.toString());
                        nCnt++;
                        if (nCnt > 5) {
                            Toast.makeText(getApplicationContext(), "Unable to read data from server. Please check your network state", Toast.LENGTH_LONG).show();
                            return;
                        }
                        readUsers();
                    }
                });

        requestQueue.add(jsonReq);

    }

    private void showDialog() {
        if (m_dialog != null) {
            return;
        }
        readUsers();
        m_dialog = new Dialog(this);
        m_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        m_dialog.setContentView(R.layout.dialog_username);
        m_dialog.setCancelable(false);

        final View layout_new = m_dialog.findViewById(R.id.layout_new);
        final View layout_prev = m_dialog.findViewById(R.id.layout_prev);

        Button btnConfirm = m_dialog.findViewById(R.id.btn_confirm);
        final EditText txtName = m_dialog.findViewById(R.id.user_name);
        final RadioButton radioPrev = m_dialog.findViewById(R.id.radio_prev);
        final RadioButton radioNew = m_dialog.findViewById(R.id.radio_new);
        final RadioButton radioCurrent = m_dialog.findViewById(R.id.radio_current);
        final CheckBox chTerms = m_dialog.findViewById(R.id.ch_terms);
//        final CheckBox chSelect = dialog.findViewById(R.id.ch_select);
        final EditText txtAge = m_dialog.findViewById(R.id.user_age);
        final RadioButton radioMale = m_dialog.findViewById(R.id.radio_male);
        final RadioButton radioFemale = m_dialog.findViewById(R.id.radio_female);

        Spinner spinnerUser = m_dialog.findViewById(R.id.spinner_user);
        dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, ArrUserName);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUser.setAdapter(dataAdapter);

        spinnerUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedUserIdx = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedUserIdx = -1;
            }
        });

        final String userName = readUserName();
        if (userName == null) {
            m_dialog.findViewById(R.id.layout_ask).setVisibility(View.GONE);
        } else {
            txtName.setText(userName);
        }

        final String userAge = readUserAge();
        if (userAge != null) {
            txtAge.setText(userAge);
        }

        final String userGender = readUserGender();
        if (userGender != null) {
            int age = Integer.valueOf(userGender);
            if ( age == 0 ) {
                radioMale.setChecked(true);
                radioFemale.setChecked(false);
            } else {
                radioMale.setChecked(false);
                radioFemale.setChecked(true);
            }
        }

        chTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showTermsConditionsDlg();
                }
            }
        });

        radioNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_new.setVisibility(View.VISIBLE);
                layout_prev.setVisibility(View.GONE);
            }
        });

        radioPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_prev.setVisibility(View.VISIBLE);
                layout_new.setVisibility(View.GONE);
            }
        });

        radioCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_prev.setVisibility(View.GONE);
                layout_new.setVisibility(View.GONE);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioNew.isChecked()) {
                    if (!chTerms.isChecked()) {
                        Toast.makeText(getApplicationContext(), "Please select checkbox", Toast.LENGTH_LONG).show();
                        return;
                    }
                    String strName = txtName.getText().toString();
                    if (radioNew.isChecked() && strName.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please insert your name.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    String strAge = txtAge.getText().toString();
                    String strGender = radioMale.isChecked() ? "0" : "1";

                    int age = Integer.valueOf(strAge);
                    if(age < 16) {
                        Toast.makeText(getApplicationContext(), "you have to be at least 16 to enter this app", Toast.LENGTH_LONG).show();
                        return;
                    }
                    GameStateHandler.getInstance().ClearGameState();
                    writeUserInfo(strName, strAge, strGender);
                    GameStateHandler.getInstance().uploadUserInfo();

                } else if (radioPrev.isChecked()) {
                    if (selectedUserIdx < 0) {
                        Toast.makeText(getApplicationContext(), "Please select a user from spinner", Toast.LENGTH_LONG).show();
                        return;
                    }
                    JSONObject user = null;
                    String strID = "";
                    try {
                        user = new JSONObject(ArrUsers.get(selectedUserIdx).toString());
                        strID = user.getString("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(strID.isEmpty()) {
                        return;
                    }

                    GameStateHandler.getInstance().InitializeGame(strID);
                }
                hideDialog();
            }
        });

        m_dialog.show();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        m_dialog.getWindow().setLayout(width, ActionBar.LayoutParams.WRAP_CONTENT);
    }

    private void showTermsConditionsDlg() {
        if (m_TCdialog != null)
            return;

        m_TCdialog = new Dialog(this);
        m_TCdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        m_TCdialog.setContentView(R.layout.dialog_terms_condition);

        String htmlAsString = getString(R.string.terms_condition_contents);      // used by WebView
        Spanned htmlAsSpanned = Html.fromHtml(htmlAsString); // used by TextView

        // set the html content on a TextView
        TextView textView = m_TCdialog.findViewById(R.id.txtTermsConditions);
        textView.setText(htmlAsSpanned);

        Button btnAccept = m_TCdialog.findViewById(R.id.btn_accept);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_TCdialog.dismiss();
            }
        });
        m_TCdialog.show();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        m_TCdialog.getWindow().setLayout(width, height);
    }

    private void hideDialog() {
        if (m_dialog != null) {
            m_dialog.dismiss();
            m_dialog = null;
        }
        if (m_TCdialog != null) {
            m_TCdialog.dismiss();
            m_TCdialog = null;
        }
    }
}
