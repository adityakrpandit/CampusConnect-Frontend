package com.campusconnect.dao;

import com.campusconnect.config.DatabaseConnection;
import com.campusconnect.model.Doubt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoubtDAO {

    // =====================================================
    // GET DOUBTS OF CURRENT STUDENT
    // =====================================================

    public List<Doubt> getDoubtsByStudent(String studentName) {

        List<Doubt> doubts = new ArrayList<>();

        String sql =
                "SELECT doubt_id, student_name, subject, " +
                        "question, status, answer " +
                        "FROM doubts " +
                        "WHERE student_name = ? " +
                        "ORDER BY doubt_id DESC";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, studentName);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    Doubt doubt = new Doubt();

                    doubt.setDoubtId(
                            resultSet.getInt("doubt_id")
                    );

                    doubt.setStudentName(
                            resultSet.getString("student_name")
                    );

                    doubt.setSubject(
                            resultSet.getString("subject")
                    );

                    doubt.setQuestion(
                            resultSet.getString("question")
                    );

                    doubt.setStatus(
                            resultSet.getString("status")
                    );

                    doubt.setAnswer(
                            resultSet.getString("answer")
                    );

                    doubts.add(doubt);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doubts;
    }

    // =====================================================
    // ADD NEW DOUBT
    // =====================================================

    public boolean addDoubt(
            String studentName,
            String subject,
            String question
    ) {

        String sql =
                "INSERT INTO doubts " +
                        "(student_name, subject, question, status) " +
                        "VALUES (?, ?, ?, ?)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, studentName);
            statement.setString(2, subject);
            statement.setString(3, question);
            statement.setString(4, "Pending");

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }
}