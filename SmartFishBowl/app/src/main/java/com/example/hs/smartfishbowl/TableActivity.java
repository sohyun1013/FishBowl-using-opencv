package com.example.hs.smartfishbowl;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

//서버에서 받아온 데이터를 리스트로 표현하기위해
public class TableActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_table);

        Intent intent = getIntent();
        ArrayList<Entry> tempList = intent.getParcelableArrayListExtra("tempList");
        ArrayList<Entry> turbList = intent.getParcelableArrayListExtra("turbList");

        if(tempList != null){
            String[] values= new String[tempList.size()];

            for(int i=0; i<tempList.size();i++){
              String num = Float.toString(tempList.get(i).getY()/10);
               Log.d("string",num);
                values[i] = new String(num);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
            setListAdapter(adapter);
            tempList.clear();
        } else if(turbList!=null){
            String[] values= new String[turbList.size()];

            for(int i=0; i<turbList.size();i++){
                String num = Float.toString(turbList.get(i).getY()/100);
                Log.d("string",num);
                values[i] = new String(num);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
            setListAdapter(adapter);
            turbList.clear();
        }
    }
}
