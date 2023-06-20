package com.ui.placeholder;

import com.google.firebase.Timestamp;

public class EventData {
    public String id;
    public String name;
    public String details;
    public String imageUrl;
    public String location;
    public Timestamp date;
    public String docId;
    public String userId;
    public Boolean share;

    public EventData(String id, String name, String details, String imageUrl, String location, Timestamp date,String docId, String userId, Boolean share) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.imageUrl = imageUrl;
        this.docId = docId;
        this.userId = userId;
        this.share = share;
        this.location = location;
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public Timestamp getDate() {
        return date;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDocId() {
        return docId;
    }

    public String getUserId() {
        return userId;
    }

    public Boolean getShare() {
        return share;
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

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setShare(Boolean share) {
        this.share = share;
    }

    @Override
    public String toString() {
        return name;
    }
}
