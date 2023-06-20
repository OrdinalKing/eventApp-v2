package com.example.myapplication;

import com.LoginActivity;
import com.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertTrue;

import android.util.Log;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.assertion.ViewAssertions;

import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    private FirebaseAuth mAuth;
    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void before() {
        Intents.init();
    }

    @After
    public void after() {
        Intents.release();
    }

    @Test
    public void loginSuccess() {

        Log.d("Tag", LoginActivity.class.getName());
        mAuth = FirebaseAuth.getInstance();
        String testEmail = "test@gmail.com";
        String testPassword = "123456";

        // Type in email and password
        onView(withId(R.id.email))
                .perform(typeText(testEmail), closeSoftKeyboard());

        onView(withId(R.id.password))
                .perform(typeText(testPassword), closeSoftKeyboard());

        // Click on the Login button
        onView(withId(R.id.login))
                .perform(click());

        assertTrue(mAuth.getCurrentUser() != null);

    }
}