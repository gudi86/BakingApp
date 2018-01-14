package br.com.gustavo.bakingapp.data.source.database;

import br.com.gustavo.bakingapp.data.model.Recipe;

/**
 * Created by gustavomagalhaes on 11/25/17.
 */

public interface BakingDataBase {

    interface OnSaveIngredient {
        void onSave();

        void onFail();
    }

    interface OnRemoveIngredient {
        void onRemove();

        void onFail();
    }

    interface OnFetchRecipeFavorite {
        void onfetchFavorite(Recipe favorite);
    }

    void addFavorite(Recipe recipe, OnSaveIngredient callback);

    void removeFavorite(OnRemoveIngredient callback);

    void fetchFavorite(OnFetchRecipeFavorite onFetchIngredient);

}
