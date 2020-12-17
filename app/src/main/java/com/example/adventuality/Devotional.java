package com.example.adventuality;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Devotional implements Serializable {

    private String name;
    private String description;
    private String imageURL;
    private String homepageURL = "none";
    private String twitterURL;
    private String subscribeURL;
    private String sampleURL;
    private String feedURL;
    private boolean followed;

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

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
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

    public void addDevotional(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        setFollowed(true);
        String followedDevotionals = sharedPref.getString("devotionals", "");
        ArrayList<String> followedDevotionalsArray = new ArrayList<>(Arrays.asList(followedDevotionals.split("@")));
        if(!followedDevotionalsArray.contains(getFeedURL())){
            followedDevotionals = followedDevotionals.concat(getFeedURL() + "@");
            sharedPref.edit().putString("devotionals", followedDevotionals).apply();
        }
    }

    public void removeDevotional(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        setFollowed(false);
        String followedDevotionals = sharedPref.getString("devotionals", "");
        ArrayList<String> followedDevotionalsArray = new ArrayList<>(Arrays.asList(followedDevotionals.split("@")));
        if(followedDevotionalsArray.contains(getFeedURL())){
            followedDevotionalsArray.remove(getFeedURL());
            followedDevotionals = "";
            for(String d : followedDevotionalsArray){
                followedDevotionals = followedDevotionals.concat(d + "@");
            }
            sharedPref.edit().putString("devotionals", followedDevotionals).apply();
        }
    }
}
