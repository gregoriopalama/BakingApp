package com.gregoriopalama.udacity.bakingapp.ui.list;

import com.gregoriopalama.udacity.bakingapp.model.Recipe;
import com.gregoriopalama.udacity.bakingapp.ui.GenericViewHolderListener;

/**
 * Interface listener for the action on a recipe
 *
 * @author Gregorio Palamà
 */

public interface RecipeListener extends GenericViewHolderListener {
    public void openRecipe(Recipe recipe);
}
