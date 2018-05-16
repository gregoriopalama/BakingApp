package com.gregoriopalama.udacity.bakingapp.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.google.gson.GsonBuilder;
import com.gregoriopalama.udacity.bakingapp.Constants;
import com.gregoriopalama.udacity.bakingapp.R;
import com.gregoriopalama.udacity.bakingapp.di.Injectable;
import com.gregoriopalama.udacity.bakingapp.model.Ingredient;
import com.gregoriopalama.udacity.bakingapp.model.Recipe;
import com.gregoriopalama.udacity.bakingapp.service.RecipeService;
import com.gregoriopalama.udacity.bakingapp.ui.list.MainActivity;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.gregoriopalama.udacity.bakingapp.Constants.EXTRA_WIDGET_ID;
import static com.gregoriopalama.udacity.bakingapp.Constants.EXTRA_WIDGET_INGREDIENTS;
import static com.gregoriopalama.udacity.bakingapp.Constants.EXTRA_WIDGET_REQUESTED_RECIPE;

/**
 * Widget that shows the list of ingredients of a given recipe
 *
 * @author Gregorio Palamà
 */
public class RecipeIngredientsWidget extends AppWidgetProvider implements Injectable {

    static RecipeService getRecipeService() {
        Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.RECIPE_SERVICE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                            .create()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory
                            .createWithScheduler(Schedulers.io()))
                    .build();

        return retrofit.create(RecipeService.class);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        CharSequence recipeName = RecipeIngredientsWidgetConfigureActivity
                .loadRecipePreference(context, appWidgetId);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredients_widget);
        views.setTextViewText(R.id.recipe_name,
                String.format(context.getString(R.string.widget_ingredient_title), recipeName));
        views.setEmptyView(R.id.recipe_list, R.id.loading);

        Intent listIntent = new Intent(context, MainActivity.class);
        PendingIntent listPendingIntent = PendingIntent
                .getActivity(context, 0, listIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.recipe_list, listPendingIntent);

        Intent titleIntent = new Intent(context, MainActivity.class);
        titleIntent.putExtra(EXTRA_WIDGET_REQUESTED_RECIPE, recipeName);
        PendingIntent titlePendingIntent = PendingIntent
                .getActivity(context, 0, titleIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.recipe_name, titlePendingIntent);

        getRecipeService().getRecipes().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableObserver<List<Recipe>>() {

                @Override
                public void onNext(List<Recipe> results) {
                    if (results.size() == 0)
                        return;

                    Intent intent = new Intent(context, RecipeWidgetService.class);
                    for (Recipe recipe : results) {
                        if (recipe.getName().equals(recipeName)) {
                            ArrayList<String> ingredienti = new ArrayList<>();
                            for (Ingredient ingredient : recipe.getIngredients()) {
                                ingredienti.add(
                                        String.format(context.getString(R.string.widget_ingredient),
                                                ingredient.getName(),
                                                Double.toString(ingredient.getQuantity()),
                                                ingredient.getMeasure()));
                            }
                            intent.putExtra(EXTRA_WIDGET_INGREDIENTS, ingredienti);
                        }
                    }
                    intent.putExtra(EXTRA_WIDGET_ID, appWidgetId);
                    views.setRemoteAdapter(R.id.recipe_list, intent);
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.recipe_list);
                    appWidgetManager.updateAppWidget(appWidgetId, views);
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onComplete() {

                }
            });
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RecipeIngredientsWidgetConfigureActivity.deleteRecipePreference(context, appWidgetId);
        }
    }
}

