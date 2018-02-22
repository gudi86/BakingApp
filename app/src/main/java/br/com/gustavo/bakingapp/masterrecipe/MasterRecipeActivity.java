package br.com.gustavo.bakingapp.masterrecipe;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.gustavo.bakingapp.R;
import br.com.gustavo.bakingapp.data.model.Recipe;
import br.com.gustavo.bakingapp.data.model.Step;
import br.com.gustavo.bakingapp.data.source.BakingDataSourceImpl;
import br.com.gustavo.bakingapp.recipelist.MainActivity;
import br.com.gustavo.bakingapp.masterrecipe.recipesteplist.RecipeStepListFragment;
import br.com.gustavo.bakingapp.masterrecipe.recipesteplist.RecipeStepPresenter;
import br.com.gustavo.bakingapp.masterrecipe.stepdetailrecipe.StepDetailActivity;
import br.com.gustavo.bakingapp.masterrecipe.stepdetailrecipe.StepDetailFragment;

/**
 * Created by gustavomagalhaes on 11/30/17.
 */

public class MasterRecipeActivity extends AppCompatActivity implements RecipeStepListFragment.OnSelectedStep, MasterRecipeContract.View {

    private static final String LOG_TAG = MasterRecipeActivity.class.getName();

    public static final  String STEP_RECIPE = "STEP_RECIPE";
    public static final String IDX_STEP = "IDX_STEP";

    private MasterRecipeContract.Presenter presenter;

    private Step currentStep = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_detail);

        if (savedInstanceState != null && savedInstanceState.containsKey("step")) {
            currentStep = savedInstanceState.getParcelable("step");
        }

        new MasterRecipePresenter(BakingDataSourceImpl.getInstance(getBaseContext()), this);

        RecipeStepListFragment stepRecipeFragment = (RecipeStepListFragment) getSupportFragmentManager().findFragmentById(R.id.list_step_detail);
        new RecipeStepPresenter(BakingDataSourceImpl.getInstance(getBaseContext()), stepRecipeFragment);

        presenter.loadStepDetail((Recipe) getIntent().getParcelableExtra(MainActivity.RECIPE), currentStep);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("step", currentStep);
    }

    @Override
    public void onClickSelectedStep(Step step) {
        presenter.openNewStep(step);
    }

    @Override
    public void setPresenter(MasterRecipeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showStepDetail(Step step) {
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        currentStep = step;
        stepDetailFragment.setStep(step);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container_step_detail, stepDetailFragment).commitNow();
    }

    @Override
    public void showListStepsByIndex(List<Step> steps, Integer index) {
        Intent intent = new Intent(this, StepDetailActivity.class);
        intent.putParcelableArrayListExtra(STEP_RECIPE, (ArrayList<? extends Parcelable>) steps);
        intent.putExtra(IDX_STEP, index);
        startActivity(intent);
    }

    @Override
    public boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }

}
