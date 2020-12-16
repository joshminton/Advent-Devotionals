package com.example.adventuality;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class DevotionalActivity extends AppCompatActivity {

    Devotional devotional;

    SharedPreferences sharedPref;

    ExtendedFloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        sharedPref = getSharedPreferences("preferences", Context.MODE_PRIVATE);

        devotional = (Devotional) getIntent().getSerializableExtra("Devotional");
        getDevotionalDetails(devotional);

        setContentView(R.layout.activity_devotional);
        Picasso.get().load(devotional.getImageURL()).into((ImageView) findViewById(R.id.devotionalImage), new Callback() {
            @Override
            public void onSuccess() {
                ((ImageView) findViewById(R.id.devotionalImage)).setScaleType(ImageView.ScaleType.CENTER_CROP);//Or ScaleType.FIT_CENTER
            }
            @Override
            public void onError(Exception e) {
                //nothing for now
            }
        });
        ((TextView) findViewById(R.id.txtDevotionalName)).setText(devotional.getName());
        ((TextView) findViewById(R.id.txtDevotionalDescription)).setText(devotional.getDescription());
        ((TextView) findViewById(R.id.txtTwitter)).setText(devotional.getTwitterURL());
        ((TextView) findViewById(R.id.txtLink)).setText(devotional.getSubscribeURL());

        ExtendedFloatingActionButton fab = ((ExtendedFloatingActionButton) findViewById(R.id.btnFollow));

        if(devotional.isFollowed()){
            fab.setBackgroundColor(getColor(R.color.colorRemove));
            fab.setText("Remove");
        } else {
            fab.setBackgroundColor(getColor(R.color.colorAdd));
            fab.setText("Add");
        }
    }

    private void loadDetails(){
        ((TextView) findViewById(R.id.txtDevotionalName)).setText(devotional.getName());
        ((TextView) findViewById(R.id.txtDevotionalDescription)).setText(devotional.getDescription());
        ((TextView) findViewById(R.id.txtTwitter)).setText(devotional.getTwitterURL());
        ((TextView) findViewById(R.id.txtLink)).setText(devotional.getSubscribeURL());
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
                            loadDetails();
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

    public void onClickAdd(View v){
        Log.d("ADDING", "DEVOTIONAL");
        if(devotional.isFollowed()){
            removeDevotional();
        } else {
            addDevotional();
        }
    }

    private void addDevotional(){
        fab = findViewById(R.id.btnFollow);
        fab.setBackgroundColor(getColor(R.color.colorRemove));
        fab.setText("Remove");
        devotional.setFollowed(true);
        String followedDevotionals = sharedPref.getString("devotionals", "");
        ArrayList<String> followedDevotionalsArray = new ArrayList<>(Arrays.asList(followedDevotionals.split("@")));
        if(!followedDevotionalsArray.contains(devotional.getFeedURL())){
            followedDevotionals = followedDevotionals.concat(devotional.getFeedURL() + "@");
            sharedPref.edit().putString("devotionals", followedDevotionals).apply();
        }
        Log.d("ADDING", followedDevotionals);
    }

    private void removeDevotional(){
        fab = findViewById(R.id.btnFollow);
        fab.setBackgroundColor(getColor(R.color.colorAdd));
        fab.setText("Add");
        devotional.setFollowed(false);
        String followedDevotionals = sharedPref.getString("devotionals", "");
        Log.d("Old array", followedDevotionals);
        ArrayList<String> followedDevotionalsArray = new ArrayList<>(Arrays.asList(followedDevotionals.split("@")));
        if(followedDevotionalsArray.contains(devotional.getFeedURL())){
            followedDevotionalsArray.remove(devotional.getFeedURL());
            followedDevotionals = "";
            for(String d : followedDevotionalsArray){
                Log.d("Followed array:", "is" + followedDevotionalsArray.toString());
                followedDevotionals = followedDevotionals.concat(d + "@");
            }
            Log.d("New array", "is" + followedDevotionals);
            sharedPref.edit().putString("devotionals", followedDevotionals).apply();
        }
    }
}