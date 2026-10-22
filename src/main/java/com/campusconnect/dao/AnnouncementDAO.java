package com.campusconnect.dao;

import com.campusconnect.api.ApiClient;
import com.campusconnect.model.Announcement;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementDAO {

    public List<Announcement> getAllAnnouncements() {

        List<Announcement> announcements = new ArrayList<>();

        try {
            JsonNode response = ApiClient.get("/announcements");

            if (response == null || !response.isArray()) {
                throw new RuntimeException(
                        "Invalid response received from announcements API."
                );
            }

            for (JsonNode item : response) {

                Announcement announcement = new Announcement();

                announcement.setAnnouncementId(
                        item.path("announcementId").asInt()
                );

                announcement.setTitle(
                        item.path("title").asText("")
                );

                announcement.setMessage(
                        item.path("message").asText("")
                );

                JsonNode dateNode = item.get("announcementDate");

                announcement.setAnnouncementDate(
                        dateNode == null || dateNode.isNull()
                                ? ""
                                : dateNode.asText("")
                );

                announcements.add(announcement);
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Unable to load announcements from the server. "
                            + e.getMessage(),
                    e
            );
        }

        return announcements;
    }
}
