package com.example.hs.smartfishbowl;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;

import org.w3c.dom.Text;

import java.util.ArrayList;

//통합 모니터링할 수 있는 액티비티
public class TotalActivity extends AppCompatActivity {
    ArrayList<Entry> tempList;
    ArrayList<Entry> turbList;
    ArrayList<Entry> xyList0;
    ArrayList<Entry> xyList1;
    ArrayList vidList;
    ArrayList veloList;
    ArrayList lidList;
    ArrayList locList;
    String veloValue;
    String locValue;
    boolean checkState0;
    boolean checkState1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total);
        setTitle("통합 모니터링");

        StringBuffer inform = new StringBuffer();
        StringBuffer inform2 = new StringBuffer();
        StringBuffer inform_state = new StringBuffer();
        StringBuffer inform_state2 = new StringBuffer();

        TextView tempA = (TextView)findViewById(R.id.tempA);
        TextView turbA = (TextView)findViewById(R.id.turbA);
        TextView moveA = (TextView)findViewById(R.id.moveA);
        TextView moveB = (TextView)findViewById(R.id.moveB);
        TextView conditionA = (TextView)findViewById(R.id.conditionA);
        TextView conditionB = (TextView)findViewById(R.id.conditionB);

        ImageView tempImg = (ImageView)findViewById(R.id.tempImg);
        ImageView turbImg = (ImageView)findViewById(R.id.turbImg);
        ImageView moveImg = (ImageView)findViewById(R.id.moveImg);
        ImageView moveImg2 = (ImageView)findViewById(R.id.moveImg2);
        //ImageView faceImg = (ImageView)findViewById(R.id.face);

        LinearLayout button1 = (LinearLayout) findViewById(R.id.button1);
        LinearLayout button2 = (LinearLayout) findViewById(R.id.button2);
        LinearLayout button3 = (LinearLayout) findViewById(R.id.button3);
        LinearLayout button4 = (LinearLayout) findViewById(R.id.button4);

        Intent intent = getIntent();
        tempList = intent.getParcelableArrayListExtra("tempList");
        turbList = intent.getParcelableArrayListExtra("turbList");
        xyList0 = intent.getParcelableArrayListExtra("xyList0");
        xyList1 = intent.getParcelableArrayListExtra("xyList1");
        vidList = intent.getParcelableArrayListExtra("vidList");
        veloList = intent.getParcelableArrayListExtra("veloList");
        lidList = intent.getParcelableArrayListExtra("lidList");
        locList = intent.getParcelableArrayListExtra("locList");

        /*
        textView
        현재 상태는 ___________ 입니다. 더보기 => 클릭하게 되면 새로운 인텐트, 각 상태에 대한 설명만 보여줄 수 있도록 경우 나누기
        속도 계산
        * 가만히 있는 경우 : 사망이나 세균성 질병에 대한 설명 필요(세균성 장염)
        * 속도가 정상
        * 속도가 느린 경우 : 솔방울병, 바이러스성 질병(감기), 소과충병?
        * 속도가 빠른경우 : 기생충성 질병

        * 전염성 질병일 경우 전염성 질병에 대한 설명도 덧붙일 것

        * 좌표가 위에 존재하는 경우 각 시간대의 평균과 지금까지 모았던 데이터를 비교해서 나타냄 - 산소부족, 부레병 => 해결 방법 위주로 설명 덧붙이기
        */

        System.out.println(vidList);
        // conditionA에 텍스트뷰로 setText
        for(int i = 0; i < vidList.size(); i++)
        {
            veloValue = veloList.get(i).toString();
            String name = null;
            if(i == 0)
            {
                name = "베리";
            }
            else
            {
                name = "닐라";
            }

            // 속도에 따른 텍스트뷰
            if(veloValue.equals("slow"))
            {
                if(i == 0)
                {
                    inform_state.append(name+"는 속도가 느리거나 움직이지 않습니다. 솔방울병, 세균성 질병, 바이러스성 질병이 의심됩니다.");
                    checkState0 = true;
                }
                else
                {
                    inform_state2.append(name+"는 속도가 느리거나 움직이지 않습니다. 솔방울병, 세균성 질병, 바이러스성 질병이 의심됩니다.");
                    checkState1 = true;
                }
            }
            else if(veloValue.equals("fast"))
            {
                if(i == 0)
                {
                    inform_state.append(name+"속도가 빠릅니다. 기생충성 질병이 의심됩니다.");
                    checkState0 = true;
                }
                else
                {
                    inform_state2.append(name+"속도가 빠릅니다. 기생충성 질병이 의심됩니다.");
                    checkState1 = true;
                }
            }

            // 물고기 위치에 따른 텍스트뷰
            locValue = locList.get(i).toString();
            if(locValue.equals("top"))
            {
                if(i == 0)
                {
                    inform_state.append(name+"물고기가 수면에 위치해있습니다. 산소부족, 부레병이 의심됩니다.");
                    checkState0 = true;
                }
                else
                {
                    inform_state2.append(name+"물고기가 수면에 위치해있습니다. 산소부족, 부레병이 의심됩니다.");
                    checkState1 = true;
                }
            }
            /*else {
                if (i==0) {
                    inform_state.append(name+"물고기가 정상적으로 움직입니다.");
                } else {
                    inform_state2.append(name+"물고기가 정상적으로 움직입니다.");
                }
            }*/


            if (!checkState0 && i==0)
            {
                inform_state.append(name+"속도가 정상입니다.");
            }
            if (!checkState1 && i==1)
            {
                inform_state2.append(name+"속도가 정상입니다.");
            }

        }

        // conditionB에 텍스트뷰로 setText
        // 온도에 따른 텍스트뷰
        Log.d("value1", String.valueOf(tempList.get(1)));
        float value1 = tempList.get(1).getY();
        if(value1>=270)
        {
            Log.d("value","HOT");
            tempA.setText("현재 수온 높음");
            inform.append("수온 조절이 즉시 필요합니다.\n");
            tempImg.setImageResource(R.drawable.thermometerr);
        }
        else if(value1>=250)
        {
            Log.d("value","SOSO");
            tempA.setText("현재 수온 조금 높음");
            inform.append("수온 조절이 필요합니다.\n");
            tempImg.setImageResource(R.drawable.thermometery);
        }
        else {
            Log.d("value","COOL");
            tempA.setText("현재 수온 적정");
            tempImg.setImageResource(R.drawable.thermometerg);
        }

        // 탁도에 따른 텍스트뷰
        Log.d("tvalue", String.valueOf(turbList.get(1)));
        float value2 = turbList.get(1).getY();
        if(value2>=300){
            Log.d("value","COOL");
            turbA.setText("현재 수질 좋음");
            turbImg.setImageResource(R.drawable.waterdropg);
        }
        else if (value2>=150){
            Log.d("value","SOSO");
            turbA.setText("현재 수질 조금 나쁨");
            inform.append("수질 관리가 필요합니다.\n");
            turbImg.setImageResource(R.drawable.waterdropy);
        }
        else {
            Log.d("value","HOT");
            turbA.setText("현재 수질 나쁨");
            turbImg.setImageResource(R.drawable.waterdropr);
            inform.append("수질 관리가 즉시 필요합니다.\n");
        }

        if(checkState0)
        {
            moveA.setText("베리 이상 움직임 감지");
            moveImg.setImageResource(R.drawable.alarmr);
            //faceImg.setImageResource(R.drawable.sad);
        } else {
            Log.d("value","COOL");
            moveA.setText("움직임 정상");
            moveImg.setImageResource(R.drawable.alarmg);
        }
        if (checkState1){
            moveB.setText("닐라 이상 움직임 감지");
            moveImg2.setImageResource(R.drawable.alarmr);
        } else {
            moveB.setText("움직임 정상");
            moveImg2.setImageResource(R.drawable.alarmg);
        }

        conditionA.setText(inform_state);
        conditionB.setText(inform_state2);
    }

    public void onClick(View view) {         //Activity 전환
        Intent intent = null;
        //GetData task = new GetData();
        //task.execute( "http://" + IP_ADDRESS + "/sensor_db.php", "");
        switch (view.getId()) {
            case R.id.conditionA: // 이상증세에 대한 전체적인 설명 기재
                intent = new Intent(getApplicationContext(), ExplainActivity.class);
                intent.putExtra("veloList", veloList);
                intent.putExtra("locList", locList);
                break;
            case R.id.conditionB: // 이상증세에 대한 전체적인 설명 기재
                intent = new Intent(getApplicationContext(), ExplainActivity.class);
                intent.putExtra("veloList", veloList);
                intent.putExtra("locList", locList);
                break;
            case R.id.button1:
                intent = new Intent(getApplicationContext(), DssActivity.class);
                intent.putExtra("xyList0", xyList0);
                intent.putExtra("xyList1", xyList1);
                break;
            case R.id.button2:
                intent = new Intent(getApplicationContext(), DssActivity.class);
                intent.putExtra("xyList0", xyList0);
                intent.putExtra("xyList1", xyList1);
                break;
            case R.id.button3:
                intent = new Intent(getApplicationContext(), TableActivity.class);
                intent.putExtra("tempList", tempList);
                break;
            case R.id.button4:
                intent = new Intent(getApplicationContext(), TableActivity.class);
                intent.putExtra("turbList", turbList);
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
}
