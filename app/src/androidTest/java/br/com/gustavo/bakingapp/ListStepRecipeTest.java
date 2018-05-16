package br.com.gustavo.bakingapp;

import android.content.Intent;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import br.com.gustavo.bakingapp.data.model.Ingredient;
import br.com.gustavo.bakingapp.data.model.Recipe;
import br.com.gustavo.bakingapp.data.model.Step;
import br.com.gustavo.bakingapp.masterrecipe.MasterRecipeActivity;
import br.com.gustavo.bakingapp.masterrecipe.RecipeIdlingResource;
import br.com.gustavo.bakingapp.recipelist.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * Created by gustavomagalhaes on 2/4/18.
 */

@RunWith(AndroidJUnit4.class)
public class ListStepRecipeTest {

    @Rule
    public ActivityTestRule<MasterRecipeActivity> ruleMasterStepDetailActivity
            = new ActivityTestRule(MasterRecipeActivity.class) {


        @Override
        protected Intent getActivityIntent() {
            List<Ingredient> ingredients = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                Ingredient ingredient = new Ingredient();
                ingredient.setQuantity(1.5);
                ingredient.setMeasure("CUP");
                ingredient.setIngredient("Chocolate");
                ingredients.add(ingredient);
            }

            List<Step> steps = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Step step = new Step();
                step.setId(i);
                step.setDescription("Long Description");
                step.setShortDescription("Short Description " + (i + 1));
                step.setThumbnailUrl("");
                step.setVideoUrl("");
                steps.add(step);
            }

            recipe = new Recipe();
            recipe.setName("Recipe name");
            recipe.setId(1);
            recipe.setServings(5);
            recipe.setIngredients(ingredients);
            recipe.setSteps(steps);

            Intent intent = new Intent();
            intent.putExtra(MainActivity.RECIPE, recipe);
            return intent;
        }

    };



    private Recipe recipe;
    private RecipeIdlingResource recipeIdlingResource = null;


    @Before
    public void setIntent() {
//        Intents.init();
        recipeIdlingResource = ruleMasterStepDetailActivity.getActivity().getRecipeListIdle();
        IdlingRegistry.getInstance().register(recipeIdlingResource);

    }

    @Test
    public void checkOpenRecipe() {
        StringBuffer labelIngredient = new StringBuffer("");
        for (Ingredient i : recipe.getIngredients()) {
            labelIngredient
                    .append(i.getQuantity())
                    .append(" ")
                    .append(i.getMeasure())
                    .append(" ")
                    .append(i.getIngredient())
                    .append("\n");
        }
        onView(withId(R.id.tv_ingredient)).check(matches(withText(labelIngredient.toString())));

        onView(withId(R.id.list_step_detail)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

//        onView(withText(recipe.getSteps().get(1).getShortDescription()))
//                .perform(click());

        if (doesViewExist(R.id.tv_step_more_description)) {
            onView(withId(R.id.tv_step_more_description)).check(matches(withText(recipe.getSteps().get(1).getDescription())));
        }
    }

    public boolean doesViewExist(int id) {
        try {
            onView(withId(id)).check(matches(isDisplayed()));
            return true;
        } catch (NoMatchingViewException e) {
            return false;
        }
    }

    @After
    public void finish() {

        if (recipeIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(recipeIdlingResource);
        }

    }

}
