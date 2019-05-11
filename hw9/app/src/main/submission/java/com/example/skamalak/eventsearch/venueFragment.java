package com.example.skamalak.eventsearch;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.Manifest;
import java.util.Map;

public class venueFragment extends Fragment implements OnMapReadyCallback{

    private GoogleMap mGoogleMap;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private static final String TAG = "venueFragment";
    private RequestQueue mQueue;
    private ProgressBar mProgressBar;

    MapView mapView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.venue_tab, container, false);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        mEditor = mPreferences.edit();
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar3);
        mProgressBar.setVisibility(View.VISIBLE);
        mapView =(MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
//        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager()
//                .findFragmentById(R.id.map);
        //pass fragment in getMapAsync handler
        mapView.getMapAsync(this);

//

        Log.d(TAG, "inside venue Fragment");

        String venueName = mPreferences.getString("venueFragmentName", "");
        venueName = venueName.replace(" ", "%20");
        Log.d(TAG, venueName);

        mQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = "http://csci571-env.w3n729ir4i.us-east-1.elasticbeanstalk.com/venueDetails?" + "name=" + venueName;
        Log.d(TAG, url);
//        final ProgressBar spinner;
//        spinner = (ProgressBar) getActivity().findViewById(R.id.progressBar3);
//        spinner.setVisibility(View.VISIBLE);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //TODO handle cases when not there,show NA

                            JSONObject _embedded = response.getJSONObject("_embedded");
                            JSONArray venues = _embedded.getJSONArray("venues");

                            JSONObject venue = venues.getJSONObject(0);

                            TextView venueVal = getActivity().findViewById(R.id.venueFragNameVal);
                            venueVal.setText(venue.getString("name"));

                            JSONObject add = venue.getJSONObject("address");
                            TextView address = getActivity().findViewById(R.id.addressVal);
                            address.setText(add.getString("line1"));

                            JSONObject cityVal = venue.getJSONObject("city");
                            TextView city = getActivity().findViewById(R.id.cityVal);
                            city.setText(cityVal.getString("name"));

                            JSONObject boxoffice = venue.getJSONObject("boxOfficeInfo");
                            TextView phoneNo = getActivity().findViewById(R.id.phonenoVal);
                            phoneNo.setText(boxoffice.getString("phoneNumberDetail"));

                            TextView openhrs = getActivity().findViewById(R.id.openhrsVal);
                            openhrs.setText(boxoffice.getString("openHoursDetail"));

                            JSONObject generalinfo = venue.getJSONObject("generalInfo");
                            TextView generalRule = getActivity().findViewById(R.id.generalVal);
                            generalRule.setText(generalinfo.getString("generalRule"));

                            TextView childRule = getActivity().findViewById(R.id.childVal);
                            childRule.setText(generalinfo.getString("childRule"));

                            JSONObject loc = venue.getJSONObject("location");
                            Double lat = Double.parseDouble( (String) loc.get("latitude"));
                            Double lon = Double.parseDouble( (String) loc.get("longitude"));
                            mGoogleMap.clear();
                            LatLng latlon = new LatLng(lat, lon);
                            mGoogleMap.addMarker(new MarkerOptions().position(latlon)
                                    .title("Marker in Sydney"));
                            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlon, 12.0f));

//                            // latitude and longitude
//                            double latitude = 17.385044;
//                            double longitude = 78.486671;




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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "inside Map onReady");
        mGoogleMap = googleMap;
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }
}
