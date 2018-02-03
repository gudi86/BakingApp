package br.com.gustavo.bakingapp.recipelist;

import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import br.com.gustavo.bakingapp.data.model.Recipe;
import br.com.gustavo.bakingapp.data.source.BakingDataSource;
import br.com.gustavo.bakingapp.data.source.database.BakingDataBase;
import br.com.gustavo.bakingapp.data.source.remote.BakingRemote;

/**
 * Created by gustavomagalhaes on 11/21/17.
 */

public class RecipeListPresenter implements RecipeListContract.Presenter {

    private static final String LOG_TAG = RecipeListPresenter.class.getName();
    private RecipeListContract.View view;

    private BakingDataSource dataSource;

    public RecipeListPresenter(@NonNull BakingDataSource bakingDataSource, @NonNull RecipeListContract.View view) {

        dataSource = bakingDataSource;
        this.view = view;

        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        dataSource.fetchRecipes(new BakingRemote.OnFetchRecipe() {
            @Override
            public void onRecipeLoad(final List<Recipe> recipes) {
                new android.os.Handler(Looper.myLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        dataSource.fetchFavorite(new BakingDataBase.OnFetchRecipeFavorite() {
                            @Override
                            public void onfetchFavorite(Recipe favorite) {
                                if (favorite != null) {
                                    dataSource.fetchRecipeBy(favorite.getId(), new BakingDataSource.IFetchRecipe() {
                                        @Override
                                        public void getRecipe(Recipe recipe) {
                                            if (recipe != null) {
                                                view.showRecipes(recipes, recipe);
                                            }
                                        }
                                    });
                                } else {
                                    view.showRecipes(recipes, null);
                                }
                            }
                        });
                    }
                });
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

    @Override
    public void saveFavorite(Recipe recipe) {
        if (recipe != null) {
            dataSource.addFavorite(recipe, new BakingDataBase.OnSaveIngredient() {
                @Override
                public void onSave() {
                    view.showConfirmAddFavorite();
                }

                @Override
                public void onFail() {
                    view.showNotAddFavorite();
                }
            });
        } else {
            dataSource.removeFavorite(new BakingDataBase.OnRemoveIngredient() {
                @Override
                public void onRemove() {
                    view.showConfirmAddFavorite();
                }

                @Override
                public void onFail() {
                    view.showNotAddFavorite();
                }
            });
        }
    }

}
