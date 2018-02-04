package br.com.gustavo.bakingapp;

import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
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

import br.com.gustavo.bakingapp.data.model.Ingredient;
import br.com.gustavo.bakingapp.data.model.Recipe;
import br.com.gustavo.bakingapp.data.model.Step;
import br.com.gustavo.bakingapp.masterrecipe.MasterRecipeActivity;
import br.com.gustavo.bakingapp.recipelist.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.gustavo.bakingapp.TestUtil.withRecyclerView;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;

/**
 * Created by gustavomagalhaes on 2/4/18.
 */

@RunWith(AndroidJUnit4.class)
public class MasterStepDetailTest {

    @Rule
    public ActivityTestRule<MasterRecipeActivity> ruleMasterStepDetailActivity
            = new ActivityTestRule(MasterRecipeActivity.class, true, false);

    private Recipe recipe;


    @Before
    public void setIntent() {
        System.out.print("Teste Before");

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
            step.setShortDescription("Short Description");
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

        Intents.init();

    }

    @Test
    public void checkOpenRecipe() {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.RECIPE, recipe);
        ruleMasterStepDetailActivity.launchActivity(intent);
        ruleMasterStepDetailActivity.getActivity().getSupportFragmentManager().beginTransaction();

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

        onView(withId(R.id.tv_step_description)).check(matches(withText(recipe.getSteps().get(0).getShortDescription())));

//        onView(withId(R.id.rv_recipe_detail)).perform(RecyclerViewActions.scrollToPosition(1));
//        onView(withId(R.id.tv_step_description)).check(matches(withText(recipe.getSteps().get(0).getShortDescription())));

//        onView(withRecyclerView(R.id.rv_recipe_detail)
//                .atPositionOnView(1, R.id.tv_step_description))
//                .check(matches(withText(recipe.getSteps().get(0).getShortDescription())));


    }

    @After
    public void finish() {
        System.out.print("Teste After");
        Intents.release();
    }
}
