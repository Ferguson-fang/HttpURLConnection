package com.example.httpurlconnection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button get;
    private Button post;
    private TextView tv;
    private String url;
    private MHander mHander = new MHander();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    void init(){
        get = findViewById(R.id.btn_GET);
        post = findViewById(R.id.btn_POST);
        tv = findViewById(R.id.response);

        get.setOnClickListener(this);
        post.setOnClickListener(this);

        url = "http://guolin.tech/api/china";

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_GET:
                Request request = new Request();
                request.SendRequest(url,mHander);

                break;
            case R.id.btn_POST:

                break;
        }
    }

    private class MHander extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String responseData = msg.obj.toString();
            tv.setText(responseData);

        }
    }
}