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
        void showRecipes(List<Recipe> recipes);

        void showNoRecipes();

        void showSelected(Recipe recipe);

        void showConfirmAddFavorite();

        void showNotAddFavorite();
    }

    interface Presenter extends BasePresenter {
        void openSelected(Recipe recipe);

        void saveFavorite(Recipe recipe);
    }
}
