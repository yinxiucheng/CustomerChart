package com.yxc.customerchart.ui.ecg;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.yxc.customerchart.R;
import com.yxc.customerchart.base.BaseChartFragment;
import com.yxc.customerchart.ui.ecg.EcgDayFragment;


/**
 * 心电图
 */
public class EcgLandScapeActivity extends AppCompatActivity {

    Toolbar toolbar;
    FrameLayout container;
    private BaseChartFragment currentFragment;
    private TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hrm);
        initView();
    }

    private void initView() {
        container = findViewById(R.id.container);
        switchTab(EcgDayFragment.class, "EcgDayFragment");
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
        BaseChartFragment fragment = (BaseChartFragment) getSupportFragmentManager().findFragmentByTag(tag);
        if (currentFragment != null) {
            currentFragment.resetSelectedEntry();
            ft.hide(currentFragment);
        }
        if (fragment == null) {
            try {
                fragment = (BaseChartFragment) clz.newInstance();
                ft.add(R.id.container, fragment, tag);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        } else {
            ft.show(fragment);
        }
        ft.commitAllowingStateLoss();
        currentFragment = fragment;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (currentFragment != null) {
            currentFragment.resetSelectedEntry();
        }
    }
}
