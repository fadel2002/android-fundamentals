package com.dicoding.submissionone

import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    private val existUser = "fadel2002"
    private val noUserFound = "No User Found"
    private val notExistUser = "bdienbd"
    private val searchHint = "Telusuri..."

    @Before
    fun setup(){
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun testUserDetail() = runBlocking {
        delay(3000)

        onView(withId(R.id.rv_user)).check(matches(isDisplayed()))

        onView(withId(R.id.rv_user)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

        delay(3000)

        onView(withId(R.id.tv_user_origin)).check(matches(withText(not(""))))
        onView(withId(R.id.tv_username)).check(matches(withText(not(""))))
        onView(withId(R.id.tv_name)).check(matches(withText(not(""))))
        onView(withId(R.id.tv_user_company)).check(matches(withText(not(""))))

        onView(withId(R.id.button_favorite)).check(matches(isDisplayed()))
        onView(withId(R.id.button_favorite)).perform(click())

        onView(withId(R.id.tabs)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_user_follow)).check(matches(isDisplayed()))

        val tabLayout = onView(withId(R.id.tabs)).perform(selectTabAtPosition(1))
        tabLayout.perform(selectTabAtPosition(1))

        delay(3000)
        onView(withId(R.id.rv_user_follow)).check(matches(isDisplayed()))
        return@runBlocking
    }

    @Test
    fun testSearchByExistUsername() = runBlocking {
        onView(withId(R.id.search)).check(matches(isDisplayed()))
        onView(withId(R.id.search)).perform(click())
        onView(withHint(searchHint)).perform(typeText(existUser))
        onView(withHint(searchHint)).perform(pressKey(KeyEvent.KEYCODE_ENTER))

        delay(3000)

        onView(withId(R.id.rv_user)).check(matches(isDisplayed()))

        onView(withId(R.id.rv_user)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

        return@runBlocking
    }

    @Test
    fun testSearchByNotExistUsername() = runBlocking {
        onView(withId(R.id.search)).check(matches(isDisplayed()))
        onView(withId(R.id.search)).perform(click())
        onView(withHint(searchHint)).perform(typeText(notExistUser))
        onView(withHint(searchHint)).perform(pressKey(KeyEvent.KEYCODE_ENTER))

        delay(3000)

        onView(withId(R.id.no_user_found)).check(matches(isDisplayed()))
        onView(withId(R.id.no_user_found)).check(matches(withText(noUserFound)))

        return@runBlocking
    }

    @Test
    fun testToFavorite() = runBlocking {
        delay(3000)

        onView(withId(R.id.rv_user)).check(matches(isDisplayed()))

        delay(500)

        onView(withId(R.id.favorite)).check(matches(isDisplayed()))
        onView(withId(R.id.favorite)).perform(click())

        onView(withId(R.id.rv_user)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_user)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

        return@runBlocking
    }

    @Test
    fun checkThemeMode() = runBlocking {
        delay(3000)

        val currentMode = getThemeMode()
        delay(500)

        onView(withId(R.id.theme)).check(matches(isDisplayed()))
        onView(withId(R.id.theme)).perform(click())
        delay(500)

        val newMode = getThemeMode()

        assertNotEquals(currentMode, newMode)

        return@runBlocking
    }

    private fun selectTabAtPosition(position: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return allOf(isDisplayed(), isAssignableFrom(TabLayout::class.java))
            }

            override fun getDescription(): String {
                return "Select tab at position $position"
            }

            override fun perform(uiController: UiController?, view: View?) {
                val tabLayout = view as TabLayout
                val tab = tabLayout.getTabAt(position)
                tab?.select()
            }
        }
    }

    private fun getThemeMode(): Int {
        return when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_NO -> {
                // The current theme is light mode
                1
            }
            AppCompatDelegate.MODE_NIGHT_YES -> {
                // The current theme is dark mode
                2
            }
            else -> 0
        }
    }
}