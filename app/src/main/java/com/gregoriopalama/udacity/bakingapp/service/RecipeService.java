package com.gregoriopalama.udacity.bakingapp.service;

import com.gregoriopalama.udacity.bakingapp.model.Recipe;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Recipe Service Interface for Retrofit
 *
 * @author Gregorio Palamà
 */

public interface RecipeService {
    @GET("baking.json")
    Observable<List<Recipe>> getRecipes();
}
