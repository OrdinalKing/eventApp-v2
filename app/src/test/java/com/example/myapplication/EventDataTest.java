package com.example.myapplication;

import org.junit.Test;
import static org.junit.Assert.*;

import com.ui.placeholder.EventData;

import com.google.firebase.Timestamp;

import java.util.Calendar;
import java.util.Date;

public class EventDataTest {
    @Test
    public void testEventData() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2022);
        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DATE, 25);
        calendar.set(Calendar.HOUR_OF_DAY, 3);
        calendar.set(Calendar.MINUTE, 36);

        Date date = calendar.getTime();
        Timestamp timestamp = new Timestamp(date);

        EventData event = new EventData("1", "Sample Event", "Sample Details",
                "https://sample.com/image.jpg", "Sample Location", timestamp,
                "doc1", "user1", true);

        assertEquals("1", event.getId());
        assertEquals("Sample Event", event.getName());
        assertEquals("Sample Details", event.getDetails());
        assertEquals("https://sample.com/image.jpg", event.getImageUrl());
        assertEquals("doc1", event.getDocId());
        assertEquals("user1", event.getUserId());
        assertTrue(event.getShare());

        // Setters
        event.setLocation("New Location");
        assertEquals("New Location", event.getLocation());


        event.setId("2");
        assertEquals("2", event.getId());

        event.setName("New Event Name");
        assertEquals("New Event Name", event.getName());

        event.setDetails("New Event Details");
        assertEquals("New Event Details", event.getDetails());

        event.setImageUrl("https://newurl.com/image.jpg");
        assertEquals("https://newurl.com/image.jpg", event.getImageUrl());

        event.setDocId("doc2");
        assertEquals("doc2", event.getDocId());

        event.setUserId("user2");
        assertEquals("user2", event.getUserId());

        event.setShare(false);
        assertFalse(event.getShare());
    }
}