package com.gregoriopalama.udacity.bakingapp.ui.list;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gregoriopalama.udacity.bakingapp.R;
import com.gregoriopalama.udacity.bakingapp.model.Ingredient;
import com.gregoriopalama.udacity.bakingapp.model.Recipe;
import com.gregoriopalama.udacity.bakingapp.model.Step;
import com.gregoriopalama.udacity.bakingapp.ui.recipe.RecipeActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.core.internal.deps.guava.base.Preconditions.checkNotNull;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.gregoriopalama.udacity.bakingapp.Constants.EXTRA_RECIPE;

/**
 * Instrumented test to check if the activity of a single recipe, once opened, shows
 * the list of steps
 *
 * @author Gregorio Palamà
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SingleRecipeTest {
    private final static int INGREDIENT_QUANTITY = 1;
    private final static String INGREDIENT_MEASURE = "LTR";
    private final static String INGREDIENT_NAME = "Milk";

    private final static int STEP_ID = 1;
    private final static String STEP_DESCRIPTION = "First step";
    private final static String STEP_VIDEO = "";
    private final static String STEP_THUMB = "";

    private final static int RECIPE_ID = 1;
    private final static String RECIPE_NAME = "Test recipe";
    private final static int RECIPE_SERVINGS = 1;
    private final static String RECIPE_THUMB = "";

    @Rule
    public ActivityTestRule<RecipeActivity> activityRule =
            new ActivityTestRule(RecipeActivity.class, true, false);

    @Before
    public void init() {
        Ingredient ingredient = new Ingredient(INGREDIENT_QUANTITY,
                INGREDIENT_MEASURE, INGREDIENT_NAME);
        Step step = new Step(STEP_ID, STEP_DESCRIPTION, STEP_DESCRIPTION, STEP_VIDEO, STEP_THUMB);
        Recipe recipe = new Recipe(RECIPE_ID, RECIPE_NAME, Arrays.asList(ingredient),
                Arrays.asList(step), RECIPE_SERVINGS, RECIPE_THUMB);

        Intent intent = new Intent();
        intent.putExtra(EXTRA_RECIPE, recipe);
        activityRule.launchActivity(intent);
    }

    @Test
    public void checkListIsVisible() {
        onView(withId(R.id.steps))
                .check(matches(atPosition(0, isDisplayed())));
    }

    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }
}
