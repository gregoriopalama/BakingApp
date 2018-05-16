package com.gregoriopalama.udacity.bakingapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.gregoriopalama.udacity.bakingapp.model.Ingredient;
import com.gregoriopalama.udacity.bakingapp.model.Recipe;
import com.gregoriopalama.udacity.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Holds informations about a single recipe
 *
 * @author Gregorio Palamà
 */

public class RecipeViewModel extends ViewModel {

    private MutableLiveData<List<Ingredient>> ingredients;
    private MutableLiveData<List<Step>> steps;
    private MutableLiveData<Recipe> recipe;

    @Inject
    public RecipeViewModel() {
        this.ingredients = new MutableLiveData<>();
        this.ingredients.setValue(new ArrayList<>());
        this.steps = new MutableLiveData<>();
        this.steps.setValue(new ArrayList<>());
        this.recipe = new MutableLiveData<>();
    }

    public MutableLiveData<List<Ingredient>> getObservableIngredients() {
        return ingredients;
    }

    public MutableLiveData<List<Step>> getObservableSteps() {
        return steps;
    }

    public MutableLiveData<Recipe> getObservableRecipe() {
        return recipe;
    }

    public List<Ingredient> getIngredients() {
        return ingredients.getValue();
    }

    public List<Step> getSteps() {
        return steps.getValue();
    }

    public Recipe getRecipe() {
        return recipe.getValue();
    }

    public void setRecipe(Recipe recipe) {
        this.recipe.setValue(recipe);
        this.steps.setValue(recipe.getSteps());
        this.ingredients.setValue(recipe.getIngredients());
    }
}
