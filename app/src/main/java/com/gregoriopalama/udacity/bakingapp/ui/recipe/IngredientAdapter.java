package com.gregoriopalama.udacity.bakingapp.ui.recipe;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gregoriopalama.udacity.bakingapp.databinding.IngredientBinding;
import com.gregoriopalama.udacity.bakingapp.model.Ingredient;
import com.gregoriopalama.udacity.bakingapp.ui.GenericViewHolder;

import java.util.List;

/**
 * Adapter for the ingredients
 *
 * @author Gregorio Palamà
 */

public class IngredientAdapter extends RecyclerView.Adapter<GenericViewHolder> {
    private List<Ingredient> items;

    public IngredientAdapter(List<Ingredient> items) {
        this.items = items;
    }

    public void setItems(List<Ingredient> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        IngredientBinding binding =
                IngredientBinding.inflate(layoutInflater, parent, false);
        return new GenericViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        if (items == null)
            return 0;
        return items.size();
    }
}
