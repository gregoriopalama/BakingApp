package com.gregoriopalama.udacity.bakingapp.di;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.gregoriopalama.udacity.bakingapp.BakingApplication;

import dagger.android.AndroidInjection;
import dagger.android.support.AndroidSupportInjection;

/**
 * AppInjector that injects the Application and register a callback that listens
 * to all the activities' lifecycles. It injects the activities when they are created
 *
 * @author Gregorio Palamà
 */

public class AppInjector {
    private AppInjector() {
    }

    public static void init(BakingApplication application) {
        AppComponent appComponent = DaggerAppComponent.builder().application(application)
                .build();
        appComponent.inject(application);
        BindingComponent bindingComponent = appComponent.bindingComponent();
        DataBindingUtil.setDefaultComponent(bindingComponent);

        application
                .registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                    @Override
                    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                        injectActivity(activity);
                    }

                    @Override
                    public void onActivityStarted(Activity activity) {

                    }

                    @Override
                    public void onActivityResumed(Activity activity) {

                    }

                    @Override
                    public void onActivityPaused(Activity activity) {

                    }

                    @Override
                    public void onActivityStopped(Activity activity) {

                    }

                    @Override
                    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                    }

                    @Override
                    public void onActivityDestroyed(Activity activity) {

                    }
                });
    }

    private static void injectActivity(Activity activity) {
        if (activity instanceof  Injectable)
            AndroidInjection.inject(activity);

        if (activity instanceof FragmentActivity) {
            ((FragmentActivity) activity).getSupportFragmentManager()
                    .registerFragmentLifecycleCallbacks(
                            new FragmentManager.FragmentLifecycleCallbacks() {
                                @Override
                                public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
                                    super.onFragmentAttached(fm, f, context);
                                    if (f instanceof Injectable) {
                                        AndroidSupportInjection.inject(f);
                                    }
                                }
                            }, true);
        }
    }
}
