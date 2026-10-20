package com.campusconnect.model;

public class Doubt {

    private int doubtId;
    private String studentName;
    private String subject;
    private String question;
    private String status;
    private String answer;

    public Doubt() {
    }

    public int getDoubtId() {
        return doubtId;
    }

    public void setDoubtId(int doubtId) {
        this.doubtId = doubtId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}