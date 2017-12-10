package br.com.gustavo.bakingapp.masterrecipe.liststeprecipe;

import br.com.gustavo.bakingapp.BasePresenter;
import br.com.gustavo.bakingapp.BaseView;
import br.com.gustavo.bakingapp.data.model.Step;

/**
 * Created by gustavo on 09/12/17.
 */

public interface StepRecipeContract {
    interface View extends BaseView<Presenter> {
        void showStep(Step step);
    }

    interface Presenter extends BasePresenter {

        void openSelected(Step step);
    }
}
