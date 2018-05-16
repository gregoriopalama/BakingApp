package com.gregoriopalama.udacity.bakingapp.ui.step;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;

import static com.gregoriopalama.udacity.bakingapp.Constants.CURRENT_STEP_POSITION;

/**
 * Adapter with all the steps and a material stepper on the bottom
 *
 * @author Gregorio Palamà
 */
public class StepperAdapter extends AbstractFragmentStepAdapter  {
    int size;
    StepperLayout layout;

    public StepperAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setLayout(StepperLayout layout) {
        this.layout = layout;
    }

    @Override
    public Step createStep(int position) {
        final SingleStepFragment stepFragment = new SingleStepFragment();

        stepFragment.setLayout(this.layout);
        Bundle bundle = new Bundle();
        bundle.putInt(CURRENT_STEP_POSITION, position);
        stepFragment.setArguments(bundle);

        return stepFragment;
    }

    @Override
    public int getCount() {
        return size;
    }
}
