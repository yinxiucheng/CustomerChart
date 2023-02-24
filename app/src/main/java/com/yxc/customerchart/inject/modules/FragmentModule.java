package com.yxc.customerchart.inject.modules;

import android.app.Activity;


import androidx.fragment.app.Fragment;

import com.yxc.customerchart.inject.others.PerFragment;

import dagger.Module;
import dagger.Provides;

@Module
public class FragmentModule {
    private Fragment mFragment;

    public FragmentModule(Fragment fragment) {
        mFragment = fragment;
    }

    @Provides
    @PerFragment
    public Activity provideActivity() {
        return mFragment.getActivity();
    }
}
