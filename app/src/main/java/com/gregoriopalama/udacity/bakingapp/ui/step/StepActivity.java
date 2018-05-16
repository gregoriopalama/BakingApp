package com.gregoriopalama.udacity.bakingapp.ui.step;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import com.gregoriopalama.udacity.bakingapp.R;
import com.gregoriopalama.udacity.bakingapp.databinding.ActivityStepBinding;
import com.gregoriopalama.udacity.bakingapp.di.Injectable;
import com.gregoriopalama.udacity.bakingapp.viewmodel.StepsListViewModel;
import com.gregoriopalama.udacity.bakingapp.viewmodel.ViewModelFactory;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

import static com.gregoriopalama.udacity.bakingapp.Constants.EXTRA_RECIPE_NAME;
import static com.gregoriopalama.udacity.bakingapp.Constants.EXTRA_STEPS;
import static com.gregoriopalama.udacity.bakingapp.Constants.EXTRA_STEP_POSITION;

/**
 * Activity with the list of the steps
 *
 * @author Gregorio Palamà
 */
public class StepActivity extends AppCompatActivity implements HasSupportFragmentInjector, Injectable {
    ActivityStepBinding binding;

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Inject
    ViewModelFactory viewModelFactory;
    StepsListViewModel stepsListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_step);

        if (!getIntent().hasExtra(EXTRA_STEPS))
            finish();

        if (!getResources().getBoolean(R.bool.is_landscape)) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(TextUtils.concat(getIntent().getStringExtra(EXTRA_RECIPE_NAME),
                    " ",
                    getString(R.string.steps_title)));
        }

        stepsListViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(StepsListViewModel.class);

        stepsListViewModel.setSteps(getIntent().getParcelableArrayListExtra(EXTRA_STEPS));
        StepperAdapter adapter = new StepperAdapter(getSupportFragmentManager(), this);
        adapter.setSize(stepsListViewModel.getSteps().size());
        adapter.setLayout(binding.stepperLayout);
        binding.stepperLayout.setAdapter(adapter);

        if (getIntent().hasExtra(EXTRA_STEP_POSITION)) {
            binding.stepperLayout.setCurrentStepPosition(getIntent().getIntExtra(EXTRA_STEP_POSITION, 0));
        }
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
