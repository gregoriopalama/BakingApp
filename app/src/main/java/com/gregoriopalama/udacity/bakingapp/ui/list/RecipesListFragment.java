package com.gregoriopalama.udacity.bakingapp.ui.list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.gregoriopalama.udacity.bakingapp.R;
import com.gregoriopalama.udacity.bakingapp.databinding.FragmentRecipesListBinding;
import com.gregoriopalama.udacity.bakingapp.di.Injectable;
import com.gregoriopalama.udacity.bakingapp.model.Recipe;
import com.gregoriopalama.udacity.bakingapp.ui.recipe.RecipeActivity;
import com.gregoriopalama.udacity.bakingapp.viewmodel.RecipesListViewModel;
import com.gregoriopalama.udacity.bakingapp.viewmodel.ViewModelFactory;

import javax.inject.Inject;

import static com.gregoriopalama.udacity.bakingapp.Constants.EXTRA_RECIPE;

/**
 * Fragment for a list of recipes
 *
 * @author Gregorio Palamà
 */
public class RecipesListFragment extends Fragment implements Injectable, RecipeListener  {
    private static final String TAG = "RecipesListFragment";
    FragmentRecipesListBinding binding;

    @Inject
    ViewModelFactory viewModelFactory;
    RecipesListViewModel recipesListViewModel;

    RecipeAdapter adapter;

    public RecipesListFragment() {
    }

    public static RecipesListFragment findOrCreateFragment(FragmentManager fragmentManager) {
        RecipesListFragment fragment = (RecipesListFragment) fragmentManager.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new RecipesListFragment();
            fragmentManager.beginTransaction().add(fragment, TAG).commit();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recipesListViewModel = ViewModelProviders.of(getActivity(), viewModelFactory)
                .get(RecipesListViewModel.class);
        recipesListViewModel.getObservableRecipes().observe(this, (recipes) -> {
            if (adapter == null)
                return;

            adapter.setItems(recipes);
            binding.recipeList.scheduleLayoutAnimation();

            if (!TextUtils.isEmpty(recipesListViewModel.getRequestedRecipeFromWidget())) {
                for (Recipe recipe : recipes) {
                    if (recipe.getName().equals(recipesListViewModel
                            .getRequestedRecipeFromWidget())) {
                        openRecipe(recipe);
                        break;
                    }
                }
            }
        });
        recipesListViewModel.retrieveRecipes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecipesListBinding.inflate(inflater, container, false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),
                recipesListViewModel.getGridColumns());
        binding.recipeList.setLayoutManager(gridLayoutManager);
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(getContext(), R.anim.grid_layout_bottom);

        binding.recipeList.setLayoutAnimation(controller);

        adapter = new RecipeAdapter(recipesListViewModel.getRecipes(), this);
        binding.recipeList.setAdapter(adapter);
        binding.recipeList.scheduleLayoutAnimation();
        binding.recipeList.setHasFixedSize(true);
        binding.recipeList.setItemViewCacheSize(20);
        binding.recipeList.setDrawingCacheEnabled(true);
        binding.recipeList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        int gridColumns = getResources().getInteger(R.integer.recipe_list_grid_columns);
        gridLayoutManager.setSpanCount(gridColumns);
        recipesListViewModel.setGridColumns(gridColumns);

        return binding.getRoot();
    }

    @Override
    public void openRecipe(Recipe recipe) {
        Intent intent = new Intent(getContext(), RecipeActivity.class);
        intent.putExtra(EXTRA_RECIPE, recipe);
        startActivity(intent);
    }

}
