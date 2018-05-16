package com.gregoriopalama.udacity.bakingapp.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gregoriopalama.udacity.bakingapp.Constants;
import com.gregoriopalama.udacity.bakingapp.service.RecipeService;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Dagger module that provides Retrofit and the Recipe API consumer
 *
 * @author Gregorio Palamà
 */

@Module
public class RecipeServiceModule {

    @Singleton
    @Provides
    Gson providesGson() {
        return  new GsonBuilder()
                .create();
    }

    @Singleton
    @Provides
    Retrofit providesRetrofit(Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(Constants.RECIPE_SERVICE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory
                        .createWithScheduler(Schedulers.io()))
                .build();
    }

    @Singleton
    @Provides
    RecipeService providesRecipeService(Retrofit retrofit) {
        return retrofit.create(RecipeService.class);
    }
}
