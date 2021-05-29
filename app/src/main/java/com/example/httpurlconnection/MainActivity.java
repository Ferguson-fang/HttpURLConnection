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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button get;
    private Button post;
    private Button gson;
    private Button gsonf;
    private TextView tv;
    private String url;
    private Map<String,String> map = new HashMap<>();
    private MHander mHander = new MHander();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        Gson gson;
    }

    void init(){
        get = findViewById(R.id.btn_GET);
        post = findViewById(R.id.btn_POST);
        tv = findViewById(R.id.response);
        gson = findViewById(R.id.btn_GSONTOJSON);
        gsonf = findViewById(R.id.btn_GSONFROMJSON);

        get.setOnClickListener(this);
        post.setOnClickListener(this);
        gson.setOnClickListener(this);
        gsonf.setOnClickListener(this);

        map.put("name","Ferguson");
        map.put("age","19");
        map.put("hobby","programming");

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

            case R.id.btn_GSONTOJSON:
                showGsonToJson(map);
                break;

            case R.id.btn_GSONFROMJSON:
                String str =
                  "{\"name\":\"Ferguson\",\"age\":\"19\",\"hobby\":\"programming\"}";
                //{"name":"Ferguson","age":"23","hobby":"programming"}
                showGsonFromJson(str);
                break;
        }
    }
    private void showGsonToJson(Map<String,String> jsonData){
        Class<?> c = null;
        try {
            c = Class.forName("com.google.gson.Gson");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        try {
            Method met = c.getMethod("toJson",Object.class);
            String rv = null;
            rv = (String)met.invoke(c.newInstance(),jsonData);
            Log.e("sssa",rv);
            tv.setText(rv);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showGsonFromJson(String jsonData){
        Class<?> c = null;
        try {
            c = Class.forName("com.google.gson.Gson");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        try {
            Method met = c.getMethod("fromJson",String.class, Type.class);
            PersonBean person = (PersonBean) met.invoke(c.newInstance(),jsonData,PersonBean.class);
            String rv = "姓名:"+person.getName() + "年龄:" + person.getAge() + "爱好:" + person.getHobby();
            Log.e("sss",rv);
            tv.setText(rv);
        }catch (Exception e){
            e.printStackTrace();
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