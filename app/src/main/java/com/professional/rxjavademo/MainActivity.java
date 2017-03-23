package com.professional.rxjavademo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    Button btn_rx_base;
    Button btn_rx_caozuofu;
    Button btn_Flowable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_rx_base = (Button) findViewById(R.id.btn_rx_base);
        btn_rx_caozuofu = (Button) findViewById(R.id.btn_rx_caozuofu);
        btn_Flowable = (Button) findViewById(R.id.btn_Flowable);
        btn_Flowable.setOnClickListener(this);
        btn_rx_base.setOnClickListener(this);
        btn_rx_caozuofu.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){

            case R.id.btn_rx_base:
                intent = new Intent(MainActivity.this,RxbaseActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_rx_caozuofu:
                intent = new Intent(MainActivity.this,FuActivity.class);
                startActivity(intent);
                break;

          case R.id.btn_Flowable:
                intent = new Intent(MainActivity.this,FlowableActivity.class);
                startActivity(intent);
                break;




        }

    }
}
