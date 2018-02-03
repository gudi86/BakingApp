package br.com.gustavo.bakingapp.masterrecipe.recipesteplist;

import java.util.List;

import br.com.gustavo.bakingapp.BasePresenter;
import br.com.gustavo.bakingapp.BaseView;
import br.com.gustavo.bakingapp.data.model.Ingredient;
import br.com.gustavo.bakingapp.data.model.Recipe;
import br.com.gustavo.bakingapp.data.model.Step;

/**
 * Created by gustavo on 09/12/17.
 */

public interface RecipeStepContract {
    interface View extends BaseView<Presenter> {
        void showStep(Step step);

        void showListInstructions(List<Step> steps, List<Ingredient> ingredients);

        void showFailLoadSteps();
    }

    interface Presenter extends BasePresenter {

        void openSelected(Step step);

        void loadRecipe(Recipe recipe);
    }
}
