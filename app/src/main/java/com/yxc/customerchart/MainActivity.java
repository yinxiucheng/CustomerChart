package com.yxc.customerchart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
public class MainActivity extends BaseActivity {
    @Bind(R.id.btn)
    Button btn;
    @Bind(R.id.btn_k)
    Button btnK;
    @Bind(R.id.btn_fix)
    Button btnFix;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MinutesActivity.class);
                startActivity(intent);
            }
        });

        btnK = findViewById(R.id.btn_k);
        btnK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentK = new Intent(MainActivity.this, KLineActivity.class);
                startActivity(intentK);
            }
        });
    }
}
