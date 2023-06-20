package com.example.myapplication;

import org.junit.Test;
import static org.junit.Assert.*;

import com.ui.review.ReviewData;

public class ReviewDataTest {
    @Test
    public void getName_returnsCorrectName() {
        String expectedName = "John";
        String description = "This is a review.";
        ReviewData reviewData = new ReviewData(expectedName, description);

        String actualName = reviewData.getName();

        assertEquals(expectedName, actualName);
    }

    @Test
    public void getDescription_returnsCorrectDescription() {
        String name = "John";
        String expectedDescription = "This is a review.";
        ReviewData reviewData = new ReviewData(name, expectedDescription);

        String actualDescription = reviewData.getDescription();

        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void setName_setsNameCorrectly() {
        String expectedName = "Jane";
        String description = "This is another review.";
        ReviewData reviewData = new ReviewData("John", description);

        reviewData.setName(expectedName);
        String actualName = reviewData.getName();

        assertEquals(expectedName, actualName);
    }

    @Test
    public void setDescription_setsDescriptionCorrectly() {
        String name = "John";
        String expectedDescription = "This is another review.";
        ReviewData reviewData = new ReviewData(name, "This is a review.");

        reviewData.setDescription(expectedDescription);
        String actualDescription = reviewData.getDescription();

        assertEquals(expectedDescription, actualDescription);
    }
}
