package com.campusconnect.dao;

import com.campusconnect.config.DatabaseConnection;
import com.campusconnect.model.StudyMaterial;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudyMaterialDAO {

    // =========================================================
    // GET ALL STUDY MATERIALS
    // =========================================================

    public List<StudyMaterial> getAllMaterials() {

        List<StudyMaterial> materials = new ArrayList<>();

        String sql = """
                SELECT material_id,
                       title,
                       subject,
                       description,
                       file_path
                FROM study_materials
                ORDER BY material_id
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

                StudyMaterial material =
                        new StudyMaterial();

                material.setMaterialId(
                        resultSet.getInt("material_id")
                );

                material.setTitle(
                        resultSet.getString("title")
                );

                material.setSubject(
                        resultSet.getString("subject")
                );

                material.setDescription(
                        resultSet.getString("description")
                );

                material.setFilePath(
                        resultSet.getString("file_path")
                );

                materials.add(material);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return materials;
    }

    // =========================================================
    // ADD STUDY MATERIAL
    // =========================================================

    public boolean addMaterial(
            StudyMaterial material
    ) {

        String sql = """
                INSERT INTO study_materials
                (title, subject, description, file_path)
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    material.getTitle()
            );

            statement.setString(
                    2,
                    material.getSubject()
            );

            statement.setString(
                    3,
                    material.getDescription()
            );

            statement.setString(
                    4,
                    material.getFilePath()
            );

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // DELETE STUDY MATERIAL
    // =========================================================

    public boolean deleteMaterial(
            int materialId
    ) {

        String sql =
                "DELETE FROM study_materials " +
                        "WHERE material_id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    materialId
            );

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }
}
