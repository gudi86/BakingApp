package br.com.gustavo.bakingapp.masterrecipe.liststeprecipe;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import br.com.gustavo.bakingapp.R;
import br.com.gustavo.bakingapp.data.model.Recipe;
import br.com.gustavo.bakingapp.data.model.Step;
import br.com.gustavo.bakingapp.listrecipes.MainActivity;

/**
 * Created by gustavomagalhaes on 11/27/17.
 */

public class ListStepRecipeFragment extends Fragment implements AdapterStepDetail.OnClickStep{

    private static final String LOG_TAG = ListStepRecipeFragment.class.getName();

    public interface OnSelectedStep {
        public void onClickSelectedStep(Step step);
    }

    private RecyclerView rvStepDetails;

    private OnSelectedStep onCallbackSelectStep = null;

    public ListStepRecipeFragment() {

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View lstRecipeStep = inflater.inflate(R.layout.fragment_list_recipe_step, container, false);

        Recipe recipe = getActivity().getIntent().getParcelableExtra(MainActivity.RECIPE);
        if (recipe != null) {

            rvStepDetails = lstRecipeStep.findViewById(R.id.rv_recipe_detail);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            rvStepDetails.setLayoutManager(layoutManager);

            rvStepDetails.setAdapter(new AdapterStepDetail(recipe.getIngredients(), recipe.getSteps(), this));

        } else {
            Toast.makeText(getContext(), "Ocorreu um erro ao carregar a receita", Toast.LENGTH_SHORT).show();

        }

        return lstRecipeStep;
    }

    @Override
    public void onClickStep(Step step) {
        Log.d(LOG_TAG, "Clicou no step: " + step.getShortDescription());
        onCallbackSelectStep.onClickSelectedStep(step);
    }
}
