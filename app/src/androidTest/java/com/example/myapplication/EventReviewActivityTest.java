package com.example.myapplication;

import com.EventReviewActivity;
import com.EventDetailActivity;
import com.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class EventReviewActivityTest {
    private FirebaseAuth mAuth;
    @Rule
    public ActivityScenarioRule<EventReviewActivity> activityRule = new ActivityScenarioRule<>(EventReviewActivity.class);

    @Test
    public void reviewSuccess() {

        Log.d("Tag", EventReviewActivity.class.getName());
        mAuth = FirebaseAuth.getInstance();
        String reviewDescription = "Excellent Event";
        String docId = "C7bwF0oO5Cw4AKrftKa7";


        if (mAuth.getCurrentUser() != null){

            EventDetailActivity.eventId = docId;
            // Type review description
            onView(withId(R.id.reviewDescription))
                    .perform(typeText(reviewDescription), closeSoftKeyboard());

            // Click on the Login button
            onView(withId(R.id.saveButton))
                    .perform(click());
            
            String userId = mAuth.getCurrentUser().getUid();
            FirebaseFirestore db1 = FirebaseFirestore.getInstance();
            db1.collection("myevents").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    ArrayList<String> eventlist = (ArrayList<String>) documentSnapshot.get("eventlist");
                    Boolean is_exist = false;
                    for (String event : eventlist) {
                        if (event.equals(docId)) is_exist = true;
                    }
                    assertTrue(is_exist == true);
                }
            });
        }
    }
}