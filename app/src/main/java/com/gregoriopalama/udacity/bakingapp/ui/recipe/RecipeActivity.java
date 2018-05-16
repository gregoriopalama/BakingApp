package com.gregoriopalama.udacity.bakingapp.ui.recipe;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.gregoriopalama.udacity.bakingapp.R;
import com.gregoriopalama.udacity.bakingapp.databinding.ActivityRecipeBinding;
import com.gregoriopalama.udacity.bakingapp.di.Injectable;
import com.gregoriopalama.udacity.bakingapp.ui.step.StepperAdapter;
import com.gregoriopalama.udacity.bakingapp.viewmodel.RecipeViewModel;
import com.gregoriopalama.udacity.bakingapp.viewmodel.StepsListViewModel;
import com.gregoriopalama.udacity.bakingapp.viewmodel.ViewModelFactory;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

import static com.gregoriopalama.udacity.bakingapp.Constants.EXTRA_RECIPE;

/**
 * The detail of a recipe
 *
 * @author Gregorio Palamà
 */
public class RecipeActivity extends AppCompatActivity implements HasSupportFragmentInjector, Injectable {
    ActivityRecipeBinding binding;

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Inject
    ViewModelFactory viewModelFactory;
    RecipeViewModel recipeViewModel;
    StepsListViewModel stepsListViewModel;

    StepperAdapter stepperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getIntent().hasExtra(EXTRA_RECIPE))
            finish();

        recipeViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(RecipeViewModel.class);
        if (getResources().getBoolean(R.bool.is_tablet)) {
            stepsListViewModel = ViewModelProviders.of(this, viewModelFactory)
                    .get(StepsListViewModel.class);
            stepsListViewModel.getObservableCurrentStep().observe(this, step -> {
                if (stepperAdapter == null)
                    return;

                binding.stepperLayout.setCurrentStepPosition(step);
            });
        }


        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecipePagerAdapter adapter = new RecipePagerAdapter(this, getSupportFragmentManager());
        binding.viewpager.setAdapter(adapter);
        binding.slidingTabs.setupWithViewPager(binding.viewpager);

        recipeViewModel.getObservableRecipe().observe(this, recipe -> {
            if (recipe == null)
                return;

            setTitle(recipe.getName());
            if (getResources().getBoolean(R.bool.is_tablet)
                    && stepsListViewModel != null) {
                stepsListViewModel.setSteps(recipe.getSteps());

                stepperAdapter = new StepperAdapter(getSupportFragmentManager(), this);
                stepperAdapter.setSize(stepsListViewModel.getSteps().size());
                stepperAdapter.setLayout(binding.stepperLayout);
                binding.stepperLayout.setAdapter(stepperAdapter);

                stepsListViewModel.setCurrentStep(0);
            }

        });
        recipeViewModel.setRecipe(getIntent().getParcelableExtra(EXTRA_RECIPE));
        binding.setRecipe(recipeViewModel.getRecipe());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }
}
