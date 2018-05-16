package br.com.gustavo.bakingapp.masterrecipe.recipesteplist;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.gustavo.bakingapp.R;
import br.com.gustavo.bakingapp.data.model.Ingredient;
import br.com.gustavo.bakingapp.data.model.Recipe;
import br.com.gustavo.bakingapp.data.model.Step;
import br.com.gustavo.bakingapp.masterrecipe.RecipeIdlingResource;
import br.com.gustavo.bakingapp.recipelist.MainActivity;

/**
 * Created by gustavomagalhaes on 11/27/17.
 */

public class RecipeStepListFragment extends Fragment implements AdapterStepDetail.OnClickStep, RecipeStepContract.View{

    private static final String LOG_TAG = RecipeStepListFragment.class.getName();
    private RecipeStepContract.Presenter presenter;
    private RecyclerView lstRecipeStep;

    private Parcelable listState = null;
    private Recipe recipe;

    private RecipeIdlingResource recipeIdlingResource = null;

    public interface OnSelectedStep {
        void onClickSelectedStep(Step step);
    }

    private OnSelectedStep onCallbackSelectStep = null;

    public RecipeStepListFragment() {

    }

    public void setIdleTest(RecipeIdlingResource idleTest) {
        this.recipeIdlingResource = idleTest;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onCallbackSelectStep = (OnSelectedStep) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must be implment OnSelectedStep");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            if ( savedInstanceState.containsKey("list") )
                listState = savedInstanceState.getParcelable("list");
            if (savedInstanceState.containsKey("recipe"))
                recipe = savedInstanceState.getParcelable("recipe");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        lstRecipeStep = (RecyclerView) inflater.inflate(R.layout.fragment_list_recipe_step, container, false);
        return lstRecipeStep;
    }

    @Override
    public void setPresenter(RecipeStepContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("recipe", recipe);
        outState.putParcelable("list", lstRecipeStep.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity().getIntent().hasExtra(MainActivity.RECIPE))
            recipe = getActivity().getIntent().getParcelableExtra(MainActivity.RECIPE);
        presenter.loadRecipe(recipe);
    }

    @Override
    public void onClickStep(Step step) {
        Log.d(LOG_TAG, "Clicou no step: " + step.getShortDescription());
        presenter.openSelected(step);
    }

    @Override
    public void showStep(Step step) {
        onCallbackSelectStep.onClickSelectedStep(step);
    }

    @Override
    public void showListInstructions(List<Step> steps, List<Ingredient> ingredients) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.onRestoreInstanceState(listState);
        lstRecipeStep.setLayoutManager(layoutManager);
        lstRecipeStep.setAdapter(new AdapterStepDetail(ingredients, steps, this));

        if (recipeIdlingResource != null)
            recipeIdlingResource.setIdleNow(true);
    }

    @Override
    public void showFailLoadSteps() {
        Log.d(LOG_TAG, "Recipe isn't loaded");
    }
}