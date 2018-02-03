package br.com.gustavo.bakingapp.listingredientwidget;

import br.com.gustavo.bakingapp.data.model.Recipe;
import br.com.gustavo.bakingapp.data.source.BakingDataSource;
import br.com.gustavo.bakingapp.data.source.database.BakingDataBase;

/**
 * Created by gustavomagalhaes on 1/8/18.
 */

public class WidgetListPresenter implements WidgetListContract.Presenter {

    private BakingDataSource data;
    private WidgetListContract.View view;

    public WidgetListPresenter(BakingDataSource dataSource, WidgetListContract.View view) {

        this.data = dataSource;
        this.view = view;

        this.view.setPresenter(this);
    }

    @Override
    public void start() {
       data.fetchFavorite(new BakingDataBase.OnFetchRecipeFavorite() {
            @Override
            public void onfetchFavorite(Recipe favorite) {
                if (favorite != null) {
                    view.showListOf(favorite);
                } else {
                    view.showNotListIngredient();
                }
            }
        });
    }
}
