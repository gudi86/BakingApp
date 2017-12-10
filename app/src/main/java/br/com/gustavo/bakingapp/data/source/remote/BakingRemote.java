package br.com.gustavo.bakingapp.data.source.remote;

import java.util.List;

import br.com.gustavo.bakingapp.data.model.Recipe;

/**
 * Created by gustavomagalhaes on 11/25/17.
 */

public interface BakingRemote {

    interface OnFetchRecipe {
        public void onRecipeLoad(List<Recipe> recipes);

        public void onFailFetch(Throwable t);
    }

    void fetchRecipes(OnFetchRecipe fetchRecipe);

}
