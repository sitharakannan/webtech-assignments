package com.example.skamalak.eventsearch;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.GregorianCalendar;

public class upcomingFragment extends Fragment {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private static final String TAG = "upcomingFragment";
    private RequestQueue mQueue;

    private ArrayList<String> displayName = new ArrayList<>();
    private ArrayList<String> artistName  = new ArrayList<>();
    private ArrayList<String> dateTime = new ArrayList<>();
    private ArrayList<String> type = new ArrayList<>();
    private ArrayList<String> uri = new ArrayList<>();
    private ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.upcoming_recycler, container, false);

        Spinner category_spinner = view.findViewById(R.id.categorySort);
        ArrayAdapter<CharSequence> categ_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.categoryType_array, android.R.layout.simple_spinner_item);
        categ_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(categ_adapter);
        Log.i("Entered", "Into Fragmnent");
        Spinner dist_spinner = view.findViewById(R.id.sortSort);
        ArrayAdapter<CharSequence> distunits_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.sort, android.R.layout.simple_spinner_item);
        distunits_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dist_spinner.setAdapter(distunits_adapter);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar3);
        mProgressBar.setVisibility(View.VISIBLE);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        mEditor = mPreferences.edit();

        mQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        String venueFragName = mPreferences.getString("venueFragmentName", "");
        venueFragName = venueFragName.replace(" ", "%20");
        Log.d(TAG, venueFragName);

        String url = "http://csci571-env.w3n729ir4i.us-east-1.elasticbeanstalk.com/songkickVenueID?" + "name=" + venueFragName;
        Log.d(TAG, url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //TODO handle cases when not there,show NA
                            Log.d(TAG, String.valueOf(response));
                            String venueId = response.getString("id");


                            String url2 = "http://csci571-env.w3n729ir4i.us-east-1.elasticbeanstalk.com/songkickVenueDetails?" + "id=" + venueId;
                            Log.d(TAG, url2);
                            JsonObjectRequest requestAgain = new JsonObjectRequest(Request.Method.GET, url2, null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                //TODO handle cases when not there,show NA
                                                Log.d(TAG, String.valueOf(response));
                                                JSONObject resultsPg = response.getJSONObject("resultsPage");
                                                JSONObject results = resultsPg.getJSONObject("results");
                                                JSONArray events = results.getJSONArray("event");

                                                for (int i = 0; i < events.length(); i++) {
                                                    if(i<5){
                                                        JSONObject event = events.getJSONObject(i);
                                                        displayName.add(event.getString("displayName"));
                                                        type.add("Type: "+event.getString("type"));

                                                        JSONArray performance = event.getJSONArray("performance");
                                                        JSONObject perf = performance.getJSONObject(0);
                                                        artistName.add(perf.getString("displayName"));

                                                        JSONObject dates = event.getJSONObject("start");
                                                        String date = dates.getString("date");
                                                        String time = dates.getString("time");

                                                        String[] parts = date.split("-");

                                                        Calendar cal = new GregorianCalendar(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                                                        String crctDate = DateUtils.formatDateTime(getActivity().getApplicationContext(), cal.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE);
                                                        String[] splitData = crctDate.split(" ");
                                                        splitData[0] = splitData[0].substring(0,3);

                                                        dateTime.add(splitData[0]+","+splitData[1]+splitData[2] +" "+time);
                                                        uri.add(event.getString("uri"));
                                                        //gt error here and app crashed
                                                        Log.d(TAG, date);

                                                    }
                                                    else{
                                                        break;
                                                    }
                                                }



                                                Log.d(TAG, "initRecyclerView: init recyclerview");

                                                RecyclerView upcomingRecyclerView = view.findViewById(R.id.upcomingRecyclerView);
                                                upcomingRecyclerAdapter adapter = new upcomingRecyclerAdapter(displayName, artistName, dateTime, type, uri, getActivity().getApplicationContext());
                                                upcomingRecyclerView.setAdapter(adapter);
                                                upcomingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                                                mProgressBar.setVisibility(View.GONE);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                    //spinner.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), "Unable to Retrieve Contents", Toast.LENGTH_LONG).show();
                                }
                            });


                            mQueue.add(requestAgain);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //spinner.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Unable to Retrieve Contents", Toast.LENGTH_LONG).show();
            }
        });

        mQueue.add(request);
        //mQueue.add(requestAgain);

        return view;
    }
}
