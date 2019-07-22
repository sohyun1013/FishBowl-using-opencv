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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

//온도를 시각화한 액티비티(아마 사용안하는 액티비티일듯)
public class TempActivity extends AppCompatActivity {
    private LineChart lineChart;
    ArrayList<Entry> tempList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        //setTitle("수온 상태 조회");

        TextView tempalarm = (TextView)findViewById(R.id.tempalarm);
        ImageView therImage = (ImageView)findViewById(R.id.therImage);

        lineChart = (LineChart)findViewById(R.id.chart);

        Intent intent = getIntent();
        tempList = intent.getParcelableArrayListExtra("tempList");

        LineDataSet lineDataSet = new LineDataSet(tempList, "수온");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
        //lineDataSet.setCircleColorHole(Color.BLUE);
        lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(8, 24, 0);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);
        yLAxis.setAxisMaximum(270f);
        yLAxis.setAxisMinimum(220f);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        //Description description = new Description();
        //description.setText("");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        //lineChart.setDescription(description);
        //lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
        lineChart.invalidate();


        //int value = Integer.parseInt(tempList.get(1).toString());
        Log.d("value", String.valueOf(tempList.get(1)));
        float value = tempList.get(1).getY();
        if(value>=230){
            Log.d("value","HOT");
            tempalarm.setText("현재 수온 높음");
            therImage.setImageResource(R.drawable.thermometerr);
        } else if (value>=200){
            Log.d("value","SOSO");
            tempalarm.setText("현재 수온 조금 높음");
            therImage.setImageResource(R.drawable.thermometery);
        } else {
            Log.d("value","COOL");
            tempalarm.setText("현재 수온 적정");
            therImage.setImageResource(R.drawable.thermometerg);
        }
    }

    public void onClick(View view){
        Intent intent = null;
        intent = new Intent(getApplicationContext(), TableActivity.class);
        intent.putExtra("tableList",tempList);
        startActivity(intent);
    }
}
