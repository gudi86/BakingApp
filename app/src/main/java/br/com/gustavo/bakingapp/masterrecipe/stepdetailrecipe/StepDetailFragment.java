package br.com.gustavo.bakingapp.masterrecipe.stepdetailrecipe;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import br.com.gustavo.bakingapp.R;
import br.com.gustavo.bakingapp.data.model.Step;
import br.com.gustavo.bakingapp.data.source.BakingDataSourceImpl;

/**
 * Created by gustavomagalhaes on 11/30/17.
 */

public class StepDetailFragment extends Fragment implements StepDetailContract.Intern.View, ExoPlayer.EventListener {

    private Step stepRecipe;
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer mExoPlayer = null;
    private View layoutView;
    private StepDetailContract.Intern.Presenter presenter;
    private ImageView imageStep;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        simpleExoPlayerView = layoutView.findViewById(R.id.epv_recipe);
        imageStep = layoutView.findViewById(R.id.iv_recipe);

        new StepDetailInternPresenter(BakingDataSourceImpl.getInstance(getContext()), this);
        presenter.loadStep(stepRecipe);

        return layoutView;
    }

    @Override
    public void setPresenter(StepDetailContract.Intern.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showTextDescription(String description) {
        TextView tvStepDescription = layoutView.findViewById(R.id.tv_step_description);
        tvStepDescription.setText(description);
    }

    @Override
    public void showVideoBy(Uri videoUri) {
        if (mExoPlayer == null) {
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            imageStep.setVisibility(View.GONE);
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(
                    videoUri,
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(),
                    null,
                    null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void showImageBy(Uri imageUri) {
        imageStep.setVisibility(View.VISIBLE);
        simpleExoPlayerView.setVisibility(View.GONE);
        Picasso
            .with(getContext())
            .load(imageUri)
            .into(imageStep);
    }

    @Override
    public void showMissingMedia() {
        simpleExoPlayerView.setVisibility(View.GONE);
        imageStep.setVisibility(View.VISIBLE);
    }

    public void setStep(Step step) {
        this.stepRecipe = step;
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }
    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}
