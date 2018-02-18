package br.com.gustavo.bakingapp.masterrecipe;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;

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

    public static final  String STEP_RECIPE = "STEP_RECIPE";
    public static final String IDX_STEP = "IDX_STEP";

    private MasterRecipeContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_detail);

        if (getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        new MasterRecipePresenter(BakingDataSourceImpl.getInstance(getBaseContext()), this);

        RecipeStepListFragment stepRecipeFragment = (RecipeStepListFragment) getSupportFragmentManager().findFragmentById(R.id.list_step_detail);
        new RecipeStepPresenter(BakingDataSourceImpl.getInstance(getBaseContext()), stepRecipeFragment);

        presenter.loadStepDetail((Recipe) getIntent().getParcelableExtra(MainActivity.RECIPE));
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
