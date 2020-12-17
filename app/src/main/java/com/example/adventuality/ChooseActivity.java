package com.example.adventuality;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ChooseActivity extends AppCompatActivity implements DevotionalAdapter.OnClickListener {

    private ArrayList<Devotional> devotionals;
    private RecyclerView lstDevotionals;
    private RecyclerView.LayoutManager layoutManager;
    private DevotionalAdapter devotionalsAdapter;
    private ArrayList<String> chosenDevotionalFeedURLs;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        devotionals = new ArrayList<>();
        devotionalsAdapter = new DevotionalAdapter(devotionals, this);
        lstDevotionals = findViewById(R.id.lstTodaysDevotionals);
        layoutManager = new GridLayoutManager(this.getBaseContext(), 2);
        lstDevotionals.setLayoutManager(layoutManager);
        lstDevotionals.setAdapter(devotionalsAdapter);

        sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);

        chosenDevotionalFeedURLs = new ArrayList<>();
        getChosenDevotionals();

        getDevotionals();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateChosen();
    }

    protected void getDevotionals() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String allDevotionals = "https://prayermate-dt.s3.eu-west-2.amazonaws.com/galleries/dev_challenge.json";
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, allDevotionals, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Successful request", (String.valueOf(response)));
                        try {
                            JSONArray jsonDevotionals =  response.getJSONArray("sections").getJSONObject(0).getJSONArray("items");

                            for(int i = 0; i < jsonDevotionals.length(); i++){
                                JSONObject currentDevotional = jsonDevotionals.getJSONObject(i);
                                Log.d(jsonDevotionals.getJSONObject(i).getString("label"), " ");
                                if(currentDevotional.has("feed_url")) {
                                    Devotional devotional = new Devotional(currentDevotional.getString("label"),
                                            currentDevotional.getString("feed_url"));
                                    devotional.setImageURL(currentDevotional.getString("image_url").replace("http:", "https:"));
                                    devotional.setFollowed(chosenDevotionalFeedURLs.contains(currentDevotional.getString("feed_url")));
                                    devotionals.add(devotional);
                                    devotionalsAdapter.notifyDataSetChanged();
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

    @Override
    public void onDevotionalClick(int position) {
        Intent intent = new Intent(this, DevotionalActivity.class);
        intent.putExtra("Devotional", devotionals.get(position));
        startActivity(intent);
    }

    @Override
    public void onCheckClick(int position) {
        Devotional dev = devotionals.get(position);
        if(dev.isFollowed()){
            dev.removeDevotional(this.getApplicationContext());
            ((MaterialCardView) layoutManager.findViewByPosition(position)).setStrokeWidth(0);
        } else {
            dev.addDevotional(this.getApplicationContext());
            ((MaterialCardView) layoutManager.findViewByPosition(position)).setStrokeWidth(12);
            ((MaterialCardView) layoutManager.findViewByPosition(position)).setStrokeColor(getColor(R.color.colorAccent));
        }
    }

    public void getChosenDevotionals(){
        String followedDevotionals = sharedPref.getString("devotionals", "");
        chosenDevotionalFeedURLs = new ArrayList<>(Arrays.asList(followedDevotionals.split("@")));
    }

    public void updateChosen(){
        getChosenDevotionals();
        for(Devotional dev : devotionals){
            if(chosenDevotionalFeedURLs.contains(dev.getFeedURL())){
                dev.setFollowed(true);
            } else {
                dev.setFollowed(false);
            }
        }
        devotionalsAdapter.notifyDataSetChanged();
    }

}