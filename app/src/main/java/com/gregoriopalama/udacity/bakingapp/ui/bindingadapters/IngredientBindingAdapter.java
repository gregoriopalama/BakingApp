package com.gregoriopalama.udacity.bakingapp.ui.bindingadapters;

import android.databinding.BindingAdapter;
import android.widget.TextView;

import com.gregoriopalama.udacity.bakingapp.model.Ingredient;

/**
 * Binding adapters for ingredients
 *
 * @author Gregorio Palamà
 */

public class IngredientBindingAdapter {
    @BindingAdapter({"ingredient_name"})
    public static void loadIngredientName(TextView textView, Ingredient ingredient) {
        textView.setText(Character.toUpperCase(ingredient.getName().charAt(0)) +
                ingredient.getName().substring(1));
    }

    @BindingAdapter({"ingredient_quantity"})
    public static void loadIngredientQuantity(TextView textView, Ingredient ingredient) {
        textView.setText(ingredient.getQuantity() + " " + ingredient.getMeasure());
    }
}
