package br.com.gustavo.bakingapp.masterrecipe.liststeprecipe;

import br.com.gustavo.bakingapp.data.model.Step;
import br.com.gustavo.bakingapp.data.source.BakingDataSource;

/**
 * Created by gustavo on 09/12/17.
 */

public class StepRecipePresenter implements StepRecipeContract.Presenter {

    private StepRecipeContract.View view;
    private BakingDataSource dataSource;


    public StepRecipePresenter(BakingDataSource bakingDataSource, StepRecipeContract.View view) {
        this.dataSource = bakingDataSource;
        this.view = view;

        this.view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void openSelected(Step step) {
        view.showStep(step);
    }
}
