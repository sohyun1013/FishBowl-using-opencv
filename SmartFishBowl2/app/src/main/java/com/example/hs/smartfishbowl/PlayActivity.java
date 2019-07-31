package com.example.hs.smartfishbowl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

//어항 실시간 스트리밍할 수 있는 액티비티
public class PlayActivity extends AppCompatActivity {

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

        String url = "http://IP:8081";
        webView.loadUrl(url);
    }
}
