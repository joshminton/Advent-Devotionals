package com.example.adventuality;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity implements DevotionalEntryAdapter.OnClickListener {

    private HashMap<String, Devotional> devotionals;
    private RecyclerView lstEntries;
    private RecyclerView.LayoutManager layoutManager;
    private DevotionalEntryAdapter entriesAdapter;

    private ArrayList<String> chosenDevotionalFeedURLs;

    private ArrayList<DevotionalEntry> todaysDevotionalEntries;

    private String today;

    private SharedPreferences sharedPref;

    private String followedDevotionals = "";

    private MaterialProgressBar progressBar;

    private int activeRequests = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        today = formatter.format(cal.getTime());
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        String month = new SimpleDateFormat("MMMM").format(cal.getTime());
        String prettyDate = String.valueOf(ordinal(day) + " " + month);

        ((TextView) findViewById(R.id.txtDate)).setText(prettyDate);

        sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);

        Picasso.get().setLoggingEnabled(true);

        progressBar = (MaterialProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        devotionals = new HashMap<>();
        chosenDevotionalFeedURLs = new ArrayList<>();
        todaysDevotionalEntries = new ArrayList<>();
        entriesAdapter = new DevotionalEntryAdapter(todaysDevotionalEntries, this);
        lstEntries = findViewById(R.id.lstTodaysDevotionals);
        layoutManager = new LinearLayoutManager(this.getBaseContext());
        lstEntries.setLayoutManager(layoutManager);
        lstEntries.setAdapter(entriesAdapter);
        getChosenDevotionals();
        getTodaysDevotions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!followedDevotionals.equals(sharedPref.getString("devotionals", ""))){
            getChosenDevotionals();
            getTodaysDevotions();
            entriesAdapter.notifyDataSetChanged();
        }
    }

    public void getTodaysDevotions(){
//        Log.d("Getting", "entries");
        todaysDevotionalEntries.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        for(final String feedURL : chosenDevotionalFeedURLs){
            if(feedURL != "") {
                Log.d("Getting", " url " + feedURL);
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, feedURL, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("Successful request", (String.valueOf(response)));
                                try {
                                    DevotionalEntry devotionalEntry = new DevotionalEntry();
                                    devotionalEntry.setImageURL(response.getString("image_url").replace("http:", "https:"));
                                    devotionalEntry.setSeriesTitle(response.getString("name"));
                                    JSONArray entries = response.getJSONArray("petitions");
                                    for (int i = 0; i < entries.length(); i++) {
                                        JSONObject entry = entries.getJSONObject(i);
                                        Log.d("Found, ", "entry");
                                        if (entry.getString("date").equals(today)) {
                                            Log.d("Found, ", "entry");
                                            devotionalEntry.setUid(entry.getString("uid"));
                                            devotionalEntry.setDate(entry.getString("date"));
                                            devotionalEntry.setTitle(entry.getString("title"));
                                            devotionalEntry.setDescription(entry.getString("description"));
                                            devotionalEntry.setMarkdown(entry.getBoolean("markdown"));
                                            //                                        devotionalEntry.setDevotionalSeries(devotionals.get(feedURL));
                                            todaysDevotionalEntries.add(devotionalEntry);
                                            Collections.sort(todaysDevotionalEntries);
                                            //                                        Log.v("Devotional Entry:", devotionalEntry.toString());
                                            lstEntries.getAdapter().notifyDataSetChanged();
                                        }
                                    }
                                    activeRequests--;
                                    checkRequests();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    activeRequests--;
                                    checkRequests();
                                } catch (RuntimeException e) {
                                    activeRequests--;
                                    checkRequests();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Unsuccessful request", (String.valueOf(error)));
                            }
                        });

                requestQueue.add(getRequest);
                activeRequests++;
                checkRequests();
            }
        }
        if(chosenDevotionalFeedURLs.isEmpty()){
            findViewById(R.id.addTip).setVisibility(View.VISIBLE);
            Log.d("sdsdfsdf", "sfsdfsdfsdf");
        } else {
            findViewById(R.id.addTip).setVisibility(View.GONE);
            Log.d("sdsdfsdf", "blalblbla");
        }
    }

    public void chooseDevotionals(View v){
        Intent intent = new Intent(MainActivity.this, ChooseActivity.class);
        startActivity(intent);
    }

    public void addDevotional(Devotional dev){
        String followedDevotionals = sharedPref.getString("devotionals", "");
        ArrayList<String> followedDevotionalsArray = new ArrayList<>(Arrays.asList(followedDevotionals.split("@")));
        if(!followedDevotionalsArray.contains(dev.getFeedURL())){
            followedDevotionals.concat(dev.getFeedURL() + "@");
            sharedPref.edit().putString("devotionals", followedDevotionals).apply();
        }
    }

    public void removeDevotional(Devotional dev){
        String followedDevotionals = sharedPref.getString("devotionals", "");
        ArrayList<String> followedDevotionalsArray = new ArrayList<>(Arrays.asList(followedDevotionals.split("@")));
        if(followedDevotionalsArray.contains(dev.getFeedURL())){
            followedDevotionalsArray.remove(dev.getFeedURL());
            followedDevotionals = "";
            for(String d : followedDevotionalsArray){
                followedDevotionals.concat(d + "@");
            }
            sharedPref.edit().putString("devotionals", followedDevotionals).apply();
        }
    }

    public void getChosenDevotionals(){
        followedDevotionals = sharedPref.getString("devotionals", "");
        if(!followedDevotionals.equals("")){
            chosenDevotionalFeedURLs = new ArrayList<>(Arrays.asList(followedDevotionals.split("@")));
        } else {
            chosenDevotionalFeedURLs = new ArrayList<>();
        }
        Log.d("Chosen devotionals:", chosenDevotionalFeedURLs.toString());
    }

    @Override
    public void onEntryClick(int position) {
        Intent intent = new Intent(this, EntryActivity.class);
        try {
            intent.putExtra("Entry", todaysDevotionalEntries.get(position));
            startActivity(intent);
        } catch(IndexOutOfBoundsException e){
            Log.d("Error:", "Index out of bounds exception.");
            e.printStackTrace();
        }
    }

    private void checkRequests(){
        if(activeRequests>0){
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void checkDevotionCount(){

    }


    //CREDIT https://stackoverflow.com/a/4011339/3032936
    String ordinal(int num)
    {
        String[] suffix = {"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        int m = num % 100;
        return String.valueOf(num) + suffix[(m > 3 && m < 21) ? 0 : (m % 10)];
    }
}