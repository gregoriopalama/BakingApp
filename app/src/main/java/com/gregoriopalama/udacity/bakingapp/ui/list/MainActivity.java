package com.gregoriopalama.udacity.bakingapp.ui.list;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.gregoriopalama.udacity.bakingapp.R;
import com.gregoriopalama.udacity.bakingapp.databinding.ActivityMainBinding;
import com.gregoriopalama.udacity.bakingapp.di.Injectable;
import com.gregoriopalama.udacity.bakingapp.viewmodel.RecipesListViewModel;
import com.gregoriopalama.udacity.bakingapp.viewmodel.ViewModelFactory;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

import static com.gregoriopalama.udacity.bakingapp.Constants.EXTRA_WIDGET_REQUESTED_RECIPE;


/**
 * The first activity for the App
 *
 * @author Gregorio Palamà
 */
public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector, Injectable {
    ActivityMainBinding binding;

    @Inject
    ViewModelFactory viewModelFactory;
    RecipesListViewModel recipesListViewModel;

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        recipesListViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(RecipesListViewModel.class);

        if (getIntent() != null && getIntent().hasExtra(EXTRA_WIDGET_REQUESTED_RECIPE)) {
            recipesListViewModel.setRequestedRecipeFromWidget(getIntent()
                    .getStringExtra(EXTRA_WIDGET_REQUESTED_RECIPE));
        }

        if (savedInstanceState == null)
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_layout,
                            RecipesListFragment.findOrCreateFragment(getSupportFragmentManager()))
                    .commit();

    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }
}
