package br.com.gustavo.bakingapp.listingredientwidget;

import java.util.List;

import br.com.gustavo.bakingapp.data.model.Ingredient;
import br.com.gustavo.bakingapp.data.source.BakingDataSource;
import br.com.gustavo.bakingapp.data.source.BakingDataSourceImpl;
import br.com.gustavo.bakingapp.data.source.database.BakingDataBase;

/**
 * Created by gustavomagalhaes on 1/8/18.
 */

public class ListWidgetPresenter implements ListWidgetContract.Presenter {

    private BakingDataSource data;
    private ListWidgetContract.View view;

    public ListWidgetPresenter(BakingDataSource dataSource, ListWidgetContract.View view) {

        this.data = dataSource;
        this.view = view;

        this.view.setPresenter(this);
    }

    @Override
    public void start() {
       data.fetchFavorite(new BakingDataBase.OnFetchIngredient() {
            @Override
            public void onfetchFavorite(List<Ingredient> ingredients) {
                if (ingredients != null && ingredients.size() > 0) {
                    view.showListOf(ingredients);
                } else {
                    view.showNotListIngredient();
                }
            }
        });
    }
}
