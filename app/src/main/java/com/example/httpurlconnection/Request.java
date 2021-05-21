package com.example.httpurlconnection;

import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.os.Handler;

public class Request {
    // 1. 创建定长线程池对象 & 设置线程池线程数量固定为3
    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
    public void SendRequest(String murl, Handler handler){
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    String m_url =murl+"?timestamp="+System.currentTimeMillis();
                    URL url = new URL(m_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setReadTimeout(8000);
                    httpURLConnection.setConnectTimeout(8000);
                    httpURLConnection.connect();
                    InputStream in=httpURLConnection.getInputStream();
                    String responseData =StreamToString(in);

                    Message message=new Message();
                    message.obj = responseData;
                    handler.sendMessage(message);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        fixedThreadPool.shutdown();
    }

    public void POSTRequest(String murl, HashMap<String,String> params, Handler handler){
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    String m_url=murl+"?timestamp="+System.currentTimeMillis();
                    URL url=new URL(m_url);
                    HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    StringBuilder datatowrite=new StringBuilder();
                    for(String key :params.keySet()){
                        datatowrite.append(key).append("=").append(params.get(key)).append("&");
                    }
                    connection.connect();
                    OutputStream outputStream=connection.getOutputStream();
                    outputStream.write(datatowrite.substring(0,datatowrite.length()-1).getBytes());
                    InputStream in=connection.getInputStream();
                    String respondata =StreamToString(in);

                    Message message=new Message();
                    message.obj=respondata;
                    handler.sendMessage(message);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        fixedThreadPool.shutdown();

    }

    public String StreamToString(InputStream in){
        StringBuilder sb=new StringBuilder();
        String oneline;
        BufferedReader reader=new BufferedReader(new InputStreamReader(in));
        try{
            while((oneline=reader.readLine())!=null){
                sb.append(oneline).append('\n');
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                in.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
