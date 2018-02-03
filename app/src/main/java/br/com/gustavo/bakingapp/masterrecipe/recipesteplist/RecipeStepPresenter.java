package br.com.gustavo.bakingapp.masterrecipe.recipesteplist;

import br.com.gustavo.bakingapp.data.model.Recipe;
import br.com.gustavo.bakingapp.data.model.Step;
import br.com.gustavo.bakingapp.data.source.BakingDataSource;

/**
 * Created by gustavo on 09/12/17.
 */

public class RecipeStepPresenter implements RecipeStepContract.Presenter {

    private RecipeStepContract.View view;
    private BakingDataSource dataSource;


    public RecipeStepPresenter(BakingDataSource bakingDataSource, RecipeStepContract.View view) {
        this.dataSource = bakingDataSource;
        this.view = view;

        this.view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadRecipe(Recipe recipe) {
        if (recipe != null) {
            view.showListInstructions(recipe.getSteps(), recipe.getIngredients());
        } else {
            view.showFailLoadSteps();
        }
    }

    @Override
    public void openSelected(Step step) {
        view.showStep(step);
    }

}
