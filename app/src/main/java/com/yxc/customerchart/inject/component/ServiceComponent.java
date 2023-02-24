package com.yxc.customerchart.inject.component;

import android.app.Service;


import com.yxc.customerchart.inject.modules.ServiceModule;
import com.yxc.customerchart.inject.others.PerService;
import com.yxc.customerchart.service.DownLoadService;

import dagger.Component;

/**
 * author：ajiang
 * mail：1025065158@qq.com
 * blog：http://blog.csdn.net/qqyanjiang
 */
@PerService
@Component(dependencies = AppComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {
    Service getService();

    void inject(DownLoadService downLoadService);
}
