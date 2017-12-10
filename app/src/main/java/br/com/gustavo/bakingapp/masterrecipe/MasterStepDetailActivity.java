package br.com.gustavo.bakingapp.masterrecipe;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import br.com.gustavo.bakingapp.R;
import br.com.gustavo.bakingapp.data.model.Recipe;
import br.com.gustavo.bakingapp.data.model.Step;
import br.com.gustavo.bakingapp.data.source.BakingDataSourceImpl;
import br.com.gustavo.bakingapp.listrecipes.MainActivity;
import br.com.gustavo.bakingapp.masterrecipe.liststeprecipe.ListStepRecipeFragment;
import br.com.gustavo.bakingapp.masterrecipe.liststeprecipe.StepRecipePresenter;
import br.com.gustavo.bakingapp.masterrecipe.stepdetailrecipe.StepDetailActivity;
import br.com.gustavo.bakingapp.masterrecipe.stepdetailrecipe.StepDetailFragment;

/**
 * Created by gustavomagalhaes on 11/30/17.
 */

public class MasterStepDetailActivity extends AppCompatActivity implements ListStepRecipeFragment.OnSelectedStep, MasterRecipeContract.View {

    public static final  String STEP_RECIPE = "STEP_RECIPE";
    public static final String IDX_STEP = "IDX_STEP";

    private Recipe recipe;
    private MasterRecipeContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_detail);

        new MasterRecipePresenter(BakingDataSourceImpl.getInstance(getBaseContext()), this);

        ListStepRecipeFragment stepRecipeFragment = (ListStepRecipeFragment) getSupportFragmentManager().findFragmentById(R.id.list_step_detail);
        new StepRecipePresenter(BakingDataSourceImpl.getInstance(getBaseContext()), stepRecipeFragment);

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
