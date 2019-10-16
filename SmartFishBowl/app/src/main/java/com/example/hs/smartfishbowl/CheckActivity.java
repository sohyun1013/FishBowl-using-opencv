package com.example.hs.smartfishbowl;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckActivity extends AppCompatActivity implements Runnable {
    private DrawNemo drawNemo;
    Bitmap bitmap;

    //메인 스레드와 백그라운드 스레드 간의 통신
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        drawNemo = (DrawNemo)findViewById(R.id.drawing);

        Thread th = new Thread(CheckActivity.this);
        th.start();
    }
    //백그라운드 스레드
    @Override
    public void run(){
        URL url = null;
        try {
            url = new URL("http://192.168.41.119/windows/startimage.jpg");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();

            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bitmap = BitmapFactory.decodeStream(is);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);

            BitmapDrawable ob = new BitmapDrawable(getResources(), rotatedBitmap);
            drawNemo.setBackground(ob);
            Log.d("bitmap",String.valueOf(bitmap));
            handler.sendEmptyMessage(0);
            is.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.checkmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = null;
        switch (item.getItemId()){
            case R.id.check:
                drawNemo.startTask();
                intent = new Intent(CheckActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

