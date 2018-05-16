package com.gregoriopalama.udacity.bakingapp.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.gregoriopalama.udacity.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Holds informations about the list of steps of a recipe
 *
 * @author Gregorio Palamà
 */

public class StepsListViewModel extends ViewModel {
    private MutableLiveData<List<Step>> steps;
    private MutableLiveData<Integer> currentStep;

    @Inject
    public StepsListViewModel() {
        this.steps = new MutableLiveData<>();
        this.steps.setValue(new ArrayList<>());
        this.currentStep = new MutableLiveData<>();
        this.currentStep.setValue(0);
    }

    public List<Step> getSteps() {
        return steps.getValue();
    }

    public void setSteps(List<Step> steps) {
        this.steps.setValue(steps);
    }

    public MutableLiveData<Integer> getObservableCurrentStep() {
        return currentStep;
    }

    public int getCurrentStep() {
        return currentStep.getValue();
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep.setValue(currentStep);
    }
}
