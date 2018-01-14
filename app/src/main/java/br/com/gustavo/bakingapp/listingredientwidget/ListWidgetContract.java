package br.com.gustavo.bakingapp.listingredientwidget;

import br.com.gustavo.bakingapp.BasePresenter;
import br.com.gustavo.bakingapp.BaseView;
import br.com.gustavo.bakingapp.data.model.Recipe;

/**
 * Created by gustavomagalhaes on 1/8/18.
 */

public interface ListWidgetContract {
    interface View extends BaseView<Presenter> {
        void showListOf(Recipe favorite);

        void showNotListIngredient();
    }

    interface Presenter extends BasePresenter {

    }
}
