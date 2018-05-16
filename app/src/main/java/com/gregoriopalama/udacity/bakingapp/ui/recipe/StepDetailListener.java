package com.gregoriopalama.udacity.bakingapp.ui.recipe;

import com.gregoriopalama.udacity.bakingapp.model.Step;
import com.gregoriopalama.udacity.bakingapp.ui.GenericViewHolderListener;

/**
 * Interface listener for the action on a step
 *
 * @author Gregorio Palamà
 */

public interface StepDetailListener extends GenericViewHolderListener {
    public void openStep(Step step);
}
