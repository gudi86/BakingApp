package br.com.gustavo.bakingapp.data.source.database;

import android.database.ContentObserver;

import java.util.List;

import br.com.gustavo.bakingapp.data.model.Ingredient;
import br.com.gustavo.bakingapp.data.model.Recipe;

/**
 * Created by gustavomagalhaes on 11/25/17.
 */

public interface BakingDataBase {

    interface OnSaveIngredient {
        void onSave();

        void onFail();
    }

    interface OnFetchIngredient {
        void onfetchFavorite(List<Ingredient> ingredients);
    }

    void addFavorite(Recipe recipe, OnSaveIngredient callback);

    void fetchFavorite(OnFetchIngredient onFetchIngredient);

}
