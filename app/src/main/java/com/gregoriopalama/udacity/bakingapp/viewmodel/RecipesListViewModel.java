package com.gregoriopalama.udacity.bakingapp.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.gregoriopalama.udacity.bakingapp.model.Recipe;
import com.gregoriopalama.udacity.bakingapp.service.RecipeService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Hold the informations about the list of recipes and the state of the list
 *
 * @author Gregorio Palamà
 */

public class RecipesListViewModel extends ViewModel {

    private RecipeService recipeService;

    private MutableLiveData<List<Recipe>> recipes;
    private MutableLiveData<String> requestedRecipeFromWidget;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<Integer> gridColumns;

    @Inject
    public RecipesListViewModel(RecipeService recipeService) {
        this.recipeService = recipeService;
        this.recipes = new MutableLiveData<>();
        this.recipes.setValue(new ArrayList<>());
        this.requestedRecipeFromWidget = new MutableLiveData<>();
        this.gridColumns = new MutableLiveData<>();
        this.gridColumns.setValue(1);
    }

    public Integer getGridColumns() {
        return gridColumns.getValue();
    }

    public void setGridColumns(int gridColumns) {
        this.gridColumns.setValue(gridColumns);
    }

    public MutableLiveData<List<Recipe>> getObservableRecipes() {
        return recipes;
    }

    public List<Recipe> getRecipes() {
        return recipes.getValue();
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes.setValue(recipes);
    }

    public String getRequestedRecipeFromWidget() {
        return requestedRecipeFromWidget.getValue();
    }

    public void setRequestedRecipeFromWidget(String requestedRecipeFromWidget) {
        this.requestedRecipeFromWidget.setValue(requestedRecipeFromWidget);
    }

    public void retrieveRecipes() {
        compositeDisposable.add(recipeService.getRecipes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Recipe>>() {

                    @Override
                    public void onNext(List<Recipe> results) {
                        if (results.size() == 0)
                            return;

                        recipes.setValue(results);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        compositeDisposable.clear();
    }
}
