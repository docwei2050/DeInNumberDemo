package com.xxx.mall.deinbuttondemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InDeNumberButton btn=findViewById(R.id.btn);
        btn.setInitValue(3).setMaxValue(1000).setDialogActivity(this);
    }
}
