package com.gregoriopalama.udacity.bakingapp.di;

import android.app.Application;

import com.gregoriopalama.udacity.bakingapp.BakingApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjectionModule;

/**
 * Dagger module that provides the context
 *
 * @author Gregorio Palamà
 */

@Module(includes = {AndroidInjectionModule.class, ViewModelModule.class})
public class AppModule {

    @Provides
    @Singleton
    Application provideContext(BakingApplication application) {
        return application;
    }
}
