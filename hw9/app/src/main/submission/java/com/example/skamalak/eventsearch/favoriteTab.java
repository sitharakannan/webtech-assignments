package com.example.skamalak.eventsearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link favoriteTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link favoriteTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class favoriteTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private ArrayList<String> eventNames = new ArrayList<>();
    private ArrayList<String> imageUrls = new ArrayList<>();
    private ArrayList<String> venueName = new ArrayList<>();
    private ArrayList<String> dateTime = new ArrayList<>();
    private ArrayList<String> eventId = new ArrayList<>();




    public favoriteTab() {
        // Required empty public constructor
    }

    public static favoriteTab newInstance(String param1, String param2) {
        favoriteTab fragment = new favoriteTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fav_recycler, container, false);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEditor = mPreferences.edit();


        Map<String,?> keys = mPreferences.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.d("clickingImgs FAV values",entry.getKey() + ": " + entry.getValue().toString());

            try {
                JSONObject obj = new JSONObject(entry.getValue().toString());

                if(entry.getKey().matches("[a-zA-Z]+")){

                }
                else{
                    imageUrls.add(obj.getString("imgCatg"));
                    eventNames.add(obj.getString("eventName"));
                    venueName.add(obj.getString("venueName"));
                    dateTime.add(obj.getString("date"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        RecyclerView favrecyclerView = view.findViewById(R.id.favrecyclerView);
        favRecyclerAdapter adapter = new favRecyclerAdapter(imageUrls, eventNames, venueName, dateTime, getActivity());
        favrecyclerView.setAdapter(adapter);
        favrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        return view;

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
