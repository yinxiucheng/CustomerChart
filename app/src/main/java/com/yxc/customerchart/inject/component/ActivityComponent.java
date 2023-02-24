package com.yxc.customerchart.inject.component;

import android.app.Activity;

//import com.example.yanjiang.stockchart.inject.modules.ActivityModule;
//import com.example.yanjiang.stockchart.inject.others.PerActivity;
import com.yxc.customerchart.BaseActivity;
import com.yxc.customerchart.inject.modules.ActivityModule;
import com.yxc.customerchart.inject.others.PerActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Activity getActivityContext();

    void inject(BaseActivity mBaseActivity);



}
