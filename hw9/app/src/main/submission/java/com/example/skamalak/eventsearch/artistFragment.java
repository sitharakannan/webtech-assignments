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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.skamalak.eventsearch.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class artistFragment extends Fragment {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private static final String TAG = "artistFragment";
    private RequestQueue mQueue;


    private ArrayList<String> artistnameVal = new ArrayList<>();
    private ArrayList<String> followersVal  = new ArrayList<>();
    private ArrayList<String> popularityVal = new ArrayList<>();
    private ArrayList<String> checkatVal = new ArrayList<>();
    private ArrayList<String> artistImgUrl = new ArrayList<>();
    String category;
    private Integer breakIndex;
    private ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.artist_recycler, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar3);
        mProgressBar.setVisibility(View.VISIBLE);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        mEditor = mPreferences.edit();
        
        mQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        String artistTeamName = mPreferences.getString("eventFragmentName", "");
        final String categoryFragmentVal = mPreferences.getString("categoryFragmentName", "");
        Log.d(TAG, artistTeamName);
        final String[] reqArtistTeam = artistTeamName.split(" \\| ");
        Log.d(TAG, String.valueOf(reqArtistTeam.length));
        for (int i=0; i<reqArtistTeam.length; i++){
            Log.d(TAG, reqArtistTeam[i]);
        }

        breakIndex = 0;
        final Integer teamLt = reqArtistTeam.length;
        for (int i=0; i<reqArtistTeam.length; i++){

//        final ProgressBar spinner;
//        spinner = (ProgressBar) getActivity().findViewById(R.id.progressBar3);
//        spinner.setVisibility(View.VISIBLE);
            if (i<2){
                if(categoryFragmentVal.equals("Music")){
                    category = "Music";
                    Log.d(TAG, categoryFragmentVal);
                    String searchexp = reqArtistTeam[i].replace(" ", "%20");
                    String url2 = "http://csci571-env.w3n729ir4i.us-east-1.elasticbeanstalk.com/spotify?" + "artist=" + searchexp;
                    Log.d(TAG, url2);
                    Log.d(TAG, searchexp);
                    final JsonObjectRequest requestAgain = new JsonObjectRequest(Request.Method.GET, url2, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        //TODO handle cases when not there,show NA


                                        Log.d(TAG, String.valueOf(response));

                                        JSONObject followers = response.getJSONObject("followers");
                                        artistnameVal.add(response.getString("name"));
                                        followersVal.add(followers.getString("total"));
                                        popularityVal.add(response.getString("popularity"));
                                        JSONObject external = response.getJSONObject("external_urls");
                                        checkatVal.add(external.getString("spotify"));


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
                }
                else{
                    category = "None";
//                    RelativeLayout artistlayout = view.findViewById(R.id.artistTable);
//                    artistlayout.setVisibility(View.GONE);
                }

                String searchexp = reqArtistTeam[i].replace(" ", "%20");
                String url = "http://csci571-env.w3n729ir4i.us-east-1.elasticbeanstalk.com/googlePhotos?" + "searchExp=" + searchexp;
                Log.d(TAG, url);
                Log.d(TAG, searchexp);
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    //TODO handle cases when not there,show NA
//

                                    Log.d(TAG, String.valueOf(response));
                                    //response.data.items[i].link;
                                    JSONArray items = response.getJSONArray("items");
                                    for (int i = 0; i < items.length(); i++) {
                                        if(i<8){
                                            JSONObject item = items.getJSONObject(i);
                                            artistImgUrl.add(item.getString("link"));
                                        }
                                        else{
                                            break;
                                        }
                                    }

                                    if(teamLt > 1){
                                        if(items.length() > 8){
                                            breakIndex = 7;
                                        }
                                        else{
                                            breakIndex = items.length()-1;
                                        }

                                        Log.d(TAG, String.valueOf(breakIndex));
                                    }


                                    Log.d(TAG, "initRecyclerView: init recyclerview");
                                    Log.d(TAG, String.valueOf(artistImgUrl));
                                    RecyclerView artistRecyclerView = view.findViewById(R.id.artistRecyclerView);
                                    artistRecyclerAdapter adapter = new artistRecyclerAdapter(artistImgUrl, artistnameVal, followersVal, popularityVal, checkatVal, category, breakIndex, getActivity().getApplicationContext());
                                    artistRecyclerView.setAdapter(adapter);
                                    artistRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
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

                mQueue.add(request);

            }
            else{
                break;
            }

        }


        return view;
    }

}
