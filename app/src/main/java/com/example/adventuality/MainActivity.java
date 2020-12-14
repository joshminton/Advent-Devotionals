package com.example.adventuality;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        today = formatter.format(todayDate);

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

        for(Devotional d : devotionals.values()){
            Log.d("devotional: ", d.getName() + " " + d.getFeedURL());
        }

        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                getTodaysDevotions();
            }
        };

        handler.postDelayed(r, 5000);

    }
    public void getTodaysDevotions(){
        todaysDevotionalEntries.clear();
        for(final String feedURL : chosenDevotionalFeedURLs){
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, feedURL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("Successful request", (String.valueOf(response)));
                            try {
                                DevotionalEntry devotionalEntry = new DevotionalEntry();
                                devotionalEntry.setImageURL(response.getString("image_url"));
                                JSONArray entries = response.getJSONArray("petitions");
                                for(int i = 0; i < entries.length(); i++){
                                    JSONObject entry = entries.getJSONObject(i);
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
}