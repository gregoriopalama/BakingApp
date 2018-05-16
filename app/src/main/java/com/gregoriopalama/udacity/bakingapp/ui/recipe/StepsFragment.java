package com.gregoriopalama.udacity.bakingapp.ui.recipe;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gregoriopalama.udacity.bakingapp.R;
import com.gregoriopalama.udacity.bakingapp.databinding.FragmentStepsBinding;
import com.gregoriopalama.udacity.bakingapp.di.Injectable;
import com.gregoriopalama.udacity.bakingapp.model.Step;
import com.gregoriopalama.udacity.bakingapp.ui.step.StepActivity;
import com.gregoriopalama.udacity.bakingapp.viewmodel.RecipeViewModel;
import com.gregoriopalama.udacity.bakingapp.viewmodel.StepsListViewModel;
import com.gregoriopalama.udacity.bakingapp.viewmodel.ViewModelFactory;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.gregoriopalama.udacity.bakingapp.Constants.EXTRA_RECIPE_NAME;
import static com.gregoriopalama.udacity.bakingapp.Constants.EXTRA_STEPS;
import static com.gregoriopalama.udacity.bakingapp.Constants.EXTRA_STEP_POSITION;

/**
 * Fragment for a list of steps
 *
 * @author Gregorio Palamà
 */
public class StepsFragment extends Fragment implements StepDetailListener, Injectable {
    private static final String TAG = "StepsFragment";
    FragmentStepsBinding binding;

    @Inject
    ViewModelFactory viewModelFactory;
    RecipeViewModel recipeViewModel;
    StepsListViewModel stepsListViewModel;

    StepAdapter adapter;

    public StepsFragment() {
    }

    private static StepsFragment INSTANCE = null;

    public static StepsFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StepsFragment();
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        recipeViewModel = ViewModelProviders.of(getActivity(), viewModelFactory)
                .get(RecipeViewModel.class);
        stepsListViewModel = ViewModelProviders.of(getActivity(), viewModelFactory)
                .get(StepsListViewModel.class);
        if (getActivity().getResources().getBoolean(R.bool.is_tablet)) {
            stepsListViewModel.getObservableCurrentStep().observe(this, step -> {
                if (recipeViewModel.getSteps() == null)
                    return;

                resetSelectedStep(step);
            });
        }

        recipeViewModel.getObservableSteps().observe(this, (steps) -> {
            if (adapter == null)
                return;

            adapter.setItems(steps);
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStepsBinding.inflate(inflater, container, false);

        binding.steps.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(),
                LinearLayoutManager.VERTICAL, false));

        adapter = new StepAdapter(recipeViewModel.getSteps(), this);
        binding.steps.setAdapter(adapter);
        binding.steps.addItemDecoration(new DividerItemDecoration(binding.steps.getContext(),
                LinearLayoutManager.VERTICAL));

        return binding.getRoot();
    }

    @Override
    public void openStep(Step step) {
        int position = 0;
        if (step != null) {
            position = recipeViewModel.getSteps().indexOf(step);
        }

        if (getActivity().getResources().getBoolean(R.bool.is_tablet)) {
            resetSelectedStep(position);

            stepsListViewModel.setCurrentStep(position);
            return;
        }

        Intent intent = new Intent(getContext(), StepActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_STEPS, (ArrayList<Step>) recipeViewModel.getSteps());
        intent.putExtra(EXTRA_STEP_POSITION, position);
        intent.putExtra(EXTRA_RECIPE_NAME, recipeViewModel.getRecipe().getName());
        startActivity(intent);
    }

    private void resetSelectedStep(int position) {
        for (Step s : recipeViewModel.getSteps()) {
            s.setSelected(false);
        }
        recipeViewModel.getSteps().get(position).setSelected(true);
        adapter.notifyDataSetChanged();
    }
}
