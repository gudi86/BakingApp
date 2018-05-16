package br.com.gustavo.bakingapp.masterrecipe.stepdetailrecipe;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import br.com.gustavo.bakingapp.R;
import br.com.gustavo.bakingapp.data.model.Step;
import br.com.gustavo.bakingapp.data.source.BakingDataSourceImpl;
import br.com.gustavo.bakingapp.masterrecipe.RecipeIdlingResource;

/**
 * Created by gustavomagalhaes on 11/30/17.
 */

public class StepDetailFragment extends Fragment implements StepDetailContract.Intern.View, View.OnClickListener {

    private static final String LOG_TAG = StepDetailFragment.class.getName();
    private static final String PLAYBACK_POSITION = "PLAYBACK_POSITION";
    private static final String CURRENT_WINDOW_INDEX = "CURRENT_WINDOW_INDEX";
    private Step stepRecipe;
    private SimpleExoPlayerView playerView;
    private SimpleExoPlayer player = null;
    private View layoutView;
    private StepDetailContract.Intern.Presenter presenter;
    private ImageView imageStep;
    private long playbackPosition = 0;
    private int currentWindow = 0;
    private ImageButton btnFullScreen;
    private RecipeIdlingResource idleTest = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getResources().getBoolean(R.bool.isTablet)) {
            layoutView = inflater.inflate(R.layout.fragment_step_detail_tablet, container, false);
        } else {
            layoutView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        }
        new StepDetailInternPresenter(BakingDataSourceImpl.getInstance(getContext()), this);

        playerView = layoutView.findViewById(R.id.epv_recipe);
        imageStep = layoutView.findViewById(R.id.iv_recipe);
        btnFullScreen = layoutView.findViewById(R.id.fullscreen);

        if (getResources().getBoolean(R.bool.isTablet)) {
            btnFullScreen.setVisibility(View.GONE);
        } else {
            btnFullScreen.setOnClickListener(this);
        }
        return layoutView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(PLAYBACK_POSITION)) {
                playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION);
                currentWindow = savedInstanceState.getInt(CURRENT_WINDOW_INDEX);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (player != null) {
            outState.putLong(PLAYBACK_POSITION, player.getCurrentPosition());
            outState.putInt(CURRENT_WINDOW_INDEX, player.getCurrentWindowIndex());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            presenter.loadStep(stepRecipe);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Util.SDK_INT <= 23 || player == null) {
            presenter.loadStep(stepRecipe);
        }
        if (!getResources().getBoolean(R.bool.isTablet)) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                btnFullScreen.setImageResource(R.drawable.icon_fullscreen);
            } else {
                btnFullScreen.setImageResource(R.drawable.icon_fullscreen_exit);
                layoutView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void setPresenter(StepDetailContract.Intern.Presenter presenter) {
        this.presenter = presenter;

    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void showTextDescription(String description) {
        TextView tvStepDescription = layoutView.findViewById(R.id.tv_step_more_description);
        if (tvStepDescription != null) {
            tvStepDescription.setText(description);
        }
    }

    @Override
    public void showVideoBy(Uri videoUri) {
        if (player == null) {
            playerView.setVisibility(View.VISIBLE);
            imageStep.setVisibility(View.GONE);
            // Create an instance of the ExoPlayer.

            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl());

            playerView.setPlayer(player);

            player.setPlayWhenReady(true);

            MediaSource mediaSource = new ExtractorMediaSource(
                    videoUri,
                    new DefaultHttpDataSourceFactory("ua"),
                    new DefaultExtractorsFactory(), null, null);

            player.prepare(mediaSource, true, false);

            player.seekTo(currentWindow, playbackPosition);
        }

        if (idleTest != null)
            idleTest.setIdleNow(true);
    }

    @Override
    public void showImageBy(Uri imageUri) {
        imageStep.setVisibility(View.VISIBLE);
        playerView.setVisibility(View.GONE);
        Picasso
            .with(getContext())
            .load(imageUri)
            .into(imageStep);

        if (idleTest != null)
            idleTest.setIdleNow(true);
    }

    @Override
    public void showMissingMedia() {
        playerView.setVisibility(View.GONE);
        imageStep.setVisibility(View.VISIBLE);

        if (idleTest != null)
            idleTest.setIdleNow(true);
    }

    private void releasePlayer() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    public void setStep(Step step) {
        this.stepRecipe = step;
    }

    public void setIdleTest(RecipeIdlingResource idleTest) {
        this.idleTest = idleTest;
    }
}
