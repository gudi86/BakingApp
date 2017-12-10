package br.com.gustavo.bakingapp.masterrecipe;

import java.util.List;

import br.com.gustavo.bakingapp.BasePresenter;
import br.com.gustavo.bakingapp.BaseView;
import br.com.gustavo.bakingapp.data.model.Recipe;
import br.com.gustavo.bakingapp.data.model.Step;

/**
 * Created by gustavo on 09/12/17.
 */

public interface MasterRecipeContract {
    interface View extends BaseView<Presenter> {
        void showStepDetail(Step step);

        void showListStepsByIndex(List<Step> steps, Integer index);

        boolean isTablet();
    }

    interface Presenter extends BasePresenter {
        void loadStepDetail(Recipe current);

        void openNewStep(Step step);
    }
}
