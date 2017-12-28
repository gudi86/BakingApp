package br.com.gustavo.bakingapp.masterrecipe.stepdetailrecipe;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.os.ConfigurationCompat;
import android.support.v4.util.Preconditions;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import br.com.gustavo.bakingapp.R;
import br.com.gustavo.bakingapp.data.model.Step;
import br.com.gustavo.bakingapp.data.source.BakingDataSourceImpl;
import br.com.gustavo.bakingapp.masterrecipe.MasterStepDetailActivity;

/**
 * Created by gustavomagalhaes on 12/2/17.
 */

public class StepDetailActivity extends AppCompatActivity implements StepDetailContract.Extern.View {//, StepDetailFragment.OnEventFullscreen{

    private static final String TAG_LOG = StepDetailActivity.class.getName();

    private Button buttonNext;
    private Button buttonPrevious;

    private StepDetailContract.Extern.Presenter presenter;
    private StepDetailFragment stepDetailFragment;
    private Bundle savedState = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_step_detail);

        buttonNext = findViewById(R.id.btn_next_step);
        buttonPrevious = findViewById(R.id.btn_previous_step);

        new StepDetailExternPresenter(BakingDataSourceImpl.getInstance(getBaseContext()), this);

        if (savedInstanceState != null && savedInstanceState.containsKey("frag")) {
            savedState = savedInstanceState;
        }

        if (getIntent() != null) {
            List<Step> steps = getIntent().getParcelableArrayListExtra(MasterStepDetailActivity.STEP_RECIPE);
            presenter.loadStepByIndex(steps,
                    getIntent().getIntExtra(MasterStepDetailActivity.IDX_STEP, 0));
        } else {
            Log.d(TAG_LOG, "Intent is null");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        savedState = null;

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "frag", stepDetailFragment);
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
        if (buttonPrevious != null) {
            buttonPrevious.setEnabled(false);
        }
    }

    @Override
    public void disableNextButton() {
        if (buttonNext != null) {
            buttonNext.setEnabled(false);
        }
    }

    @Override
    public void enableNextButton() {
        if (buttonNext != null) {
            buttonNext.setEnabled(true);
        }
    }

    @Override
    public void enablePreviousButton() {
        if (buttonPrevious != null) {
            buttonPrevious.setEnabled(true);
        }
    }

    @Override
    public void showStep(Step step) {
        if (savedState == null) {
            stepDetailFragment = new StepDetailFragment();
        } else {
            stepDetailFragment = (StepDetailFragment) getSupportFragmentManager().getFragment(savedState, "frag");
        }
        getIntent().putExtra(
                MasterStepDetailActivity.IDX_STEP,
                getIntent().getParcelableArrayListExtra(MasterStepDetailActivity.STEP_RECIPE).indexOf(step));

        stepDetailFragment.setStep(step);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container_step_detail, stepDetailFragment).commitNow();
    }
}
