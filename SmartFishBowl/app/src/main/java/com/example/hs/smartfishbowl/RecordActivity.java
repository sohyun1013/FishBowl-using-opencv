package com.example.hs.smartfishbowl;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class RecordActivity extends AppCompatActivity {
    TextView berryVelo;
    TextView nillaVelo;
    TextView berryDate;
    TextView nillaDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        setTitle("물고기 이력");

        berryVelo = (TextView)findViewById(R.id.berryVelo);
        nillaVelo = (TextView)findViewById(R.id.nillaVelo);
        berryDate = (TextView)findViewById(R.id.berryDate);
        nillaDate = (TextView)findViewById(R.id.nillaDate);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

}
