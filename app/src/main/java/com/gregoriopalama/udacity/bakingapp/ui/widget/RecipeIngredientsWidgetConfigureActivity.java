package com.gregoriopalama.udacity.bakingapp.ui.widget;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.gregoriopalama.udacity.bakingapp.R;
import com.gregoriopalama.udacity.bakingapp.databinding.RecipeIngredientsWidgetConfigureBinding;
import com.gregoriopalama.udacity.bakingapp.di.Injectable;
import com.gregoriopalama.udacity.bakingapp.model.Recipe;
import com.gregoriopalama.udacity.bakingapp.viewmodel.RecipesListViewModel;
import com.gregoriopalama.udacity.bakingapp.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Configuration for the widget
 *
 * @see  RecipeIngredientsWidget
 * @author Gregorio Palamà
 */
public class RecipeIngredientsWidgetConfigureActivity extends AppCompatActivity implements Injectable {
    private static final String PREFS_NAME =
            "com.gregoriopalama.udacity.bakingapp.ui.widget.RecipeIngredientsWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    RecipeIngredientsWidgetConfigureBinding binding;
    ArrayAdapter<String> adapter;

    @Inject
    ViewModelFactory viewModelFactory;
    RecipesListViewModel recipesListViewModel;

    int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    public RecipeIngredientsWidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        binding = DataBindingUtil.setContentView(this, R.layout.recipe_ingredients_widget_configure);

        recipesListViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(RecipesListViewModel.class);
        recipesListViewModel.getObservableRecipes().observe(this, (recipes) -> {
            List<String> recipeList = new ArrayList<>();
            for (Recipe recipe: recipes) {
                recipeList.add(recipe.getName());
            }
            adapter = new ArrayAdapter<>(this,
                    R.layout.spinner_list_item, recipeList);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            binding.recipeChoose.setAdapter(adapter);

            String storedValue = loadRecipePreference(
                    RecipeIngredientsWidgetConfigureActivity.this, appWidgetId);
            if (storedValue != null) {
                binding.recipeChoose.setSelection(adapter.getPosition(storedValue));
            }
        });
        recipesListViewModel.retrieveRecipes();

        binding.confirmRecipe.setOnClickListener(v -> {
            saveRecipePreference(RecipeIngredientsWidgetConfigureActivity.this, appWidgetId,
                    adapter.getItem(binding.recipeChoose.getSelectedItemPosition()));

            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(RecipeIngredientsWidgetConfigureActivity.this);
            RecipeIngredientsWidget
                    .updateAppWidget(RecipeIngredientsWidgetConfigureActivity.this,
                        appWidgetManager,
                            appWidgetId);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        });

        setResult(RESULT_CANCELED);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
    }

    static void saveRecipePreference(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    static String loadRecipePreference(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);

        return titleValue;
    }

    static void deleteRecipePreference(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }
}

