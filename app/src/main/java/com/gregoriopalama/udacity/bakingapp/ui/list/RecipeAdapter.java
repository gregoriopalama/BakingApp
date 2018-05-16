package com.gregoriopalama.udacity.bakingapp.ui.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gregoriopalama.udacity.bakingapp.databinding.RecipeBinding;
import com.gregoriopalama.udacity.bakingapp.model.Recipe;
import com.gregoriopalama.udacity.bakingapp.ui.GenericViewHolder;

import java.util.List;

/**
 * The adapter for a recipe
 *
 * @author Gregorio Palamà
 */

public class RecipeAdapter extends RecyclerView.Adapter<GenericViewHolder> {
    private List<Recipe> items;
    private RecipeListener listener;

    public RecipeAdapter(List<Recipe> items, RecipeListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public void setItems(List<Recipe> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        RecipeBinding binding =
                RecipeBinding.inflate(layoutInflater, parent, false);
        return new GenericViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
