package com.example.myapplication;

import com.EventDetailActivity;
import com.google.firebase.auth.FirebaseAuth;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.util.Log;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EventDetailActivityTest {

    private FirebaseAuth mAuth;

    @Rule
    public ActivityScenarioRule<EventDetailActivity> activityRule = new ActivityScenarioRule<>(EventDetailActivity.class);

    @Before
    public void before() {
        Intents.init();
    }

    @After
    public void after() {
        Intents.release();
    }

    @Test
    public void gotoReviewSuccess() throws InterruptedException {

        String docId = "C7bwF0oO5Cw4AKrftKa7";
        EventDetailActivity.eventId = docId;


        Log.d("Tag", EventDetailActivity.class.getName());
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null){
            String email ="test@gmail.com";
            String password = "123456";
            mAuth.signInWithEmailAndPassword(email, password);

            try {
                Thread.sleep(3000); // sleep for 3 seconds
            } catch (InterruptedException e) {
                // handle the exception
            }
        }

        // Click on the review button
        onView(withId(R.id.reviewButton))
                .perform(click());

        // wait while new activity opens..
        try {
            Thread.sleep(3000); // sleep for 3 seconds
        } catch (InterruptedException e) {
            // handle the exception
        }

        // check the EventReviewActivity properly opens..
        onView(ViewMatchers.withId(R.id.reviewDescription)).check(matches(isDisplayed()));

    }
}