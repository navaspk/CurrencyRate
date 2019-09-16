package com.luxoft.navaspk.currencyrate;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;

import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
public class CurrencyActivityAndRecyclerviewTest {

    /** Launches {@link CurrencyRateActivity} for every test */
    @Rule
    public ActivityTestRule<CurrencyRateActivity> activityRule =
            new ActivityTestRule<>(CurrencyRateActivity.class);

    /**
     * Test a heading of the recycler view is clickable.
     */
    @Test
    public void testIsClickable() {
        onView(withId(R.id.item_container)).check(matches(isClickable()));
    }

    @Test
    public void clickItemOnSecondPos() {
        onView(withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
    }

    @Test
    public void performClickOnFirstItem() {
        onView(withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @Test
    public void performClickOnBasedOnText() {
        onView(withId(R.id.recyclerview))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText("USD")), click()));
    }

    @Test
    public void checkTheItemPositionContent() {
        onView(withId(R.id.recyclerview))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(2, click()));
    }

}
