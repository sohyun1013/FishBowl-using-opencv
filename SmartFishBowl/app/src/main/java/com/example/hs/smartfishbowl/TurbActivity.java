package com.example.hs.smartfishbowl;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

//탁도 데이터를 시각화하는 액티비티
public class TurbActivity extends AppCompatActivity {
    private CombinedChart mChart;
    ArrayList<Entry> turbList;
    ArrayList<Entry> tempList;
    ArrayList<Entry> num1=new ArrayList<Entry>();
    ArrayList<Entry> num2=new ArrayList<Entry>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turb);

        //setTitle("수질 상태 조회");
        TextView turbAlarm = (TextView)findViewById(R.id.turbalarm);
        ImageView turbImage = (ImageView)findViewById(R.id.turbImage);
        TextView tempAlarm = (TextView)findViewById(R.id.tempalarm);
        ImageView tempImage = (ImageView)findViewById(R.id.tempImage);


        //lineChart = (LineChart)findViewById(R.id.chart);
        //lineChart2 = (LineChart)findViewById(R.id.chart);

        Intent intent = getIntent();
        turbList = intent.getParcelableArrayListExtra("turbList");
        tempList = intent.getParcelableArrayListExtra("tempList");

        mChart = (CombinedChart)findViewById(R.id.chart);
        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        //dataSets.add(lineDataSet);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        //xAxis.enableGridDashedLine(8, 24, 0);

        YAxis yLAxis = mChart.getAxisLeft();
        yLAxis.setDrawGridLines(false);
        yLAxis.setTextColor(Color.BLUE);
        yLAxis.setAxisMaximum(30f);
        yLAxis.setAxisMinimum(0f);

        YAxis yRAxis = mChart.getAxisRight();
        yRAxis.setDrawGridLines(false);
        yRAxis.setAxisMaximum(5f);
        yRAxis.setAxisMinimum(0f);
        yRAxis.setTextColor(Color.RED);

        CombinedData data = new CombinedData();

        data.setData(generatedLineData());
       // data.setData(generatedLineData2());

        mChart.setData(data);
        //mChart.setDoubleTapToZoomEnabled(false);
        //mChart.setDrawGridBackground(false);
        //lineChart.setDescription(description);
        //lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
        mChart.invalidate();
///////////////////////////////

        float value2 = turbList.get(1).getY();
        if(value2>=300){
            Log.d("value","COOL");
            turbAlarm.setText("현재 수질 좋음");
            turbImage.setImageResource(R.drawable.waterdropg);
        } else if (value2>=150){
            Log.d("value","SOSO");
            turbAlarm.setText("현재 수질 조금 나쁨");
            turbImage.setImageResource(R.drawable.waterdropy);
        } else {
            Log.d("value","HOT");
            turbAlarm.setText("현재 수질 나쁨");
            turbImage.setImageResource(R.drawable.waterdropr);
        }

        float value1 = tempList.get(1).getY();
        if(value1>=270)
        {
            Log.d("value","HOT");
            tempAlarm.setText("현재 수온 높음");
            tempImage.setImageResource(R.drawable.thermometerr);
        }
        else if(value1>=250)
        {
            Log.d("value","SOSO");
            tempAlarm.setText("현재 수온 조금 높음");
            tempImage.setImageResource(R.drawable.thermometery);
        }
        else {
            Log.d("value","COOL");
            tempAlarm.setText("현재 수온 적정");
            tempImage.setImageResource(R.drawable.thermometerg);
        }
    }

    private LineData generatedLineData(){
        LineData d = new LineData();

        Log.d("turbList",String.valueOf(turbList.get(2).getY()));
        for(int i=1; i<20;i++){
            num1.add(new Entry(i,turbList.get(i).getY()/100));
        }

        LineDataSet lineDataSet = new LineDataSet(num1, "수질");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#FF7E7E"));
        lineDataSet.setColor(Color.parseColor("#FF7E7E"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(true);

        ArrayList<Entry> entries = new ArrayList<Entry>();
        Log.d("tempList",String.valueOf(tempList.get(2).getY()));
        for(int i=1; i<20;i++){
            num2.add(new Entry(i,tempList.get(i).getY()/10));
        }

        LineDataSet lineDataSet2 = new LineDataSet(num2, "수온");
        lineDataSet2.setLineWidth(2);
        lineDataSet2.setCircleRadius(6);
        lineDataSet2.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet2.setColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet2.setDrawCircleHole(true);
        lineDataSet2.setDrawCircles(true);
        lineDataSet2.setDrawHorizontalHighlightIndicator(false);
        lineDataSet2.setDrawHighlightIndicators(false);
        lineDataSet2.setDrawValues(true);

        lineDataSet2.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(lineDataSet2);

        lineDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        d.addDataSet(lineDataSet);
        return d;
    }


}