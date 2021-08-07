package com.example.tramtracker.ui.activity

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.util.TreeIterables
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.example.tramtracker.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.StringDescription
import org.hamcrest.core.IsNot
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

@ExperimentalCoroutinesApi
@LargeTest
@HiltAndroidTest
class TrackerActivityTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var activityRule = ActivityScenarioRule(TrackerActivity::class.java)

    @Before
    fun start() {
        hiltRule.inject()
    }

    @Test
    fun testTrackerActivityViewDisplayed() {
        onView(withId(R.id.tracker_activity)).check(matches(isDisplayed()))
        onView(withId(R.id.action_container)).check(matches(isDisplayed()))
        onView(withId(R.id.clear)).check(matches(isDisplayed()))
        onView(withId(R.id.load)).check(matches(isDisplayed()))
    }

    @Test
    fun testRecyclerViewVisibleWhenLoadBtnClicked() {
        onView(withId(R.id.load)).perform(click())
        onView(isRoot()).perform(waitForView(withId(R.id.tram_tracker_recycler_view)))
        onView(isRoot()).perform(waitForView(withId(R.id.direction)))
        onView(isRoot()).perform(waitForView(withId(R.id.arrival_time)))
    }

    @Test
    fun testRecyclerViewVisibleWhenClearBtnClicked() {
        onView(withId(R.id.clear)).perform(click())
        onView(withId(R.id.tram_tracker_recycler_view)).check(matches(IsNot.not(isDisplayed())))
    }

    private fun waitForView(
        viewMatcher: Matcher<View>,
        timeout: Long = 10000,
        waitForDisplayed: Boolean = true
    ): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return Matchers.any(View::class.java)
            }

            override fun getDescription(): String {
                val matcherDescription = StringDescription()
                viewMatcher.describeTo(matcherDescription)
                return "wait for a specific view <$matcherDescription> to be ${if (waitForDisplayed) "displayed" else "not displayed during $timeout millis."}"
            }

            override fun perform(uiController: UiController, view: View) {
                uiController.loopMainThreadUntilIdle()
                val startTime = System.currentTimeMillis()
                val endTime = startTime + timeout
                val visibleMatcher = isDisplayed()

                do {
                    val viewVisible = TreeIterables.breadthFirstViewTraversal(view)
                        .any { viewMatcher.matches(it) && visibleMatcher.matches(it) }

                    if (viewVisible == waitForDisplayed) return
                    uiController.loopMainThreadForAtLeast(50)
                } while (System.currentTimeMillis() < endTime)

                throw PerformException.Builder()
                    .withActionDescription(this.description)
                    .withViewDescription(HumanReadables.describe(view))
                    .withCause(TimeoutException())
                    .build()
            }
        }
    }
}