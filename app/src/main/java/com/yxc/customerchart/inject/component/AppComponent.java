package com.yxc.customerchart.inject.component;

import android.content.Context;
import android.content.SharedPreferences;


import com.yxc.customerchart.api.ClientApi;
import com.yxc.customerchart.api.DownLoadApi;
import com.yxc.customerchart.application.App;
import com.yxc.customerchart.inject.modules.AppModule;
import com.yxc.customerchart.inject.modules.ClientApiModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ClientApiModule.class})
public interface AppComponent {
    Context context();

    ClientApi clientApi();

    DownLoadApi downLoadApi();
    SharedPreferences sharedPreferences();

    void inject(App application);
}
