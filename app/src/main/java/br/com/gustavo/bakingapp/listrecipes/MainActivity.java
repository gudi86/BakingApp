package br.com.gustavo.bakingapp.listrecipes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import br.com.gustavo.bakingapp.R;
import br.com.gustavo.bakingapp.data.model.Recipe;
import br.com.gustavo.bakingapp.data.source.BakingDataSourceImpl;
import br.com.gustavo.bakingapp.masterrecipe.MasterStepDetailActivity;

/*
    TODO Atividades

    - Estruturar o MVP no projeto
    - Ajustar video de apresentação na tela dos passos
    - Implementar a tela de video em landscape
    - Criar o widget
    - Tratar eventos ao realizar do landscape
    - Ajustar layout
        - Layout em todas as tela
        - Na barra superior incluir o titulo



 */

public class MainActivity extends AppCompatActivity implements ListRecipesContract.View, AdapterRecipes.OnClickRecipe {

    private static final String TAG = MainActivity.class.getName();

    public static final String RECIPE = "RECIPE";

    private ListRecipesContract.Presenter presenter;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ListRecipesPresenter(BakingDataSourceImpl.getInstance(getBaseContext()), this);

        recyclerView = (RecyclerView) findViewById(R.id.rv_recipes);

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
        Log.d(TAG, "##### onResume is called");
        presenter.start();
    }

    @Override
    public void showRecipes(List<Recipe> recipes) {
        recyclerView.setAdapter(new AdapterRecipes(recipes, this));
    }

    @Override
    public void onClickRecipeItem(Recipe recipe) {
        Toast.makeText(getBaseContext(), "Clicou na receita " + recipe.getName(), Toast.LENGTH_SHORT).show();
        presenter.openSelected(recipe);
    }

    @Override
    public void showNoRecipes() {
        Toast.makeText(getBaseContext(), "Houve um erro para exibir as receitas!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSelected(Recipe recipe) {
        Intent intent = new Intent(this, MasterStepDetailActivity.class);
        intent.putExtra(RECIPE, recipe);
        startActivity(intent);
    }

}
