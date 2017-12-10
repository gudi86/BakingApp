package br.com.gustavo.bakingapp.masterrecipe;

import android.content.res.Resources;

import br.com.gustavo.bakingapp.R;
import br.com.gustavo.bakingapp.data.model.Recipe;
import br.com.gustavo.bakingapp.data.model.Step;
import br.com.gustavo.bakingapp.data.source.BakingDataSource;

/**
 * Created by gustavo on 09/12/17.
 */

public class MasterRecipePresenter implements MasterRecipeContract.Presenter {

    private MasterRecipeContract.View view;
    private BakingDataSource dataSource;
    private Recipe currentRecipe;


    public MasterRecipePresenter(BakingDataSource bakingDataSource, MasterRecipeContract.View view) {
        this.dataSource = bakingDataSource;
        this.view = view;

        this.view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadStepDetail(Recipe current) {
        this.currentRecipe = current;
        if (view.isTablet()) {
            view.showStepDetail(currentRecipe.getSteps().get(0));
        }
    }

    @Override
    public void openNewStep(Step step) {
        if (view.isTablet()) {
            view.showStepDetail(step);
        } else {
            view.showListStepsByIndex(currentRecipe.getSteps(), currentRecipe.getSteps().indexOf(step));
        }
    }
}
