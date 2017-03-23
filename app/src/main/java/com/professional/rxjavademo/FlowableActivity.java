package com.professional.rxjavademo;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class FlowableActivity extends AppCompatActivity {


    WebView myWebView;
    Button btn_f1;
    Button btn_f2;
    Button btn_f3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowable);
        myWebView = (WebView) findViewById(R.id.webview);
        btn_f1 = (Button) findViewById(R.id.btn_f1);
        btn_f2 = (Button) findViewById(R.id.btn_f2);
        btn_f3 = (Button) findViewById(R.id.btn_f3);


        initweb();

    }

    public void initweb(){

        WebSettings settings = myWebView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true); // 支持缩放
        settings.setJavaScriptEnabled(true); // 启用JS脚本
        myWebView.requestFocus();

        btn_f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myWebView.loadUrl("http://www.jianshu.com/p/9b1304435564");
            }
        });
        btn_f2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myWebView.loadUrl("http://www.jianshu.com/p/a75ecf461e02");
            }
        });
        btn_f3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myWebView.loadUrl("http://www.jianshu.com/u/c50b715ccaeb");
            }
        });

    }




}
