package com.example.hs.smartfishbowl;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

class DrawNemo extends View {
    private static String IP_ADDRESS="localhost";
    private static String TAG = "fish_input";
    boolean drawRectangle = false;
    public float x1, y1, x2, y2;
    final Dialog fishDialog;
    Button ok, cancel;
    EditText fishname;
    private Paint paint = new Paint();
    private Paint paint2 = new Paint();
    private Paint paint3 = new Paint();
    String fishnamename;
    public String tf;

    //private Canvas drawCanvas;
    public ArrayList<String> listx1 = new ArrayList<String>();
    public ArrayList<String> listy1 = new ArrayList<String>();
    public ArrayList<String> listx2 = new ArrayList<String>();
    public ArrayList<String> listy2 = new ArrayList<String>();
    public ArrayList<String> listname = new ArrayList<String>();
    public ArrayList<NameValuePair> sendList = new ArrayList<NameValuePair>();

    public DrawNemo(final Context context, AttributeSet attrs){
        super(context, attrs);
        fishDialog = new Dialog(context);
        fishDialog.setContentView(R.layout.fish_dialog);
        fishDialog.setTitle("물고기 설정");

        ok = (Button)fishDialog.findViewById(R.id.ok);
        cancel = (Button)fishDialog.findViewById(R.id.cancel);
        fishname = (EditText)fishDialog.findViewById(R.id.fishname);

        paint.setColor(Color.RED);
        paint.setStrokeWidth(10f);
        paint.setStyle(Paint.Style.STROKE);

        paint2.setColor(Color.WHITE);
        paint2.setAntiAlias(true);
        paint2.setTextSize(80f);

        paint3.setColor(Color.BLACK);
        paint3.setAntiAlias(true);
        paint3.setStrokeWidth(10f);
        paint3.setTextSize(80f);
        paint3.setStyle(Paint.Style.STROKE);

        //paint2.setStyle(Paint.Style.STROKE);

        //drawCanvas = new Canvas();

        ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (fishname.getText().toString().trim().length()>0) {
                    Log.d("start", "<<" + fishname.getText().toString() + ">> x1:" + x1 + " y1:" + y1 + " x2:" + x2 + " y2:" + y2);
                    invalidate();
                    tf = "false";
                    fishDialog.dismiss();
                    listname.add(fishname.getText().toString());
                    fishnamename = fishname.getText().toString();
                    drawRectangle = true;
                    invalidate();
                    fishDialog.dismiss();

                    sendList.clear();
                    sendList.add(new BasicNameValuePair("name",fishname.getText().toString()));
                    sendList.add(new BasicNameValuePair("x1",String.valueOf((y1*400)/1420)));
                    sendList.add(new BasicNameValuePair("y1",String.valueOf(400-(x1*720)/2560)));
                    sendList.add(new BasicNameValuePair("x2",String.valueOf((y2*400)/1420)));
                    sendList.add(new BasicNameValuePair("y2",String.valueOf(400-(x2*720)/2560)));
                    sendList.add(new BasicNameValuePair("end","false"));
                    Log.d("sendList",sendList.get(0).toString()+sendList.get(1).toString()+sendList.get(2).toString()+sendList.get(3).toString()+sendList.get(4).toString()+sendList.get(5).toString());

                    InsertData task = new InsertData();
                    task.execute("http://"+IP_ADDRESS+"/fish_input1.php");
                    fishname.setText(null);
                    sendList.clear();

                } else {
                    Toast.makeText(context,"이름을 입력하세요",Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int listsize = listx1.size()-1;
                listx1.remove(listsize);
                listy1.remove(listsize);
                listx2.remove(listsize);
                listy2.remove(listsize);
                drawRectangle = true;
                invalidate();
                fishDialog.dismiss();
            }
        });
    }

    public void startTask(){
        sendList.add(new BasicNameValuePair("name","end"));
        sendList.add(new BasicNameValuePair("x1","0"));
        sendList.add(new BasicNameValuePair("y1","0"));
        sendList.add(new BasicNameValuePair("x2","0"));
        sendList.add(new BasicNameValuePair("y2","0"));
        sendList.add(new BasicNameValuePair("end","true"));
        tf="true";
        fishnamename = "end";
        InsertData task = new InsertData();
        task.execute("http://"+IP_ADDRESS+"/fish_input1.php");
        Log.d("why","whyyyyyy!!!!!");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                drawRectangle = false;
                x1 = event.getX();
                y1 = event.getY();
                x2 = event.getX();
                y2 = event.getY();
                listx1.add(String.valueOf(x1));
                listy1.add(String.valueOf(y1));
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                x2 = event.getX();
                y2 = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //Log.v("start","x1:"+x1+"y1:"+y1+"x2:"+x2+"y2:"+y2);
                fishDialog.show();
                listx2.add(String.valueOf(x2));
                listy2.add(String.valueOf(y2));
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas){
        if (drawRectangle){
            for (int j = 0; j < listx2.size(); j++){
                float textx = (Float.parseFloat(listx1.get(j))+Float.parseFloat(listx2.get(j)))/2;
                float texty = (Float.parseFloat(listy1.get(j))+Float.parseFloat(listy2.get(j)))/2;
                canvas.drawRect(Float.parseFloat(listx1.get(j)),Float.parseFloat(listy1.get(j)),Float.parseFloat(listx2.get(j)),Float.parseFloat(listy2.get(j)),paint);
                canvas.drawText(listname.get(j),Math.abs(textx)-70,Math.abs(texty),paint3);
                canvas.drawText(listname.get(j),Math.abs(textx)-70,Math.abs(texty),paint2);
            }
        } else {
            canvas.drawRect(x1,y1,x2,y2, paint);
        }
    }

    public class InsertData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
        }

        @Override
        protected String doInBackground(String... params){
            String serverURL = (String)params[0];
            String postParameters ="name="+sendList.get(0).getValue()+"&x1="+sendList.get(1).getValue()+"&y1="+sendList.get(2).getValue()+"&x2="+sendList.get(3).getValue()+"&y2="+sendList.get(4).getValue()+"&end="+sendList.get(5).getValue();
            Log.d("window","name="+fishnamename+"&x1="+String.valueOf((y1*400)/1420)+"&y1="+String.valueOf(400-(x1*720)/2560)+"&x2="+String.valueOf((y2*400)/1420)+"&y2="+String.valueOf(400-(x2*720)/2560));
            Log.d("fishname",fishnamename+"&x1="+String.valueOf((y1*400)/1435)+"&y1="+String.valueOf((x1*720)/2385)+"&x2="+String.valueOf((y2*400)/1435)+"&y2="+String.valueOf((x2*720)/2385));

            try {
                Log.d("try","try");
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                Log.d("try","middle");

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK){
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line=bufferedReader.readLine())!=null){
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString();
            } catch (Exception e){
                Log.d(TAG, e.getMessage());
                return new String("Error: "+e.getMessage());
            }
        }
    }
}
