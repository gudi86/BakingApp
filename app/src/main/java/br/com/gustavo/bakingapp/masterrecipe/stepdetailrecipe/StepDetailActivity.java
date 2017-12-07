package br.com.gustavo.bakingapp.masterrecipe.stepdetailrecipe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

import br.com.gustavo.bakingapp.R;
import br.com.gustavo.bakingapp.data.model.Step;
import br.com.gustavo.bakingapp.masterrecipe.MasterStepDetailActivity;

/**
 * Created by gustavomagalhaes on 12/2/17.
 */

public class StepDetailActivity extends AppCompatActivity {

    private static final String TAG_LOG = StepDetailActivity.class.getName();

    private Button buttonNext;
    private Button buttonPrevious;

    private List<Step> steps;
    private int idx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_step_detail);

        buttonNext = findViewById(R.id.btn_next_step);
        buttonPrevious = findViewById(R.id.btn_previous_step);

        steps = getIntent().getParcelableArrayListExtra(MasterStepDetailActivity.STEP_RECIPE);
        idx = getIntent().getIntExtra("IDX_STEP", 0);

        controlButtonsPN();

        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setStep(steps.get(idx));

        getSupportFragmentManager().beginTransaction().add(R.id.fl_container_step_detail, stepDetailFragment).commitNow();
    }

    private void controlButtonsPN() {
        if (idx == 0) {
            buttonPrevious.setEnabled(false);
        } else if ((steps.size()-1) == idx) {
            buttonNext.setEnabled(false);
        } else {
            buttonPrevious.setEnabled(true);
            buttonNext.setEnabled(true);
        }
    }

    public void clickNewStep(View view) {
        Log.d(TAG_LOG, "clicou para obter o pxóximo passo");

            StepDetailFragment stepDetailFragment = new StepDetailFragment();
        if (view.getId() == R.id.btn_next_step) {
            stepDetailFragment.setStep(steps.get(++idx));

//            setLabelNextStep();
        } else if (view.getId() == R.id.btn_previous_step) {
            stepDetailFragment.setStep(steps.get(--idx));

//            setLabelNextStep();
        }

        controlButtonsPN();

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container_step_detail, stepDetailFragment).commitNow();
    }

    private void setLabelNextStep() {
        if ((steps.size() - 1) == idx) {
            buttonNext.setText(getResources().getString(R.string.lbl_back_initial));
        }
    }
}
