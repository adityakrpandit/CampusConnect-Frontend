package com.campusconnect.dao;

import com.campusconnect.config.DatabaseConnection;
import com.campusconnect.model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    // =====================================================
    // GET ALL EVENTS
    // =====================================================

    public List<Event> getAllEvents() {

        List<Event> events = new ArrayList<>();

        String sql = """
                SELECT event_id,
                       event_name,
                       event_date,
                       venue,
                       description
                FROM events
                ORDER BY event_id
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

                Event event = new Event();

                event.setEventId(
                        resultSet.getInt("event_id")
                );

                event.setEventName(
                        resultSet.getString("event_name")
                );

                event.setEventDate(
                        resultSet.getDate("event_date")
                );

                event.setVenue(
                        resultSet.getString("venue")
                );

                event.setDescription(
                        resultSet.getString("description")
                );

                events.add(event);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return events;
    }
}