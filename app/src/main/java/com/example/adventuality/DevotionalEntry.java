package com.example.adventuality;

public class DevotionalEntry {
    private String uid;
    private String date;
    private String title;
    private String description;
    private boolean isMarkdown;
    private Devotional devotionalSeries;
    private String imageURL;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isMarkdown() {
        return isMarkdown;
    }

    public void setMarkdown(boolean markdown) {
        isMarkdown = markdown;
    }

    public Devotional getDevotionalSeries() {
        return devotionalSeries;
    }

    public void setDevotionalSeries(Devotional devotionalSeries) {
        this.devotionalSeries = devotionalSeries;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "DevotionalEntry{" +
                "uid='" + uid + '\'' +
                ", date='" + date + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", isMarkdown=" + isMarkdown +
                '}';
    }
}
