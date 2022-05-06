package com.example.nolazynote.entity;

public class Task {
    private String userId;
    private String content;
    private String date;
    private int taskId;
    public Task(){
    }


    public Task(String userId, String content, String date, int taskId) {
        this.userId = userId;
        this.content = content;
        this.date = date;
        this.taskId = taskId;
    }
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
