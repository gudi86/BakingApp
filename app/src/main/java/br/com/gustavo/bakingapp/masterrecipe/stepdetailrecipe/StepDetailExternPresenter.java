package br.com.gustavo.bakingapp.masterrecipe.stepdetailrecipe;

import java.util.List;

import br.com.gustavo.bakingapp.data.model.Step;
import br.com.gustavo.bakingapp.data.source.BakingDataSource;

/**
 * Created by gustavo on 10/12/17.
 */

public class StepDetailExternPresenter implements StepDetailContract.Extern.Presenter {


    private final StepDetailContract.Extern.View view;
    private final BakingDataSource dataSource;
    private List<Step> steps;
    private int index;

    public StepDetailExternPresenter(BakingDataSource bakingDataSource, StepDetailContract.Extern.View view) {
        this.view = view;
        this.dataSource = bakingDataSource;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadStepByIndex(List<Step> steps, int idx) {
        this.steps = steps;
        this.index = idx;
        loadStep();
    }

    private void loadStep() {
        if (this.index == 0) {
            this.view.disablePreviousButton();
        } else if ((this.steps.size()-1) == this.index) {
            this.view.disableNextButton();
        } else {
            this.view.enableNextButton();
            this.view.enablePreviousButton();
        }
        this.view.showStep(this.steps.get(index));
    }

    @Override
    public void loadNextStep() {
        index++;
        loadStep();
    }

    @Override
    public void loadPreviousStep() {
        index--;
        loadStep();
    }
}
