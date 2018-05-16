package com.gregoriopalama.udacity.bakingapp.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;


import com.gregoriopalama.udacity.bakingapp.viewmodel.RecipeViewModel;
import com.gregoriopalama.udacity.bakingapp.viewmodel.RecipesListViewModel;
import com.gregoriopalama.udacity.bakingapp.viewmodel.StepsListViewModel;
import com.gregoriopalama.udacity.bakingapp.viewmodel.ViewModelFactory;
import com.gregoriopalama.udacity.bakingapp.viewmodel.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Dagger module that provides all the ViewModels
 *
 * @author Gregorio Palamà
 */

@Module(includes = {RecipeServiceModule.class})
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(RecipesListViewModel.class)
    abstract ViewModel bindRecipesListViewModel(RecipesListViewModel recipesListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecipeViewModel.class)
    abstract ViewModel bindRecipeViewModel(RecipeViewModel recipeViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(StepsListViewModel.class)
    abstract ViewModel bindStepsListViewModel(StepsListViewModel stepsListViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

}
