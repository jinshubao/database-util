package com.jean.database.mysql;

public class ExecuteAnalyse {

    private String status;
    private String duration;
    private String percentage;

    public ExecuteAnalyse() {
    }

    public ExecuteAnalyse(String status, String duration, String percentage) {
        this.status = status;
        this.duration = duration;
        this.percentage = percentage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
