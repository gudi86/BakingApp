package br.com.gustavo.bakingapp.masterrecipe.stepdetailrecipe;

import android.net.Uri;

import java.util.List;

import br.com.gustavo.bakingapp.BasePresenter;
import br.com.gustavo.bakingapp.BaseView;
import br.com.gustavo.bakingapp.data.model.Step;

/**
 * Created by gustavo on 10/12/17.
 */

public interface StepDetailContract {

    interface Extern {

        interface View extends BaseView<Presenter> {

            void disablePreviousButton();

            void disableNextButton();

            void enableNextButton();

            void enablePreviousButton();

            void showStep(Step step);
        }

        interface Presenter extends BasePresenter {

            void loadStepByIndex(List<Step> steps, int idx);

            void loadNextStep();

            void loadPreviousStep();
        }
    }

    interface Intern {

        interface View extends BaseView<Presenter> {

            void showTextDescription(String description);

            void showVideoBy(Uri videoUri);

            void showMissingMedia();

            void showImageBy(Uri imageUri);
        }

        interface Presenter extends BasePresenter {

            void loadStep(Step step);
        }
    }
}
