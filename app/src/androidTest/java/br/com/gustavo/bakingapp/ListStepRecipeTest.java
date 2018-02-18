package br.com.gustavo.bakingapp;

import android.content.Intent;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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

        recipeIdlingResource = ruleMasterStepDetailActivity.getActivity().getRecipeIdle();
        IdlingRegistry.getInstance().register(recipeIdlingResource);



//        Intents.init();
    }

    @Test
    public void checkOpenRecipe() {


//        ruleMasterStepDetailActivity.launchActivity(intent);
//        ruleMasterStepDetailActivity.getActivity().getSupportFragmentManager().beginTransaction();

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

        onView(withText(recipe.getSteps().get(1).getShortDescription()))
                .perform(click());

        onView(withId(R.id.tv_step_description))
                .check(matches(withText(recipe.getSteps().get(1).getDescription())));
    }

    @After
    public void finish() {

        if (recipeIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(recipeIdlingResource);
        }

//        Intents.release();
    }

    //
//    private static Matcher<View> withDataAtPosition(final int id, final String data, final int position) {
//        return new TypeSafeMatcher<View>() {
//
//            @Override
//            public void describeTo(Description description) {
//                description.appendText("My text");
////                describeTo(description);
//            }
//
//            @Override
//            protected boolean matchesSafely(View item) {
//
//                if (!(item instanceof RecyclerView)) {
//                    return false;
//                }
//                RecyclerView recyclerView = (RecyclerView) item;
//
//                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
//
//                TextView view = viewHolder.itemView.findViewById(id);
//
//                if (view.getText().equals(data)) {
//                    return true;
//                }
//
//                return false;
//            }
//        };
//    }
}
