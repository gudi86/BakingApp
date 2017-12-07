package br.com.gustavo.bakingapp.masterrecipe;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.gustavo.bakingapp.R;
import br.com.gustavo.bakingapp.data.model.Recipe;
import br.com.gustavo.bakingapp.data.model.Step;
import br.com.gustavo.bakingapp.listrecipes.MainActivity;
import br.com.gustavo.bakingapp.masterrecipe.liststeprecipe.ListStepRecipeFragment;
import br.com.gustavo.bakingapp.masterrecipe.stepdetailrecipe.StepDetailActivity;
import br.com.gustavo.bakingapp.masterrecipe.stepdetailrecipe.StepDetailFragment;

/**
 * Created by gustavomagalhaes on 11/30/17.
 */

public class MasterStepDetailActivity extends AppCompatActivity implements ListStepRecipeFragment.OnSelectedStep {

    public final static String STEP_RECIPE = "STEP_RECIPE";

    private Recipe recipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_detail);

        recipe = getIntent().getParcelableExtra(MainActivity.RECIPE);

        if (getResources().getBoolean(R.bool.isTablet)) {
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setStep(recipe.getSteps().get(0));
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_container_step_detail, stepDetailFragment).commitNow();
        }
    }

    @Override
    public void onClickSelectedStep(Step step) {
        if (getResources().getBoolean(R.bool.isTablet)) {
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setStep(step);

            getSupportFragmentManager().beginTransaction().replace(R.id.fl_container_step_detail, stepDetailFragment).commitNow();
        } else {
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putParcelableArrayListExtra(STEP_RECIPE, (ArrayList<? extends Parcelable>) recipe.getSteps());
            intent.putExtra("IDX_STEP", recipe.getSteps().indexOf(step));
            startActivity(intent);
        }
    }
}
