package com.example.hs.smartfishbowl;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.data.Entry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private static String IP_ADDRESS = "192.168.35.164";
    private static String TAG = "phptest";
    private String mJsonString;
    private static final String TAG2 = "tokenid";

    ArrayList<Entry> xList = new ArrayList<>();
    ArrayList<Entry> yList = new ArrayList<>();
    ArrayList<Entry> tempList = new ArrayList<>();
    ArrayList<Entry> turbList = new ArrayList<>();
    ArrayList vidList = new ArrayList();
    ArrayList veloList = new ArrayList();
    ArrayList lidList = new ArrayList();
    ArrayList locList = new ArrayList();
    ArrayList<Entry> xyList0 = new ArrayList<>();
    ArrayList<Entry> xyList1 = new ArrayList<>();
    // 각 시간대별로 현재 상태 저장 => 그 문자열 가져오기

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("SMART FISH BOWL");

        GetData task = new GetData();
        task.execute( "http://" + IP_ADDRESS + "/sensor_db.php", "");

        GetData2 task2 = new GetData2();
        task2.execute("http://"+IP_ADDRESS+"/fish_xy_0.php","");

        GetData3 task_stateLoc = new GetData3();
        task_stateLoc.execute("http://"+IP_ADDRESS+"/nowStateVelo.php","");

        GetData4 task_stateVelo = new GetData4();
        task_stateVelo.execute("http://"+IP_ADDRESS+"/nowStateLoc.php","");

        GetData5 task5 = new GetData5();
        task5.execute("http://"+IP_ADDRESS+"/fish_xy_1.php","");

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        // 메인 네비게이
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //푸시알림 설정
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task)
                    {
                        if (!task.isSuccessful())
                        {
                            Log.w(TAG2, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG2, token);
                        //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // 뒤로가기 버튼을 눌렀을 경우 처리하는 함수
    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // 옵션 메뉴 초기화, 옵션 메뉴를 맨 처음 띄울 때 호
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu, menu);
        return true;
    }

    // 사용자가 선택한 메뉴 항목을 처리
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        Intent intent = null;
        switch(item.getItemId()){
            case R.id.total:
                intent = new Intent(getApplicationContext(), TotalActivity.class);
                break;
        }
        if(intent != null){
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    // activity_navigation_drawer.xml
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;
        if (id == R.id.nav_camera)
        {
            // Handle the camera action
            intent = new Intent(getApplicationContext(), DssActivity.class);
            intent.putExtra("xyList0",xyList0);
            intent.putExtra("xyList1",xyList1);
        }
        else if (id == R.id.nav_gallery)
        {
            intent = new Intent(getApplicationContext(), TotalActivity.class);
            intent.putExtra("tempList",tempList);
            intent.putExtra("turbList",turbList);
        }
        else if (id == R.id.nav_slideshow)
        {
            intent = new Intent(getApplicationContext(), TempActivity.class);
            intent.putExtra("tempList",tempList);
        }
        else if (id == R.id.nav_manage)
        {
            intent = new Intent(getApplicationContext(), TurbActivity.class);
            intent.putExtra("turbList",turbList);
        }
        else if (id == R.id.nav_dictionary)
        {
            intent = new Intent(getApplicationContext(), DictionaryActivity.class);
        }
        else if (id == R.id.nav_send)
        {

        }
        if(intent != null)
        {
            startActivity(intent);
            //intent = null;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClick(View view)  // Activity 전환
    {
        Intent intent = null;

        switch (view.getId()){
            case R.id.button1:
                intent = new Intent(getApplicationContext(), TotalActivity.class);
                intent.putExtra("tempList",tempList);
                intent.putExtra("turbList",turbList);
                intent.putExtra("xyList0",xyList0);
                intent.putExtra("xyList1",xyList1);
                intent.putExtra("vidList",vidList);
                intent.putExtra("veloList", veloList);
                intent.putExtra("lidList",lidList);
                intent.putExtra("locList", locList);
                break;
            case R.id.button2:
                intent = new Intent(getApplicationContext(), PlayActivity.class);
                break;
            case R.id.button3:
                intent = new Intent(getApplicationContext(), DssActivity.class);
                intent.putExtra("xyList0",xyList0);
                intent.putExtra("xyList1",xyList1);
                break;
            case R.id.button4:
                intent = new Intent(getApplicationContext(), TurbActivity.class);
                intent.putExtra("turbList",turbList);
                intent.putExtra("tempList",tempList);
                break;
        }
        if(intent != null){
            startActivity(intent);
        }
    }

    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result);

            if (result == null){
                //mTextViewResult.setText(errorString);
            }
            else {
                mJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            String postParameters = params[1];

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "GetData2 : Error ", e);
                errorString = e.toString();
                return null;
            }
        }
    }

    // 온도, 탁도 리스트
    private void showResult()
    {
        String TAG_JSON="result";
        //String TAG_ID = "result";
        String TAG_TEMP = "temp";
        String TAG_TURB ="tubi";

        try
        {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            int j=0;
            for(int i=(jsonArray.length()-20); i<=(jsonArray.length()-1);i++)
            {
                JSONObject item = jsonArray.getJSONObject(i);

                //String id = item.getString(TAG_ID);
                String temp = item.getString(TAG_TEMP);
                String turb = item.getString(TAG_TURB);
                j++;

                PersonalData personalData = new PersonalData();

                //personalData.setMember_id(id);
                personalData.setMember_temp(temp);
                personalData.setMember_turb(turb);

                float temp2 = Float.parseFloat(temp.toString());
                float turb2 = Float.parseFloat(turb.toString());
                int temp3 = (int)(temp2*10);
                int turb3 = (int)(turb2*100);

                //xyList.add(new Entry(i, temp3));
                tempList.add(new Entry(j, temp3));
                turbList.add(new Entry(j, turb3));
                Log.d("temp",String.valueOf(temp3));
                Log.d("turb",String.valueOf(turb3));
                Log.d("ivalue",String.valueOf(j));
                //mArrayList.add(personalData);
                //mAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    private class GetData2 extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result);

            if (result == null){
                //mTextViewResult.setText(errorString);
            }
            else {
                mJsonString = result;
                showResult2();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            String postParameters = params[1];

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();

                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }
        }
    }

    // x,y 좌표 리스트
    private void showResult2()
    {
        String TAG_JSON2="result";
        //String TAG_ID = "result";
        String TAG_X = "curCx";
        String TAG_Y = "curCy";

        try
        {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray2 = jsonObject.getJSONArray(TAG_JSON2);
            Log.d("length",String.valueOf(jsonArray2.length()));
            for(int i=jsonArray2.length()-1000;i<=(jsonArray2.length()-1);i++)
            {
                //jsonArray.length()
                JSONObject item = jsonArray2.getJSONObject(i);

                //String id = item.getString(TAG_ID);
                String x = item.getString(TAG_X);
                String y = item.getString(TAG_Y);

                PersonalData personalData = new PersonalData();

                //personalData.setMember_id(id);
                personalData.setMember_x(x);
                personalData.setMember_y(y);

                int x2 = Integer.parseInt(x.toString());
                int y2 = Integer.parseInt(y.toString());

                xList.add(new Entry(i, x2));
                yList.add(new Entry(i, y2));
                xyList0.add(new Entry(x2,y2));
                Log.d("xv",x);
                Log.d("yv",y);
                Log.d("ivalue",String.valueOf(x2));
                //mArrayList.add(personalData);
                //mAdapter.notifyDataSetChanged();
            }
        }
        catch (JSONException e)
        {
            Log.d(TAG, "showResult : ", e);
        }
    }

    private class GetData3 extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result);

            if (result == null){
                //mTextViewResult.setText(errorString);
            }
            else {
                mJsonString = result;
                showResult3();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            String postParameters = params[1];

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();

                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "GetData3 : Error ", e);
                errorString = e.toString();

                return null;
            }
        }
    }

    // 속도 상태 리스트
    private void showResult3()
    {
        String TAG_JSON3="result";
        String TAG_ID = "id";
        String TAG_VELO = "velo";

        try
        {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray3 = jsonObject.getJSONArray(TAG_JSON3);

            for(int i=(jsonArray3.length()-2);i<=(jsonArray3.length()-1);i++)
            {
                JSONObject item = jsonArray3.getJSONObject(i);

                int id = item.getInt(TAG_ID);
                String velo = item.getString(TAG_VELO);

                PersonalData personalData = new PersonalData();
                personalData.setMember_vid(id);
                personalData.setMember_velo(velo);

                int vid = id;
                String velo1 = velo;

                vidList.add(vid);
                veloList.add(velo1);
            }
        }
        catch (JSONException e)
        {
            Log.d(TAG, "showResult : ", e);
        }
    }

    private class GetData4 extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result);

            if (result == null){
                //mTextViewResult.setText(errorString);
            }
            else {
                mJsonString = result;
                showResult4();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            String postParameters = params[1];

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();

                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }
        }
    }

    // 위치상태 리스트
    private void showResult4()
    {
        String TAG_JSON4="result";
        String TAG_ID = "id";
        String TAG_LOC = "loc";

        try
        {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray4 = jsonObject.getJSONArray(TAG_JSON4);

            for(int i=(jsonArray4.length()-2);i<=(jsonArray4.length()-1);i++)
            {
                JSONObject item = jsonArray4.getJSONObject(i);

                int id = item.getInt(TAG_ID);
                String loc = item.getString(TAG_LOC);

                PersonalData personalData = new PersonalData();
                personalData.setMember_lid(id);
                personalData.setMember_loc(loc);

                int lid = id;
                String loc1 = loc;

                lidList.add(lid);
                locList.add(loc1);
            }
        }
        catch (JSONException e)
        {
            Log.d(TAG, "showResult : ", e);
        }
    }

    private class GetData5 extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result);

            if (result == null){
                //mTextViewResult.setText(errorString);
            }
            else {
                mJsonString = result;
                showResult5();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            String postParameters = params[1];

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();

                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }
        }
    }

    // x,y 좌표 리스트
    private void showResult5()
    {
        String TAG_JSON2="result";
        //String TAG_ID = "result";
        String TAG_X = "curCx";
        String TAG_Y = "curCy";

        try
        {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray5 = jsonObject.getJSONArray(TAG_JSON2);
            Log.d("length",String.valueOf(jsonArray5.length()));
            for(int i=jsonArray5.length()-1000;i<=(jsonArray5.length()-1);i++)
            {
                //jsonArray.length()
                JSONObject item = jsonArray5.getJSONObject(i);

                //String id = item.getString(TAG_ID);
                String x = item.getString(TAG_X);
                String y = item.getString(TAG_Y);

                PersonalData personalData = new PersonalData();

                //personalData.setMember_id(id);
                personalData.setMember_x(x);
                personalData.setMember_y(y);

                int x2 = Integer.parseInt(x.toString());
                int y2 = Integer.parseInt(y.toString());

                xList.add(new Entry(i, x2));
                yList.add(new Entry(i, y2));
                xyList1.add(new Entry(x2,y2));
                Log.d("xv",x);
                Log.d("yv",y);
                Log.d("ivalue",String.valueOf(i));
                //mArrayList.add(personalData);
                //mAdapter.notifyDataSetChanged();
            }
        }
        catch (JSONException e)
        {
            Log.d(TAG, "showResult : ", e);
        }
    }

}
