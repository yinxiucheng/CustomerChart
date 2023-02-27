package com.yxc.customerchart.stock;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.yxc.customerchart.R;
import com.yxc.customerchart.base.BaseFragment;
import com.yxc.mylibrary.TimeDateUtil;

import org.joda.time.LocalDate;


/**
 * 心电图
 */
public class KLineLandActivity extends AppCompatActivity {

    Toolbar toolbar;
    FrameLayout container;
    private BaseFragment currentFragment;
    private TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kline_land);
        initView();
    }

    private void initView() {
        container = findViewById(R.id.container);
        switchTab(ChartKLineFragment.class, "ChartKLineFragment");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void switchTab(Class clz, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ChartKLineFragment fragment = (ChartKLineFragment) getSupportFragmentManager().findFragmentByTag(tag);
        if (currentFragment != null) {
            ft.hide(currentFragment);
        }
        if (fragment == null) {
            fragment = ChartKLineFragment.newInstance(1, true);
            ft.add(R.id.container, fragment, tag);
        } else {
            ft.show(fragment);
        }
        ft.commitAllowingStateLoss();
        currentFragment = fragment;
    }
}
