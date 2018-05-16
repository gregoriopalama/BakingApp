package com.gregoriopalama.udacity.bakingapp.di;

import android.databinding.DataBindingComponent;

import dagger.Subcomponent;

/**
 * Dagger module for Binding adapters
 *
 * @author Gregorio Palamà
 */

@Subcomponent(modules = BindingAdaptersModule.class)
@AppScope
public interface BindingComponent extends DataBindingComponent {}
