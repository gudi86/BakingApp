package br.com.gustavo.bakingapp.listrecipes;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import br.com.gustavo.bakingapp.data.model.Recipe;
import br.com.gustavo.bakingapp.data.source.BakingDataSource;
import br.com.gustavo.bakingapp.data.source.remote.BakingRemote;

/**
 * Created by gustavomagalhaes on 11/21/17.
 */

public class ListRecipesPresenter implements ListRecipesContract.Presenter {

    private static final String LOG_TAG = ListRecipesPresenter.class.getName();
    private ListRecipesContract.View view;

    private BakingDataSource dataSource;

    public ListRecipesPresenter(@NonNull BakingDataSource bakingDataSource, @NonNull ListRecipesContract.View view) {

        dataSource = bakingDataSource;
        this.view = view;

        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        dataSource.fetchRecipes(new BakingRemote.OnFetchRecipe() {
            @Override
            public void onRecipeLoad(List<Recipe> recipes) {
                view.showRecipes(recipes);
            }

            @Override
            public void onFailFetch(Throwable t) {
                Log.d(LOG_TAG, "Happening an error: ", t);
                view.showNoRecipes();
            }
        });
    }

    @Override
    public void openSelected(@NonNull Recipe recipe) {
        view.showSelected(recipe);
    }
}
