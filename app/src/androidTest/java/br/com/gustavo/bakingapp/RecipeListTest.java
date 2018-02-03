package br.com.gustavo.bakingapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.gustavo.bakingapp.recipelist.MainActivity;

import static org.junit.Assert.*;

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



    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();



        assertEquals("br.com.gustavo.bakingapp", appContext.getPackageName());
    }
}
