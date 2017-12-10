package br.com.gustavo.bakingapp.listrecipes;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import br.com.gustavo.bakingapp.masterrecipe.MasterStepDetailActivity;

public class MainActivity extends AppCompatActivity implements ListRecipesContract.View, AdapterRecipes.OnClickRecipe {

    private static final String LOG_TAG = MainActivity.class.getName();

    public static final String RECIPE = "RECIPE";

    private ListRecipesContract.Presenter presenter;

    private RecyclerView recyclerView;
    private ViewGroup layoutError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ListRecipesPresenter(BakingDataSourceImpl.getInstance(getBaseContext()), this);

        recyclerView = (RecyclerView) findViewById(R.id.rv_recipes);
        layoutError = (ViewGroup) findViewById(R.id.ll_wrong_data);

        RecyclerView.LayoutManager layoutManager;
        if (getResources().getBoolean(R.bool.isTablet)) {
            layoutManager = new GridLayoutManager(this, 3);
        } else {
            layoutManager = new LinearLayoutManager(this);
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void setPresenter(@NonNull ListRecipesContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "##### onResume is called");
        presenter.start();
    }

    @Override
    public void showRecipes(List<Recipe> recipes) {
        recyclerView.setAdapter(new AdapterRecipes(recipes, this));
    }

    @Override
    public void onClickRecipeItem(Recipe recipe) {
        Log.d(LOG_TAG, recipe.getName() + " was clicked!");
        presenter.openSelected(recipe);
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

    private void isWrongData(boolean isWrong) {
        recyclerView.setVisibility(isWrong?View.GONE:View.VISIBLE);
        layoutError.setVisibility(isWrong?View.VISIBLE:View.GONE);
    }

}
