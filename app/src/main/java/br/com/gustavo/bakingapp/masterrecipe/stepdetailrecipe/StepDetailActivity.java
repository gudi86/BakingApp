package br.com.gustavo.bakingapp.masterrecipe.stepdetailrecipe;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.os.ConfigurationCompat;
import android.support.v4.util.Preconditions;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import br.com.gustavo.bakingapp.R;
import br.com.gustavo.bakingapp.data.model.Step;
import br.com.gustavo.bakingapp.data.source.BakingDataSourceImpl;
import br.com.gustavo.bakingapp.masterrecipe.MasterStepDetailActivity;

/**
 * Created by gustavomagalhaes on 12/2/17.
 */

public class StepDetailActivity extends AppCompatActivity implements StepDetailContract.Extern.View{

    private static final String TAG_LOG = StepDetailActivity.class.getName();

    private Button buttonNext;
    private Button buttonPrevious;

    private StepDetailContract.Extern.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG_LOG, "Orientation is portrait: " + (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT));

        setContentView(R.layout.activity_step_detail);

        buttonNext = findViewById(R.id.btn_next_step);
        buttonPrevious = findViewById(R.id.btn_previous_step);

        new StepDetailExternPresenter(BakingDataSourceImpl.getInstance(getBaseContext()), this);

        if (getIntent() != null) {
            List<Step> steps = getIntent().getParcelableArrayListExtra(MasterStepDetailActivity.STEP_RECIPE);
            presenter.loadStepByIndex(steps,
                    getIntent().getIntExtra(MasterStepDetailActivity.IDX_STEP, 0));
        } else {
            Log.d(TAG_LOG, "Intent is null");
        }

    }

    public void clickNewStep(View view) {
        Log.d(TAG_LOG, "clicou para obter o pxóximo passo");
        if (view.getId() == R.id.btn_next_step) {
            presenter.loadNextStep();
        } else if (view.getId() == R.id.btn_previous_step) {
            presenter.loadPreviousStep();
        }
    }

    @Override
    public void setPresenter(StepDetailContract.Extern.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void disablePreviousButton() {
        buttonPrevious.setEnabled(false);
    }

    @Override
    public void disableNextButton() {
        buttonNext.setEnabled(false);
    }

    @Override
    public void enableNextButton() {
        buttonNext.setEnabled(true);
    }

    @Override
    public void enablePreviousButton() {
        buttonPrevious.setEnabled(true);
    }

    @Override
    public void showStep(Step step) {
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setStep(step);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container_step_detail, stepDetailFragment).commitNow();
    }
}
