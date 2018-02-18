package br.com.gustavo.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.gustavo.bakingapp.masterrecipe.MasterRecipeActivity;
import br.com.gustavo.bakingapp.recipelist.MainActivity;
import br.com.gustavo.bakingapp.masterrecipe.RecipeIdlingResource;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RecipeListTest {

//    @Rule public ActivityTestRule<>
    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);

    private RecipeIdlingResource recipeListIdlingResource;

    @Before
    public void init() {
        recipeListIdlingResource = mainActivity.getActivity().getRecipeListIdle();
        IdlingRegistry.getInstance().register(recipeListIdlingResource);
    }

    /**
     * This test checks if when click on item in Recycle View, it opens MasterRecipeActivity
     * @throws Exception
     */
    @Test
    public void checkRecipeList() throws Exception {
        Intents.init();

        onView(withId(R.id.rv_recipes)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Check if activity is open
        Intents.intended(hasComponent(MasterRecipeActivity.class.getName()));
        Intents.release();
    }

    @After
    public void finish() {
        if (recipeListIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(recipeListIdlingResource);
        }
    }

}
