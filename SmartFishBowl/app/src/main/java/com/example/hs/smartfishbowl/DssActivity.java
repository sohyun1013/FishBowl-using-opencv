package com.example.hs.smartfishbowl;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

//Desease Activity 이상증세(움직임)을 조회할 수 있는 액티비티
public class DssActivity extends AppCompatActivity {
    private ScatterChart chart;
    private ScatterChart chart2;
    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY;
    ArrayList<Entry> values1;
    ArrayList<Entry> xyList0;
    ArrayList<Entry> xyList1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dss);

        setTitle("이상 증세 조회");
        //TextView dssA = (TextView)findViewById(R.id.dssA);
        //ImageView dssImg = (ImageView)findViewById(R.id.dssImg);

        Intent intent = getIntent();
        xyList0 = intent.getParcelableArrayListExtra("xyList0");
        xyList1 = intent.getParcelableArrayListExtra("xyList1");

        ScatterDataSet set1 = new ScatterDataSet(xyList0, "pos");
        set1.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        set1.setColor(ColorTemplate.COLORFUL_COLORS[1]);

        set1.setScatterShapeSize(30f);
        ArrayList<IScatterDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets

        ScatterDataSet set2 = new ScatterDataSet(xyList1, "pos");
        set2.setScatterShape(ScatterChart.ScatterShape.CIRCLE);
        set2.setColor(ColorTemplate.COLORFUL_COLORS[2]);

        set2.setScatterShapeSize(30f);
        ArrayList<IScatterDataSet> dataSets2 = new ArrayList<>();
        dataSets2.add(set2); // add the data sets

        chart = findViewById(R.id.chart);
        chart.getDescription().setEnabled(false);

        chart.setDrawGridBackground(true);
        chart.setTouchEnabled(true);
        chart.setMaxHighlightDistance(100f);

        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        chart.setMaxVisibleValueCount(100);
        chart.setPinchZoom(true);

        Legend l = chart.getLegend();

        chart2 = findViewById(R.id.chart2);
        chart2.getDescription().setEnabled(false);

        chart2.setDrawGridBackground(true);
        chart2.setTouchEnabled(true);
        chart2.setMaxHighlightDistance(100f);

        chart2.setDragEnabled(true);
        chart2.setScaleEnabled(true);

        chart2.setMaxVisibleValueCount(100);
        chart2.setPinchZoom(true);

        Legend l2 = chart2.getLegend();

        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        //l.setTypeface(tfLight);
        l.setXOffset(5f);

        l2.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l2.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l2.setOrientation(Legend.LegendOrientation.VERTICAL);
        l2.setDrawInside(false);
        //l.setTypeface(tfLight);
        l2.setXOffset(5f);

        YAxis yl = chart.getAxisLeft();
        //yl.setTypeface(tfLight);
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        yl.setAxisMaximum(800f);

        chart.getAxisRight().setEnabled(false);

        XAxis xl = chart.getXAxis();
        //xl.setTypeface(tfLight);
        xl.setDrawGridLines(false);
        xl.setAxisMaximum(1200f);
        xl.setAxisMinimum(0f);

        YAxis yl2 = chart2.getAxisLeft();
        //yl.setTypeface(tfLight);
        yl2.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        yl2.setAxisMaximum(800f);

        chart2.getAxisRight().setEnabled(false);

        XAxis xl2 = chart2.getXAxis();
        //xl.setTypeface(tfLight);
        xl2.setDrawGridLines(false);
        xl2.setAxisMaximum(1200f);
        xl2.setAxisMinimum(0f);

        ScatterData data = new ScatterData(dataSets);
        chart.setData(data);
        chart.invalidate();
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);

        ScatterData data2 = new ScatterData(dataSets2);
        chart2.setData(data2);
        chart2.invalidate();
        chart2.setPinchZoom(false);
        chart2.setDoubleTapToZoomEnabled(false);
        chart2.setTouchEnabled(true);
        chart2.setDragEnabled(true);

//        Log.d("tvalue", String.valueOf(values1.get(1)));
        //float value2 = values1.get(1).getY();
        /*int num=0;
        //int num2=0;
        for(int i=0; i<xyList.size();i++){
            if(xyList.get(i).getY()>=600){
                num+=1;
                Log.d("movemove","move");
            }
        }

        if(num>=600){
            Log.d("value","COOL");
            dssA.setText("위험 움직임 감지");
            dssImg.setImageResource(R.drawable.alarmr);
        } else if(num>=400){
            Log.d("value","COOL");
            dssA.setText("이상 움직임 감지");
            dssImg.setImageResource(R.drawable.alarmy);
        }else {
            Log.d("value","SOSO");
            dssA.setText("움직임 이상 없음");
            dssImg.setImageResource(R.drawable.alarmg);
        }*/
    }
/*
    public void onClick(View view){
        Intent intent = null;
        intent = new Intent(getApplicationContext(), TableActivity.class);
        intent.putExtra("tableList",xyList0);
        startActivity(intent);
    }*/
}