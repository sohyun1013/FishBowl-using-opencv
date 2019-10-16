package com.example.hs.smartfishbowl;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

//어항 실시간 스트리밍할 수 있는 액티비티
public class PlayActivity extends AppCompatActivity {
    String IP_ADDRESS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        WebView webView = (WebView)findViewById(R.id.webView);
        webView.setPadding(0,0,0,0);

        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        IP_ADDRESS = getString(R.string.ip);
        String url = "http://192.168.41.119:8081";
        webView.loadUrl(url);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
