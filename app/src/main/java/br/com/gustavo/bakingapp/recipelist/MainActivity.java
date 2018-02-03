package br.com.gustavo.bakingapp.recipelist;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.gustavo.bakingapp.R;
import br.com.gustavo.bakingapp.data.model.Recipe;
import br.com.gustavo.bakingapp.data.source.BakingDataSourceImpl;
import br.com.gustavo.bakingapp.listingredientwidget.IngredientWidget;
import br.com.gustavo.bakingapp.masterrecipe.MasterStepDetailActivity;

public class MainActivity extends AppCompatActivity implements RecipeListContract.View, AdapterRecipes.OnClickRecipe {

    private static final String LOG_TAG = MainActivity.class.getName();

    public static final String RECIPE = "RECIPE";

    private RecipeListContract.Presenter presenter;

    private RecyclerView recyclerView;
    private ViewGroup layoutError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        new RecipeListPresenter(BakingDataSourceImpl.getInstance(getBaseContext()), this);

        recyclerView = findViewById(R.id.rv_recipes);
        layoutError = findViewById(R.id.ll_wrong_data);

        RecyclerView.LayoutManager layoutManager;
        if (getResources().getBoolean(R.bool.isTablet)) {
            layoutManager = new GridLayoutManager(this, 3);
        } else {
            layoutManager = new LinearLayoutManager(this);
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        presenter.start();
    }

    @Override
    public void setPresenter(@NonNull RecipeListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showRecipes(List<Recipe> recipes, Recipe favorite) {
        isWrongData(false);
        recyclerView.setAdapter(new AdapterRecipes(recipes, favorite, this));

        if ( getIntent().getAction().equals(IngredientWidget.LOAD_RECIPE_FAVORITE) ) {
            getIntent().setAction(Intent.ACTION_MAIN);
            onClickRecipeItem(favorite);
        }
    }

    public void clickTryAgain(View view) {
        presenter.start();
    }

    @Override
    public void onClickRecipeItem(Recipe recipe) {
        Log.d(LOG_TAG, recipe.getName() + " was clicked!");
        presenter.openSelected(recipe);
    }

    @Override
    public void onClickFavorite(Recipe recipe) {
        Log.d(LOG_TAG, "Clicou no button favorite");
        presenter.saveFavorite(recipe);
    }

    @Override
    public void showNoRecipes() {
        isWrongData(true);
    }

    @Override
    public void showSelected(Recipe recipe) {
        isWrongData(false);
        Intent intent = new Intent(this, MasterStepDetailActivity.class);
        intent.putExtra(RECIPE, recipe);
        startActivity(intent);
    }

    @Override
    public void showConfirmAddFavorite() {
        Intent intent = new Intent(this, IngredientWidget.class);
        intent.setAction(IngredientWidget.ADD_LIST_INGREDIENTS_FAVORITE);
        sendBroadcast(intent);
    }

    @Override
    public void showNotAddFavorite() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
        builder.setTitle(getString(R.string.lbl_alert))
                .setMessage(getString(R.string.msg_add_favorite))
                .setPositiveButton(android.R.string.yes, null)
                .show();
    }

    @Override
    public void showNonFavoriteRecipe() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
        builder.setTitle(getString(R.string.lbl_alert))
                .setMessage(getString(R.string.msg_recipe_favorite_yet))
                .setPositiveButton(android.R.string.yes, null)
                .show();
    }

    private void isWrongData(boolean isWrong) {
        recyclerView.setVisibility(isWrong?View.GONE:View.VISIBLE);
        layoutError.setVisibility(isWrong?View.VISIBLE:View.GONE);
    }

}
