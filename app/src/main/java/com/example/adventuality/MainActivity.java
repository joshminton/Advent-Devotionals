package com.example.adventuality;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    private HashMap<String, Devotional> devotionals;
    private RecyclerView lstEntries;
    private RecyclerView.LayoutManager layoutManager;
    private DevotionalEntryAdapter entriesAdapter;

    private ArrayList<String> chosenDevotionalFeedURLs;

    private ArrayList<DevotionalEntry> todaysDevotionalEntries;

    private String today;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        today = formatter.format(todayDate);

        sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        getChosenDevotionals();

        Picasso.get().setLoggingEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        devotionals = new HashMap<>();
        chosenDevotionalFeedURLs = new ArrayList<>();
        todaysDevotionalEntries = new ArrayList<>();
        entriesAdapter = new DevotionalEntryAdapter(todaysDevotionalEntries, devotionals);
        lstEntries = findViewById(R.id.lstTodaysDevotionals);
        layoutManager = new LinearLayoutManager(this.getBaseContext());
        lstEntries.setLayoutManager(layoutManager);
        lstEntries.setAdapter(entriesAdapter);
        getTodaysDevotions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getChosenDevotionals();
        getTodaysDevotions();
    }

    public void getTodaysDevotions(){
        todaysDevotionalEntries.clear();
        for(final String feedURL : chosenDevotionalFeedURLs){
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, feedURL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Successful request", (String.valueOf(response)));
                            try {
                                DevotionalEntry devotionalEntry = new DevotionalEntry();
                                devotionalEntry.setImageURL(response.getString("image_url").replace("http:", "https:"));
                                JSONArray entries = response.getJSONArray("petitions");
                                for(int i = 0; i < entries.length(); i++){
                                    JSONObject entry = entries.getJSONObject(i);
                                    Log.d("Found, ", "entry");
                                    if(entry.getString("date").equals(today)){
                                        Log.d("Found, ", "entry");
                                        devotionalEntry.setUid(entry.getString("uid"));
                                        devotionalEntry.setDate(entry.getString("date"));
                                        devotionalEntry.setTitle(entry.getString("title"));
                                        devotionalEntry.setDescription(entry.getString("description"));
                                        devotionalEntry.setMarkdown(entry.getBoolean("markdown"));
//                                        devotionalEntry.setDevotionalSeries(devotionals.get(feedURL));
                                        todaysDevotionalEntries.add(devotionalEntry);
//                                        Log.v("Devotional Entry:", devotionalEntry.toString());
                                        lstEntries.getAdapter().notifyDataSetChanged();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
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
        String followedDevotionals = sharedPref.getString("devotionals", "");
        chosenDevotionalFeedURLs = new ArrayList<>(Arrays.asList(followedDevotionals.split("@")));
        Log.d("Chosen devotionals:", chosenDevotionalFeedURLs.toString());
    }
}