package com.gregoriopalama.udacity.bakingapp;

import android.app.Activity;
import android.app.Application;

import com.gregoriopalama.udacity.bakingapp.di.AppInjector;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * The Baking App application
 *
 * @author Gregorio Palamà
 */

public class BakingApplication extends Application  implements HasActivityInjector {
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        AppInjector.init(this);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
