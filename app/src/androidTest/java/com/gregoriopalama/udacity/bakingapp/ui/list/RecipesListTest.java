package com.gregoriopalama.udacity.bakingapp.ui.list;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.gregoriopalama.udacity.bakingapp.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


/**
 * Instrumented test to check if the list of recipes is shown
 *
 * @author Gregorio Palamà
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipesListTest {
    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule(MainActivity.class, true, true);

    @Test
    public void checkListIsVisible() {
        onView(withId(R.id.recipeList)).check(matches(isDisplayed()));
    }

}
