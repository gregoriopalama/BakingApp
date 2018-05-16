package com.gregoriopalama.udacity.bakingapp.ui.recipe;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gregoriopalama.udacity.bakingapp.databinding.FragmentIngredientsBinding;
import com.gregoriopalama.udacity.bakingapp.di.Injectable;
import com.gregoriopalama.udacity.bakingapp.viewmodel.RecipeViewModel;
import com.gregoriopalama.udacity.bakingapp.viewmodel.ViewModelFactory;

import javax.inject.Inject;

/**
 * Fragment for a list of Ingredients
 *
 * @author Gregorio Palamà
 */
public class IngredientsFragment extends Fragment implements Injectable {
    private static final String TAG = "IngredientsFragment";
    FragmentIngredientsBinding binding;

    @Inject
    ViewModelFactory viewModelFactory;
    RecipeViewModel recipeViewModel;

    IngredientAdapter adapter;

    public IngredientsFragment() {
    }

    private static IngredientsFragment INSTANCE = null;

    public static IngredientsFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IngredientsFragment();
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        recipeViewModel = ViewModelProviders.of(getActivity(), viewModelFactory)
                .get(RecipeViewModel.class);
        recipeViewModel.getObservableIngredients().observe(this, (ingredients) -> {
            if (adapter == null)
                return;

            adapter.setItems(ingredients);
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentIngredientsBinding.inflate(inflater, container, false);

        binding.ingredients.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(),
                LinearLayoutManager.VERTICAL, false));

        adapter = new IngredientAdapter(recipeViewModel.getIngredients());
        binding.ingredients.setAdapter(adapter);
        binding.ingredients.addItemDecoration(new DividerItemDecoration(binding.ingredients.getContext(),
                LinearLayoutManager.VERTICAL));

        return binding.getRoot();
    }

}
