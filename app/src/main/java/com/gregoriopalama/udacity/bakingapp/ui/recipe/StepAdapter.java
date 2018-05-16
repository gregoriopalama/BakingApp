package com.gregoriopalama.udacity.bakingapp.ui.recipe;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gregoriopalama.udacity.bakingapp.databinding.StepBinding;
import com.gregoriopalama.udacity.bakingapp.model.Step;
import com.gregoriopalama.udacity.bakingapp.ui.GenericViewHolder;

import java.util.List;

/**
 * Adapter for the steps
 *
 * @author Gregorio Palamà
 */

public class StepAdapter extends RecyclerView.Adapter<GenericViewHolder> {
    private List<Step> items;
    private StepDetailListener listener;

    public StepAdapter(List<Step> items, StepDetailListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public void setItems(List<Step> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        StepBinding binding =
                StepBinding.inflate(layoutInflater, parent, false);
        return new GenericViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        if (items == null)
            return 0;
        return items.size();
    }
}
