package br.com.gustavo.bakingapp.listingredientwidget;

import java.util.List;

import br.com.gustavo.bakingapp.BasePresenter;
import br.com.gustavo.bakingapp.BaseView;
import br.com.gustavo.bakingapp.data.model.Ingredient;

/**
 * Created by gustavomagalhaes on 1/8/18.
 */

public interface ListWidgetContract {
    interface View extends BaseView<Presenter> {
        void showListOf(List<Ingredient> ingredients);

        void showNotListIngredient();
    }

    interface Presenter extends BasePresenter {

    }
}
