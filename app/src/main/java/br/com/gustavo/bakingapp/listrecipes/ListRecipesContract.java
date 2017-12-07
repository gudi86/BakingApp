package br.com.gustavo.bakingapp.listrecipes;

import java.util.List;

import br.com.gustavo.bakingapp.BasePresenter;
import br.com.gustavo.bakingapp.BaseView;
import br.com.gustavo.bakingapp.data.model.Recipe;

/**
 * Created by gustavomagalhaes on 11/21/17.
 */

public interface ListRecipesContract {
    interface View extends BaseView<Presenter> {
        public void showRecipes(List<Recipe> recipes);

        public void showNoRecipes();

        public void showSelected(Recipe recipe);
    }

    interface Presenter extends BasePresenter {
        public void openSelected(Recipe recipe);
    }
}
