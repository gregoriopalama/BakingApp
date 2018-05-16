package com.gregoriopalama.udacity.bakingapp.di;

import com.gregoriopalama.udacity.bakingapp.BakingApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Dagger component for the whole Application
 *
 * @author Gregorio Palamà
 */

@Singleton
@Component(modules = {AppModule.class,
        AndroidSupportInjectionModule.class,
        ActivitiesModule.class})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(BakingApplication application);
        AppComponent build();
    }
    void inject(BakingApplication application);
    BindingComponent bindingComponent();
}
