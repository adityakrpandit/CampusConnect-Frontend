package com.campusconnect.model;

import java.sql.Date;

public class Event {

    private int eventId;
    private String eventName;
    private Date eventDate;
    private String venue;
    private String description;

    // =====================================================
    // CONSTRUCTORS
    // =====================================================

    public Event() {
    }

    public Event(
            int eventId,
            String eventName,
            Date eventDate,
            String venue,
            String description
    ) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.venue = venue;
        this.description = description;
    }

    // =====================================================
    // GETTERS
    // =====================================================

    public int getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public String getVenue() {
        return venue;
    }

    public String getDescription() {
        return description;
    }

    // =====================================================
    // SETTERS
    // =====================================================

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}