package com.example.skamalak.eventsearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class searchRecyclerAdapter extends RecyclerView.Adapter<searchRecyclerAdapter.ViewHolder>{

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mEventContent = new ArrayList<>();
    private ArrayList<String> mVenueContent = new ArrayList<>();
    private ArrayList<String> mDateContent = new ArrayList<>();
    private ArrayList<String> mEventId = new ArrayList<>();
    private Context mContext;
    private int clickId = 0;

    public searchRecyclerAdapter(ArrayList<String> mImages, ArrayList<String> mEventContent, ArrayList<String> mVenueContent, ArrayList<String> mDateContent, ArrayList<String> mEventId,  Context context) {
        this.mImages = mImages;
        this.mEventContent = mEventContent;
        this.mVenueContent = mVenueContent;
        this.mDateContent = mDateContent;
        this.mEventId = mEventId;
        this.mContext = context;
    }

    //@NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.searchevent_list_layout, viewGroup, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        Log.d("getImages", "onBindViewHolder: called.");
        mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEditor = mPreferences.edit();
//
//        mEditor.clear();
//        mEditor.commit();

//        Log.d("getImages", String.valueOf(getItemCount()));
//        Log.d("getImages", String.valueOf(mImages.get(i) + " " + String.valueOf(i)));
//        Log.d("getImages", String.valueOf(viewHolder.eventName));
        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(i))
                .into(viewHolder.image);

        viewHolder.eventName.setText(mEventContent.get(i));
        viewHolder.venueName.setText(mVenueContent.get(i));
        viewHolder.dateName.setText(mDateContent.get(i));



        if(mPreferences.contains(mEventId.get(i))){
            viewHolder.favorites.setImageResource(R.drawable.heart_fill_red);
            if(mPreferences.getString(mEventId.get(i),"") == "True"){
                mEditor.remove(mEventId.get(i));
                JSONObject favInfo = new JSONObject();
                try {
                    favInfo.put("eventName", mEventContent.get(i));
                    favInfo.put("venueName", mVenueContent.get(i));
                    favInfo.put("date", mDateContent.get(i));
                    favInfo.put("imgCatg", mImages.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                mEditor.putString(mEventId.get(i), String.valueOf(favInfo));
                mEditor.commit();

            }
            //clickId = 1;
        }
        else{
            viewHolder.favorites.setImageResource(R.drawable.heart_outline_black);
            //clickId = 0;
        }

        viewHolder.favorites.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(mPreferences.contains(mEventId.get(i))){
                    String msg = mEventContent.get(i) + " was removed from favorites";
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    viewHolder.favorites.setImageResource(R.drawable.heart_outline_black);
                    Log.d("clickingImgs removal: ", mEventId.get(i));
                    mEditor.remove(mEventId.get(i));
                    mEditor.commit();

                    //clickId = 0;

                } else {
                    viewHolder.favorites.setImageResource(R.drawable.heart_fill_red);
                    String msg = mEventContent.get(i) + " was added to favorites";
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    Log.d("clickingImgs add: ", mEventId.get(i));
                    JSONObject favInfo = new JSONObject();
                    try {
                        favInfo.put("eventName", mEventContent.get(i));
                        favInfo.put("venueName", mVenueContent.get(i));
                        favInfo.put("date", mDateContent.get(i));
                        favInfo.put("imgCatg", mImages.get(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    mEditor.putString(mEventId.get(i), String.valueOf(favInfo));
                    mEditor.commit();

                    //clickId = 1;
                }

                Log.d("clickingImgs","starting");
                Map<String,?> keys = mPreferences.getAll();

                for(Map.Entry<String,?> entry : keys.entrySet()){
                    Log.d("clickingImgs map values",entry.getKey() + ": " + entry.getValue().toString());
                }

                Log.d("clickingImgs","ending");
            }
        });

        viewHolder.searchEventLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("clickingImgs", "onClick: clicked on an image: " + mEventContent.get(i));

                Intent intent = new Intent(mContext, EventDetails.class);
                intent.putExtra("EXTRA_EVENTID", mEventId.get(i));
                intent.putExtra("EXTRA_EVENTNAME", mEventContent.get(i));
//                intent.putExtra("image_name", mImageNames.get(position));
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mEventContent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //Log.d("recyclerView", "recyclerView")
        ImageView image;
        TextView eventName;
        TextView venueName;
        TextView dateName;
        ImageView favorites;
        RelativeLayout searchEventLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            favorites = itemView.findViewById(R.id.favImg);
            image = itemView.findViewById(R.id.eventImg);
            Log.d("getContImg", String.valueOf(image));
            eventName = itemView.findViewById(R.id.eventText);
            venueName = itemView.findViewById(R.id.venueText);
            dateName = itemView.findViewById(R.id.dateText);
            searchEventLayout = itemView.findViewById(R.id.searcheventLayout);
        }
    }

}
