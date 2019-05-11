package com.example.skamalak.eventsearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class favRecyclerAdapter extends RecyclerView.Adapter<favRecyclerAdapter.ViewHolder>{


    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mEventContent = new ArrayList<>();
    private ArrayList<String> mVenueContent = new ArrayList<>();
    private ArrayList<String> mDateContent = new ArrayList<>();
    private Context mContext;

    public favRecyclerAdapter(ArrayList<String> mImages, ArrayList<String> mEventContent, ArrayList<String> mVenueContent, ArrayList<String> mDateContent,  Context context) {
        this.mImages = mImages;
        this.mEventContent = mEventContent;
        this.mVenueContent = mVenueContent;
        this.mDateContent = mDateContent;
        this.mContext = context;
    }

    //@NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_favorite_tab, viewGroup, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        Log.d("getImages", "onBindViewHolder: called.");

        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(i))
                .into(viewHolder.image);

        viewHolder.eventName.setText(mEventContent.get(i));
        viewHolder.venueName.setText(mVenueContent.get(i));
        viewHolder.dateName.setText(mDateContent.get(i));

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
        RelativeLayout favEventLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.faveventImg);
            eventName = itemView.findViewById(R.id.faveventText);
            venueName = itemView.findViewById(R.id.favvenueText);
            dateName = itemView.findViewById(R.id.favdateText);
            favEventLayout = itemView.findViewById(R.id.faveventLayout);
        }
    }
}
