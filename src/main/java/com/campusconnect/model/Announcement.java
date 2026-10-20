package com.campusconnect.model;

public class Announcement {

    private int announcementId;
    private String title;
    private String message;
    private String announcementDate;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public Announcement() {
    }

    public Announcement(
            int announcementId,
            String title,
            String message,
            String announcementDate
    ) {
        this.announcementId = announcementId;
        this.title = title;
        this.message = message;
        this.announcementDate = announcementDate;
    }

    // =====================================================
    // GETTERS
    // =====================================================

    public int getAnnouncementId() {
        return announcementId;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getAnnouncementDate() {
        return announcementDate;
    }

    // =====================================================
    // SETTERS
    // =====================================================

    public void setAnnouncementId(
            int announcementId
    ) {
        this.announcementId = announcementId;
    }

    public void setTitle(
            String title
    ) {
        this.title = title;
    }

    public void setMessage(
            String message
    ) {
        this.message = message;
    }

    public void setAnnouncementDate(
            String announcementDate
    ) {
        this.announcementDate = announcementDate;
    }
}