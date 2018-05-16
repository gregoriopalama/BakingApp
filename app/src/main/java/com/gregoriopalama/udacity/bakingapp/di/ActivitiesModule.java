package com.gregoriopalama.udacity.bakingapp.di;

import com.gregoriopalama.udacity.bakingapp.ui.widget.RecipeIngredientsWidget;
import com.gregoriopalama.udacity.bakingapp.ui.widget.RecipeIngredientsWidgetConfigureActivity;
import com.gregoriopalama.udacity.bakingapp.ui.list.MainActivity;
import com.gregoriopalama.udacity.bakingapp.ui.recipe.RecipeActivity;
import com.gregoriopalama.udacity.bakingapp.ui.step.StepActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Dagger module for Activities
 *
 * @author Gregorio Palamà
 */

@Module
public abstract class ActivitiesModule {

    @ContributesAndroidInjector(modules = {FragmentModule.class})
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector(modules = {FragmentModule.class})
    abstract RecipeActivity bindRecipeActivity();

    @ContributesAndroidInjector(modules = {FragmentModule.class})
    abstract StepActivity bindStepActivity();

    @ContributesAndroidInjector
    abstract RecipeIngredientsWidgetConfigureActivity bindRecipeIngredientsWidgetConfigureActivity();

    @ContributesAndroidInjector
    abstract RecipeIngredientsWidget bindRecipeIngredientsWidget();
}
