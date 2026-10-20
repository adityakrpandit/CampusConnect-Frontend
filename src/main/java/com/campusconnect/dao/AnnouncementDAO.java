package com.campusconnect.dao;

import com.campusconnect.config.DatabaseConnection;
import com.campusconnect.model.Announcement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementDAO {

    // =====================================================
    // GET ALL ANNOUNCEMENTS
    // =====================================================

    public List<Announcement> getAllAnnouncements() {

        List<Announcement> announcements =
                new ArrayList<>();

        String sql = """
                SELECT announcement_id,
                       title,
                       message,
                       announcement_date
                FROM announcements
                ORDER BY announcement_date DESC
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Announcement announcement =
                        new Announcement();

                announcement.setAnnouncementId(
                        resultSet.getInt(
                                "announcement_id"
                        )
                );

                announcement.setTitle(
                        resultSet.getString(
                                "title"
                        )
                );

                announcement.setMessage(
                        resultSet.getString(
                                "message"
                        )
                );

                announcement.setAnnouncementDate(
                        resultSet.getString(
                                "announcement_date"
                        )
                );

                announcements.add(
                        announcement
                );
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return announcements;
    }
}