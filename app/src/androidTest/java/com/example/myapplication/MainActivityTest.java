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
public class MainActivityTest {
    private FirebaseAuth mAuth;
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void before() {
        Intents.init();
    }

    @After
    public void after() {
        Intents.release();
    }

     @Test
     public void checkLoginSuccess() {

         Log.d("Tag", MainActivity.class.getName());
         mAuth = FirebaseAuth.getInstance();

         if (LoginActivity.isGuestMode == false)
             assertTrue(mAuth.getCurrentUser() != null); // check login..

     }

//    @Test
//    public void logoutSuccess() {
//        onView(withId(R.id.logout_id))
//                .perform(click());
//    }
    
        
}