package com.example.skamalak.eventsearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import android.support.v7.app.ActionBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private RequestQueue mQueue;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;


    private ArrayList<String> eventNames = new ArrayList<>();
    private ArrayList<String> imageUrls = new ArrayList<>();
    private ArrayList<String> venueName = new ArrayList<>();
    private ArrayList<String> dateTime = new ArrayList<>();
    private ArrayList<String> eventId = new ArrayList<>();
    private ProgressBar mProgressBar;

    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

//        mProgressBar = (ProgressBar) this.findViewById(R.id.progressBar3);
//        mProgressBar.setVisibility(View.VISIBLE);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        //TODO check with hasExtras to prevent app from crashing
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String keyword = extras.getString("EXTRA_KEYWORD");
        String radius = extras.getString("EXTRA_RADIUS");
        String unit = extras.getString("EXTRA_UNIT");
        String locInput = extras.getString("EXTRA_LOCINPUT");
        String category = extras.getString("EXTRA_CATEG");
        Boolean otherlocChecked = extras.getBoolean("EXTRA_OTHERLOCCHECKED");
        String segmentId = "";

        if(category == "Music"){
            segmentId = "KZFzniwnSyZfZ7v7nJ";
        }
        else if(category == "Sports"){
            segmentId = "KZFzniwnSyZfZ7v7nE";
        }
        else if(category == "Arts & Theatre"){
            segmentId = "KZFzniwnSyZfZ7v7na";
        }
        else if(category == "Film"){
            segmentId = "KZFzniwnSyZfZ7v7nn";
        }
        else if(category == "Miscellaneous"){
            segmentId = "KZFzniwnSyZfZ7v7n1";
        }


        Log.d("SearchActivtiy",keyword);
        try{
            Log.d("SearchActivtiy",radius);
        }
        catch(Exception e){
            radius = "10";
        }
        Log.d("SearchActivtiy",unit);
        try{
            Log.d("SearchActivtiy",locInput);
        }
        catch (Exception e){
            locInput = null;
        }
        Log.d("SearchActivtiy", String.valueOf(category));
        Log.d("SearchActivtiy", String.valueOf(otherlocChecked));


        String lat = "34.0266";
        String lon = "-118.283";
        if(otherlocChecked){
            lat = "0";
            lon = "0";
        }

        mQueue = Volley.newRequestQueue(this);
        jsonParse(keyword, radius, unit, otherlocChecked, lat, lon, locInput, segmentId);



    }

    private void jsonParse(String keyword, String radius, String unit, Boolean otherlocChecked, String lat, String lon, String locInput, String segmentId){



        String url = "http://csci571-env.w3n729ir4i.us-east-1.elasticbeanstalk.com/events?" + "keyword=" + keyword + "&radius=" + radius + "&unit=" + unit + "&otherlocChecked" + otherlocChecked + "&lat="+ lat + "&lon=" + lon + "&locInput=" + locInput + "&segmentId=" + segmentId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.d("SearchActivity", String.valueOf("Populate: "));
                            Log.d("SearchActivity", String.valueOf(response));

                            JSONObject embedded = response.getJSONObject("_embedded");
                            JSONArray jsonArray = embedded.getJSONArray("events");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject event = jsonArray.getJSONObject(i);

                                eventNames.add(event.getString("name"));

                                JSONArray segmentArray = event.getJSONArray("classifications");
                                JSONObject segmentObj = segmentArray.getJSONObject(0);
                                JSONObject segmentString = segmentObj.getJSONObject("segment");
                                String segmentName = segmentString.getString("name");
                                Log.d("SearchActivity", segmentName);
                                if(segmentName.equals("Music")){
                                    Log.d("SearchActivity","inside");
                                    imageUrls.add("http://csci571.com/hw/hw9/images/android/music_icon.png");
                                }
                                else if(segmentName.equals("Sports")){
                                    imageUrls.add("http://csci571.com/hw/hw9/images/android/sport_icon.png");
                                }
                                else if(segmentName.equals("Arts & Theatre")){
                                    imageUrls.add("http://csci571.com/hw/hw9/images/android/art_icon.png");
                                }
                                else if(segmentName.equals("Miscellaneous")){
                                    imageUrls.add("http://csci571.com/hw/hw9/images/android/miscellaneous_icon.png");
                                }
                                else if(segmentName.equals("Film")){
                                    imageUrls.add("http://csci571.com/hw/hw9/images/android/film_icon.png");
                                }

                                JSONObject dates = event.getJSONObject("dates");
                                JSONObject start = dates.getJSONObject("start");
                                String date = start.getString("localDate");
                                String time = start.getString("localTime");
                                dateTime.add(date + " " + time);

                                JSONObject _embdedded = event.getJSONObject("_embedded");
                                JSONArray venues = _embdedded.getJSONArray("venues");
                                JSONObject venue = venues.getJSONObject(0);
                                venueName.add(venue.getString("name"));
                                eventId.add(event.getString("id"));

//                                Log.d("SearchActivity", "Populate:");
//                                Log.d("SearchActivity", String.valueOf(eventNames));
//                                Log.d("SearchActivity", String.valueOf(imageUrls));
//                                Log.d("SearchActivity", String.valueOf(dateTime));
//                                Log.d("SearchActivity", String.valueOf(venueName));
                                //mProgressBar.setVisibility(View.GONE);

//                                Log.d("SearchActivity", "initRecyclerView: init recyclerview");
//                                Log.d("SearchActivity", String.valueOf(eventNames));
//                                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
//                                RecyclerView recyclerView = findViewById(R.id.recyclerView);
//                                searchRecyclerAdapter adapter = new searchRecyclerAdapter(imageUrls, eventNames, venueName, dateTime, eventId, getApplicationContext());
//                                recyclerView.setAdapter(adapter);
//                                recyclerView.setLayoutManager(layoutManager);


                                initRecyclerView();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);

    }

    private void initRecyclerView(){
        Log.d("SearchActivity", "initRecyclerView: init recyclerview");
        Log.d("SearchActivity", String.valueOf(eventNames));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        searchRecyclerAdapter adapter = new searchRecyclerAdapter(imageUrls, eventNames, venueName, dateTime, eventId, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

    }
}
