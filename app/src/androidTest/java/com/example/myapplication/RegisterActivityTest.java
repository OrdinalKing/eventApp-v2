package com.example.myapplication;

import com.RegisterActivity;
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
public class RegisterActivityTest {
    
    private FirebaseAuth mAuth;
    @Rule
    public ActivityScenarioRule<RegisterActivity> activityRule = new ActivityScenarioRule<>(RegisterActivity.class);

    @Test
    public void registerSuccess() {

        Log.d("Tag", RegisterActivity.class.getName());
        mAuth = FirebaseAuth.getInstance();
        String testEmail = "test21@gmail.com";
        String testPassword = "123456";
        String testName ="test20";
        String testUsername ="test20";
        // Type in email, password, username, name
        onView(withId(R.id.email))
                .perform(typeText(testEmail), closeSoftKeyboard());

        onView(withId(R.id.password))
                .perform(typeText(testPassword), closeSoftKeyboard());

        onView(withId(R.id.username))
                .perform(typeText(testUsername), closeSoftKeyboard());

        onView(withId(R.id.name))
                .perform(typeText(testName), closeSoftKeyboard());

        // Click on the Register button
        onView(withId(R.id.register))
                .perform(click());

        assertTrue(mAuth.getCurrentUser() != null);

    }

}
