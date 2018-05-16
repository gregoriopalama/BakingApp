package com.gregoriopalama.udacity.bakingapp.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * AppScope for Dagger SubComponent
 *
 * @author Gregorio Palamà
 */
@Scope
@Retention(RetentionPolicy.CLASS)
public @interface AppScope {
}
