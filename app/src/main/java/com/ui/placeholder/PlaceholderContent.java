package com.ui.placeholder;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    public static List<Event> ITEMS = new ArrayList<>(Arrays.asList(
            new Event("1", "Event 1", "Description 1"),
            new Event("2", "Event 2", "Description 2"),
            new Event("3", "Event 3", "Description 3"),
            new Event("4", "Event 4", "Description 4"),
            new Event("5", "Event 5", "Description 5"),
            new Event("6", "Event 1", "Description 1"),
            new Event("7", "Event 2", "Description 2"),
            new Event("8", "Event 3", "Description 3"),
            new Event("9", "Event 4", "Description 4"),
            new Event("10", "Event 5", "Description 5"),
            new Event("11", "Event 1", "Description 1"),
            new Event("12", "Event 2", "Description 2"),
            new Event("13", "Event 3", "Description 3"),
            new Event("14", "Event 4", "Description 4"),
            new Event("15", "Event 5", "Description 5")
            ));

    public static class Event {
        public String id;
        public String name;
        public String details;

        public Event(String id, String name, String details) {
            this.id = id;
            this.name = name;
            this.details = details;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDetails() {
            return details;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}