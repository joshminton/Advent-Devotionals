package com.example.adventuality;

public class Devotional {

    private String name;
    private String description;
    private String imageURL;
    private String homepageURL;
    private String twitterURL;
    private String subscribeURL;
    private String sampleURL;
    private String feedURL;

    public Devotional(String name, String description, String imageURL, String homepageURL, String twitterURL, String subscribeURL, String sampleURL) {
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
        this.homepageURL = homepageURL;
        this.twitterURL = twitterURL;
        this.subscribeURL = subscribeURL;
        this.sampleURL = sampleURL;
    }

    public Devotional(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getFeedURL() {
        return feedURL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getHomepageURL() {
        return homepageURL;
    }

    public void setHomepageURL(String homepageURL) {
        this.homepageURL = homepageURL;
    }

    public String getTwitterURL() {
        return twitterURL;
    }

    public void setTwitterURL(String twitterURL) {
        this.twitterURL = twitterURL;
    }

    public String getSubscribeURL() {
        return subscribeURL;
    }

    public void setSubscribeURL(String subscribeURL) {
        this.subscribeURL = subscribeURL;
    }

    public String getSampleURL() {
        return sampleURL;
    }

    public void setSampleURL(String sampleURL) {
        this.sampleURL = sampleURL;
    }

    public void setFeedURL(String feedURL) {
        this.feedURL = feedURL;
    }

    public Devotional(String name, String feedURL) {
        this.name = name;
        this.feedURL = feedURL;
    }

    @Override
    public String toString() {
        return "Devotional{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", homepageURL='" + homepageURL + '\'' +
                ", twitterURL='" + twitterURL + '\'' +
                ", subscribeURL='" + subscribeURL + '\'' +
                ", sampleURL='" + sampleURL + '\'' +
                ", feedURL='" + feedURL + '\'' +
                '}';
    }
}
