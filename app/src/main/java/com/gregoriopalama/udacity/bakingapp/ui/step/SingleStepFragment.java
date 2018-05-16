package com.gregoriopalama.udacity.bakingapp.ui.step;

import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.gregoriopalama.udacity.bakingapp.BR;
import com.gregoriopalama.udacity.bakingapp.databinding.FragmentStepBinding;
import com.gregoriopalama.udacity.bakingapp.di.Injectable;
import com.gregoriopalama.udacity.bakingapp.viewmodel.StepsListViewModel;
import com.gregoriopalama.udacity.bakingapp.viewmodel.ViewModelFactory;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import javax.inject.Inject;

import static com.gregoriopalama.udacity.bakingapp.Constants.CURRENT_STEP_POSITION;

/**
 * Fragment for a single step with the videos
 *
 * @author Gregorio Palamà
 */
public class SingleStepFragment extends Fragment implements BlockingStep, Injectable {
    FragmentStepBinding binding;

    @Inject
    ViewModelFactory viewModelFactory;
    StepsListViewModel stepsListViewModel;

    private int position;
    private SimpleExoPlayer player;
    private boolean shouldAutoPlay = false;
    private int resumeWindow;
    private long resumePosition;

    StepperLayout layout;

    public SingleStepFragment() {
    }

    public void setLayout(StepperLayout layout) {
        this.layout = layout;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        stepsListViewModel = ViewModelProviders.of(getActivity(), viewModelFactory)
                .get(StepsListViewModel.class);

        clearResumePosition();
        shouldAutoPlay = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStepBinding.inflate(inflater, container, false);

        position = getArguments().getInt(CURRENT_STEP_POSITION, 0);

        return binding.getRoot();
    }

    private void initializePlayer() {
        String url = stepsListViewModel.getSteps().get(position).getVideoUrl();

        if (TextUtils.isEmpty(url)) {
            return;
        }

        Uri uri = Uri.parse(url);
        initializePlayerWithUri(uri);
    }

    private void initializePlayerWithUri(Uri uri) {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()),
                trackSelector, new DefaultLoadControl());
        MediaSource audioSource = new ExtractorMediaSource.Factory(new CacheDataSourceFactory(getContext(),
                100 * 1024 * 1024, 5 * 1024 * 1024))
                .createMediaSource(uri);

        binding.video.setPlayer(player);
        player.prepare(audioSource);

        player.setPlayWhenReady(shouldAutoPlay);

        boolean haveResumePosition = resumeWindow != C.INDEX_UNSET;
        if (haveResumePosition) {
            player.seekTo(resumeWindow, resumePosition);
        }

    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        if (position >= stepsListViewModel.getSteps().size())
            return new VerificationError("STOP");

        return null;
    }

    @Override
    public void onSelected() {
        binding.setVariable(BR.step, stepsListViewModel.getSteps().get(position));
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (player == null) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    private void releasePlayer() {
        if (player == null)
            return;

        shouldAutoPlay = player.getPlayWhenReady();
        updateResumePosition();
        player.stop();
        player.release();
        player = null;
    }

    private void updateResumePosition() {
        resumeWindow = player.getCurrentWindowIndex();
        resumePosition = Math.max(0, player.getContentPosition());
    }

    private void clearResumePosition() {
        resumeWindow = C.INDEX_UNSET;
        resumePosition = C.TIME_UNSET;
    }

    @Override
    @UiThread
    public void onNextClicked(final StepperLayout.OnNextClickedCallback callback) {
        stepsListViewModel.setCurrentStep(stepsListViewModel.getCurrentStep()+1);
        releasePlayer();
        callback.goToNextStep();
    }

    @Override
    @UiThread
    public void onCompleteClicked(final StepperLayout.OnCompleteClickedCallback callback) {
        callback.complete();
    }

    @Override
    @UiThread
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        stepsListViewModel.setCurrentStep(stepsListViewModel.getCurrentStep()-1);
        releasePlayer();
        callback.goToPrevStep();
    }

}
