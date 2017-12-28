package br.com.gustavo.bakingapp.masterrecipe.stepdetailrecipe;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import br.com.gustavo.bakingapp.R;
import br.com.gustavo.bakingapp.data.model.Step;
import br.com.gustavo.bakingapp.data.source.BakingDataSourceImpl;

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
    private boolean playWhenReady;
    private ImageButton btnFullScreen;

    private OrientationEventListener orientationEventListener;
    private boolean orientationPortrait;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_step_detail, container, false);

        orientationPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        new StepDetailInternPresenter(BakingDataSourceImpl.getInstance(getContext()), this);

        if (!getResources().getBoolean(R.bool.isTablet)) {
            orientationEventListener = new OrientationEventListener(getContext(), SensorManager.SENSOR_DELAY_NORMAL) {
                @Override
                public void onOrientationChanged(int i) {
                    if (orientationPortrait) {
                        if (isPortrait(i)) {
                            disable();
                            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                        }
                    } else {
                        if (isLandscape(i)) {
                            disable();
                            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                        }
                    }
                }
            };
        }

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

    private boolean isPortrait(int orientation) {
        return (orientation >= 350 && orientation <= 360) || (orientation >= 0 && orientation <= 10) ;
    }

    private boolean isLandscape(int orientation) {
        return (orientation >= 250 && orientation <= 280) || (orientation >= 80 && orientation <= 100) ;
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
            orientationEventListener.enable();
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                btnFullScreen.setImageResource(R.drawable.icon_fullscreen);
            } else {
                btnFullScreen.setImageResource(R.drawable.icon_fullscreen_exit);
                layoutView.setSystemUiVisibility(
//                        View.SYSTEM_UI_FLAG_LOW_PROFILE
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
        if (!getResources().getBoolean(R.bool.isTablet)) {
            orientationEventListener.disable();
        }
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
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void showTextDescription(String description) {
        TextView tvStepDescription = layoutView.findViewById(R.id.tv_step_description);
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

//            // Set the ExoPlayer.EventListener to this activity.
        }
    }

    @Override
    public void showImageBy(Uri imageUri) {
        imageStep.setVisibility(View.VISIBLE);
        playerView.setVisibility(View.GONE);
        Picasso
            .with(getContext())
            .load(imageUri)
            .into(imageStep);
    }

    @Override
    public void showMissingMedia() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        playerView.setVisibility(View.GONE);
        imageStep.setVisibility(View.VISIBLE);
    }

    public void setStep(Step step) {
        this.stepRecipe = step;
    }

    private void releasePlayer() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

}
