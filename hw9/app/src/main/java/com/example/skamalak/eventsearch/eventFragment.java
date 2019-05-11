package com.example.skamalak.eventsearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

public class eventFragment extends Fragment {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private static final String TAG = "eventFragment";
    private RequestQueue mQueue;
    private View view;
    private ProgressBar mProgressBar;

    private Context mContext;
    private Button btnTEST;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO hopefully not req: https://stackoverflow.com/questions/11387740/where-how-to-getintent-getextras-in-an-android-fragment

        ///jsonParse(eventId);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //final Bundle bundleFragment = new Bundle();
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        mEditor = mPreferences.edit();
        final View view = inflater.inflate(R.layout.event_tab, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar3);
        mProgressBar.setVisibility(View.VISIBLE);

        Log.d(TAG, "inside Event Fragment");
//        final View view = inflater.inflate(R.layout.event_tab, container, false);

        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getExtras();
        String eventId = extras.getString("EXTRA_EVENTID");

        mQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        String url = "http://csci571-env.w3n729ir4i.us-east-1.elasticbeanstalk.com/eventDetails?" + "id=" + eventId;
//        final ProgressBar spinner;
//        spinner = (ProgressBar) getActivity().findViewById(R.id.progressBar3);
//        spinner.setVisibility(View.VISIBLE);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //TODO handle cases when not there,show NA
                            String artistTeam = "";
                            String category = "";
                            String time = "";
                            Log.d(TAG, String.valueOf("Populate: "));
                            Log.d(TAG, String.valueOf(response));
                            JSONObject _embedded = response.getJSONObject("_embedded");
                            JSONArray attractions = _embedded.getJSONArray("attractions");
                            if(attractions.length() == 0){
                                RelativeLayout lay = getActivity().findViewById(R.id.eventNoResults);
                                lay.setVisibility(View.VISIBLE);

                                RelativeLayout lays = getActivity().findViewById(R.id.eventResults);
                                lay.setVisibility(View.INVISIBLE);

                            }
                            else{
                                for (int i = 0; i < attractions.length(); i++) {
                                    JSONObject attraction = attractions.getJSONObject(i);
                                    if(i == attractions.length()-1){
                                        artistTeam += attraction.getString("name");
                                    }
                                    else{
                                        artistTeam += attraction.getString("name") + " | ";
                                    }
                                }

                                JSONArray venues = _embedded.getJSONArray("venues");
                                JSONObject venueName = venues.getJSONObject(0);
                                //venue.add(venueName.getString("name"));

                                JSONArray classifications = response.getJSONArray("classifications");
                                JSONObject classification = classifications.getJSONObject(0);
                                JSONObject segment = classification.getJSONObject("segment");
                                JSONObject genre = classification.getJSONObject("genre");
                                category = segment.getString("name") + " | " + genre.getString("name");

                                JSONObject dates = response.getJSONObject("dates");
                                JSONObject start = dates.getJSONObject("start");

                                String dateString = start.getString("localDate");
                                String[] parts = dateString.split("-");

                                Calendar cal = new GregorianCalendar(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                                String date = DateUtils.formatDateTime(getActivity().getApplicationContext(), cal.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE);
//                                String[] splitData = date.split(" ");
//                                splitData[0] = splitData[0].substring(0,3);

                                //gt error here and app crashed
                                Log.d(TAG, date);
                                time = dateString +" "+ start.get("localTime");

//                                JSONArray pricerangeVals = response.getJSONArray("priceRanges");
//                                JSONObject priceRange = pricerangeVals.getJSONObject(0);
//                                String minPrice = priceRange.getString("min");
//                                String maxPrice = priceRange.getString("max");
//
//                                String[] minInd = minPrice.split(".");
//                                String[] maxInd = maxPrice.split(".");
//                                String price = "";
//                                //TODO price variable not getting updated
//                                Log.d(TAG, String.valueOf(minInd));
//
//                                if (minInd.length == 1) {
//                                    price = minInd[0] + ".00";
//                                }
//                                else if (minInd.length == 2) {
//                                    if (minInd[1].length() == 1) {
//                                        price = minInd[0] + "." + minInd[1] + "0";
//                                    }
//                                    if (minInd[1].length() == 2) {
//                                        price = minInd[0] + minInd[1];
//                                    }
//                                }
//                                price += " ~ ";
//                                if (maxInd.length == 1) {
//                                    price += maxInd[0] + ".00";
//                                }
//                                else if (maxInd.length == 2) {
//                                    if (maxInd[1].length() == 1) {
//                                        price += maxInd[0] + "." + maxInd[1] + "0";
//                                    }
//                                    if (maxInd[1].length() == 2) {
//                                        price += maxInd[0] + maxInd[1];
//                                    }
//
//                                }

                                JSONObject status = dates.getJSONObject("status");

                                JSONObject seatmapVal = response.getJSONObject("seatmap");

                                TextView artistVal = getActivity().findViewById(R.id.artistVal);
                                artistVal.setText(artistTeam);

//                                TextView priceVal = getActivity().findViewById(R.id.priceRangeVal);
//                            try{
//                                priceVal.setText(price);
//                            }
//                            catch(Exception e){
//                                TextView priceHead = getActivity().findViewById(R.id.priceRange);
//                                priceVal.setVisibility(View.GONE);
//                                priceHead.setVisibility(View.GONE);
//                            }


                                TextView venueVal = getActivity().findViewById(R.id.venueVal);
                                venueVal.setText(venueName.getString("name"));

                                //pushing for fragments
                                mEditor.putString("eventFragmentName", artistTeam);
                                mEditor.putString("venueFragmentName", venueName.getString("name"));
                                mEditor.putString("categoryFragmentName", segment.getString("name"));
                                mEditor.commit();
                                Log.d("artistFragment map","starting");
                                Map<String,?> keys = mPreferences.getAll();

                                for(Map.Entry<String,?> entry : keys.entrySet()){
                                    Log.d("artistFragment map",entry.getKey() + ": " + entry.getValue().toString());
                                }

                                Log.d("artistFragment map","ending");


                                TextView categoryVal = getActivity().findViewById(R.id.categoryVal);
                                categoryVal.setText(category);

                                TextView timeVal = getActivity().findViewById(R.id.timeVal);
                                timeVal.setText(time);

                                TextView ticketVal = getActivity().findViewById(R.id.ticketStatusVal);
                                ticketVal.setText(status.getString("code"));

                                TextView buyTicketval = getActivity().findViewById(R.id.buyTicketVal);
                                buyTicketval.setText(Html.fromHtml("<a href='"+response.getString("url")+"'>TicketMaster</a> "));
                                buyTicketval.setMovementMethod(LinkMovementMethod.getInstance());

                                TextView seatmap = getActivity().findViewById(R.id.seatmapVal);
                                seatmap.setText(Html.fromHtml("<a href='"+seatmapVal.getString("staticUrl")+"'>View Here</a> "));
                                seatmap.setMovementMethod(LinkMovementMethod.getInstance());
                                mProgressBar.setVisibility(View.GONE);
                            }


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





        return view;
    }



}
