package com.campusconnect.model;

public class StudyMaterial {

    private int materialId;
    private String title;
    private String subject;
    private String description;
    private String filePath;

    public StudyMaterial() {
    }

    public StudyMaterial(int materialId, String title, String subject,
                         String description, String filePath) {
        this.materialId = materialId;
        this.title = title;
        this.subject = subject;
        this.description = description;
        this.filePath = filePath;
    }

    public StudyMaterial(String title, String subject,
                         String description, String filePath) {
        this.title = title;
        this.subject = subject;
        this.description = description;
        this.filePath = filePath;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}