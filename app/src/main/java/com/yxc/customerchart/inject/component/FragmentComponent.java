package com.yxc.customerchart.inject.component;

import android.app.Activity;

import com.yxc.customerchart.inject.modules.FragmentModule;
import com.yxc.customerchart.inject.others.PerFragment;

import dagger.Component;

@PerFragment
@Component(modules = FragmentModule.class, dependencies = AppComponent.class)
public interface FragmentComponent {

    Activity getActivity();

  //  void inject(BaseFragment mBaseFragment);


}
