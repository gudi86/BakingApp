package br.com.gustavo.bakingapp.masterrecipe.stepdetailrecipe;

import android.net.Uri;

import br.com.gustavo.bakingapp.data.model.Step;
import br.com.gustavo.bakingapp.data.source.BakingDataSource;

/**
 * Created by gustavo on 10/12/17.
 */

public class StepDetailInternPresenter implements StepDetailContract.Intern.Presenter {

    private final StepDetailContract.Intern.View view;
    private final BakingDataSource dataSource;
    private Step step;

    public StepDetailInternPresenter(BakingDataSource bakingDataSource, StepDetailContract.Intern.View view) {
        this.view = view;
        this.dataSource = bakingDataSource;

        this.view.setPresenter(this);

    }

    @Override
    public void start() {

    }

    @Override
    public void loadStep(Step step) {
        this.step = step;
        view.showTextDescription(step.getDescription());

        if (!step.getVideoUrl().equals("")) {
            view.showVideoBy(Uri.parse(step.getVideoUrl()));
        } else if(!step.getThumbnailUrl().equals("")) {
            Uri uriMedia = Uri.parse(step.getThumbnailUrl());
            if (uriMedia.getLastPathSegment().endsWith("mp4")) {
                view.showMissingMedia();
            } else {
                view.showImageBy(Uri.parse(step.getThumbnailUrl()));
            }
        } else {
            view.showMissingMedia();
        }
    }
}
