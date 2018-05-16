package com.gregoriopalama.udacity.bakingapp.di;

import com.gregoriopalama.udacity.bakingapp.ui.list.RecipesListFragment;
import com.gregoriopalama.udacity.bakingapp.ui.recipe.IngredientsFragment;
import com.gregoriopalama.udacity.bakingapp.ui.recipe.StepsFragment;
import com.gregoriopalama.udacity.bakingapp.ui.step.SingleStepFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Dagger module for Fragments
 *
 * @author Gregorio Palamà
 */

@Module
public abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract RecipesListFragment contributesRecipesListFragment();

    @ContributesAndroidInjector
    abstract StepsFragment contributesStepsFragment();

    @ContributesAndroidInjector
    abstract IngredientsFragment contributesIngredientsFragment();

    @ContributesAndroidInjector
    abstract SingleStepFragment contributesStepFragment();
}
