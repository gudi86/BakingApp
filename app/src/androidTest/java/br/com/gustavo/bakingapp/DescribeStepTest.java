package br.com.gustavo.bakingapp;

import android.content.Intent;
import android.os.Parcelable;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import br.com.gustavo.bakingapp.data.model.Step;
import br.com.gustavo.bakingapp.masterrecipe.MasterRecipeActivity;
import br.com.gustavo.bakingapp.masterrecipe.stepdetailrecipe.StepDetailActivity;
import br.com.gustavo.bakingapp.recipelist.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

/**
 * Created by gustavomagalhaes on 2/11/18.
 */
@RunWith(AndroidJUnit4.class)
public class DescribeStepTest {

    @Rule
    public ActivityTestRule<StepDetailActivity> ruleStepDetailActivity
            = new ActivityTestRule(StepDetailActivity.class, true, false);

    private List<Step> steps;

    @Before
    public void init() {

        steps = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Step step = new Step();
            step.setId(i);
            step.setDescription("Long Description " + (i + 1));
            step.setShortDescription("Short Description " + (i + 1));
            step.setThumbnailUrl("");
            step.setVideoUrl("");
            steps.add(step);
        }

        Intents.init();
    }

    @Test
    public void checkFirstStepRecipe() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(MasterRecipeActivity.STEP_RECIPE, (ArrayList<? extends Parcelable>) steps);
        ruleStepDetailActivity.launchActivity(intent);

        if (doesViewExist(R.id.btn_previous_step)) {
            onView(withId(R.id.btn_previous_step)).check(matches(not(isEnabled())));
        }
    }

    @Test
    public void checkNextStep() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(MasterRecipeActivity.STEP_RECIPE, (ArrayList<? extends Parcelable>) steps);
        ruleStepDetailActivity.launchActivity(intent);

        if (doesViewExist(R.id.btn_next_step)) {

            onView(withId(R.id.btn_next_step))
                    .perform(click())
                    .perform(click())
                    .perform(click())
                    .perform(click())
                    .perform(click());

            onView(withId(R.id.btn_next_step)).check(matches(not(isEnabled())));
        }
    }

    @After
    public void finish() {
        Intents.release();
    }

    public boolean doesViewExist(int id) {
        try {
            onView(withId(id)).check(matches(isDisplayed()));
            return true;
        } catch (NoMatchingViewException e) {
            return false;
        }
    }

}
