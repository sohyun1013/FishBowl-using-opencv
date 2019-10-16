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
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private static String TAG = "phptest";
    private String mJsonString;
    private static final String TAG2 = "tokenid";
    final String IP_ADDRESS = "192.168.41.119";

    Toolbar toolbar;
    int fish=0;

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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("SMART FISH BOWL");

        GetData task1 = new GetData();
        task1.execute( "http://" + IP_ADDRESS + "/sensor_db.php", "");

        GetData task2 = new GetData();
        task2.execute("http://"+IP_ADDRESS+"/fish_xy_0.php","");

        GetData task3 = new GetData();
        task3.execute("http://"+IP_ADDRESS+"/nowStateVelo.php","");

        GetData task4 = new GetData();
        task4.execute("http://"+IP_ADDRESS+"/nowStateLoc.php","");

        GetData task5 = new GetData();
        task5.execute("http://"+IP_ADDRESS+"/fish_xy_1.php","");

        // 메인 네비게이션
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //FCM 푸시알림 설정
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
                        String token = task.getResult().getToken();
                        Log.d(TAG2, token);
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
        getMenuInflater().inflate(R.menu.navigation, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu, menu);
        return true;
    }

    // 사용자가 선택한 메뉴 항목을 처리
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent = null;
        switch(item.getItemId()){
            case R.id.fishsetting:
                intent = new Intent(getApplicationContext(), CheckActivity.class);
                break;
            case R.id.developers:
                Toast.makeText(getApplicationContext(),"팀장 장소현, 팀원 이정민, 이혜수",Toast.LENGTH_SHORT).show();
                break;
            case R.id.fishRecord:
                intent = new Intent(getApplicationContext(), RecordActivity.class);
                break;
        }
        if(intent != null){
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    // activity_navigation_drawer.xml
    @SuppressWarnings("StatementWithEmptyBody")
    //네비게이션바 메뉴 선택
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;
        if (id == R.id.nav_camera)
        {
            intent = new Intent(getApplicationContext(), TotalActivity.class);
            intent.putExtra("tempList",tempList);
            intent.putExtra("turbList",turbList);
            intent.putExtra("xyList0",xyList0);
            intent.putExtra("xyList1",xyList1);
            intent.putExtra("vidList",vidList);
            intent.putExtra("veloList", veloList);
            intent.putExtra("lidList",lidList);
            intent.putExtra("locList", locList);
        }
        else if (id == R.id.nav_gallery)
        {
            intent = new Intent(getApplicationContext(), PlayActivity.class);
        }
        else if (id == R.id.nav_slideshow)
        {
            intent = new Intent(getApplicationContext(), DssActivity.class);
            intent.putExtra("xyList0",xyList0);
            intent.putExtra("xyList1",xyList1);
        }
        else if (id == R.id.nav_manage)
        {
            intent = new Intent(getApplicationContext(), TurbActivity.class);
            intent.putExtra("tempList",tempList);
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClick(View view)  // Activity 전환
    {
        Intent intent = null;
        switch (view.getId()){
            case R.id.button1:      //통합 모니터링 버튼을 클릭하였을 경우
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
            case R.id.button2:      //실시간 어항 감상 버튼을 클릭하였을 경우
                intent = new Intent(getApplicationContext(), PlayActivity.class);
                break;
            case R.id.button3:      //물고기 움직임 조회 버튼을 클릭하였을 경우
                intent = new Intent(getApplicationContext(), DssActivity.class);
                intent.putExtra("xyList0",xyList0);
                intent.putExtra("xyList1",xyList1);
                break;
            case R.id.button4:      //어항 환경 조회 버튼을 클릭하였을 경우
                intent = new Intent(getApplicationContext(), TurbActivity.class);
                intent.putExtra("turbList",turbList);
                intent.putExtra("tempList",tempList);
                break;
        }
        if(intent != null){
            startActivity(intent);
        }
    }

    //서버에서 데이터를 받아오는 내부클래스
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

    //각 데이터 리스트에 저장
    private void showResult()
    {
        String TAG_JSON="result";
        String TAG_TEMP = "temp";
        String TAG_TURB ="tubi";
        String TAG_X = "curCx";
        String TAG_Y = "curCy";
        String TAG_ID = "id";
        String TAG_VELO = "velo";
        String TAG_LOC = "loc";

        try
        {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            int j=0;
            Log.d("dress",jsonArray.getJSONObject(0).toString());
            if (jsonArray.getJSONObject(0).has(TAG_TEMP)) {
                for (int i = (jsonArray.length() - 20); i <= (jsonArray.length() - 1); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    String temp = item.getString(TAG_TEMP);
                    String turb = item.getString(TAG_TURB);
                    j++;

                    float temp2 = Float.parseFloat(temp);
                    float turb2 = Float.parseFloat(turb);
                    int temp3 = (int) (temp2 * 10);
                    int turb3 = (int) (turb2 * 100);

                    tempList.add(new Entry(j, temp3));
                    turbList.add(new Entry(j, turb3));
                    Log.d("temp", String.valueOf(temp3));
                    Log.d("turb", String.valueOf(turb3));
                    Log.d("ivalue", String.valueOf(j));
                }
            } else if (jsonArray.getJSONObject(0).has(TAG_X)&&fish==0) {
                for (int i = jsonArray.length() - 1000; i <= (jsonArray.length() - 1); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    String x = item.getString(TAG_X);
                    String y = item.getString(TAG_Y);

                    int x2 = Integer.parseInt(x);
                    int y2 = Integer.parseInt(y);

                    xyList0.add(new Entry(x2, y2));
                    Log.d("berry's value", String.valueOf(x2));
                }
                fish = 1;
            } else if (jsonArray.getJSONObject(0).has(TAG_X)&&fish==1) {
                for(int i=jsonArray.length()-1000;i<=(jsonArray.length()-1);i++)
                {
                    JSONObject item = jsonArray.getJSONObject(i);
                    String x = item.getString(TAG_X);
                    String y = item.getString(TAG_Y);

                    int x2 = Integer.parseInt(x);
                    int y2 = Integer.parseInt(y);

                    xyList1.add(new Entry(x2,y2));
                    Log.d("nilla's value",String.valueOf(i));
                }
                fish = 0;
            } else if (jsonArray.getJSONObject(0).has(TAG_VELO)){
                for(int i=(jsonArray.length()-2);i<=(jsonArray.length()-1);i++)
                {
                    JSONObject item = jsonArray.getJSONObject(i);

                    int id = item.getInt(TAG_ID);
                    String velo = item.getString(TAG_VELO);

                    int vid = id;
                    String velo1 = velo;

                    vidList.add(vid);
                    veloList.add(velo1);
                    Log.d("velo",String.valueOf(velo1));
                }
            } else if (jsonArray.getJSONObject(0).has(TAG_LOC)) {
                for(int i=(jsonArray.length()-2);i<=(jsonArray.length()-1);i++)
                {
                    JSONObject item = jsonArray.getJSONObject(i);

                    int id = item.getInt(TAG_ID);
                    String loc = item.getString(TAG_LOC);

                    int lid = id;
                    String loc1 = loc;

                    lidList.add(lid);
                    locList.add(loc1);
                }
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}
