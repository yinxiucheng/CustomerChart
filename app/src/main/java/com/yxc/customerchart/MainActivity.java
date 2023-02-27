package com.yxc.customerchart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.yxc.customerchart.stock.KLineLandActivity;
import com.yxc.customerchart.ui.EcgLandScapeActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mBinding = ActivityMainBinding.inflate(getLayoutInflater());

        findViewById(R.id.kline_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "KLine Chart Click", Toast.LENGTH_SHORT).show();
                Intent intentK = new Intent(MainActivity.this, KLineLandActivity.class);
                startActivity(intentK);
            }
        });

        findViewById(R.id.hrm_chart_landscape).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "心电图 Click", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, EcgLandScapeActivity.class);
                startActivity(intent);
            }
        });
    }
}
