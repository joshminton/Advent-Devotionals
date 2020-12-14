package com.example.adventuality;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ChooseActivity extends AppCompatActivity implements DevotionalAdapter.OnClickListener {

    private ArrayList<Devotional> devotionals;
    private RecyclerView lstDevotionals;
    private RecyclerView.LayoutManager layoutManager;
    private DevotionalAdapter devotionalsAdapter;

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

        final AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Devotional clickedDevotional = (Devotional) devotionalsAdapter.getItem(position);
            }
        };


        getDevotionals();
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
                                    getDevotionalDetails(devotional);
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

    protected void getDevotionalDetails(final Devotional devotional){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String name = devotional.getName();
        Log.d("getting details for ", name);
        final String devotionalURL = devotional.getFeedURL();
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, devotionalURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Successful request", (String.valueOf(response)));
                        try {
                            devotional.setDescription(response.getString("description"));
                            devotional.setImageURL(response.getString("image_url").replace("http:", "https:"));
                            devotional.setHomepageURL(response.getString("homepage"));      //validate if any of these are missing?
                            devotional.setTwitterURL(response.getString("twitter_handle"));
                            devotional.setSubscribeURL(response.getString("subscribe_url"));
                            devotional.setSampleURL(response.getString("sample_chapter_url"));
//                            Log.d("Devotional updated: ", devotional.toString());
                            devotionalsAdapter.notifyDataSetChanged();
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
        Log.d("Clicked: ", devotionals.get(position).getName());

        Intent intent = new Intent(this, DevotionalActivity.class);
        startActivity(intent);
    }
}