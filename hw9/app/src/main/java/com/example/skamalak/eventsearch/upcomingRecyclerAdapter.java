package com.example.skamalak.eventsearch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class upcomingRecyclerAdapter extends RecyclerView.Adapter<upcomingRecyclerAdapter.upcomingViewHolder>{


    private static final String TAG = "artistRecyclerAdapter";
    private ArrayList<String> mdisplayName = new ArrayList<>();
    private ArrayList<String> martistName = new ArrayList<>();
    private ArrayList<String> mdateTime  = new ArrayList<>();
    private ArrayList<String> mtype = new ArrayList<>();
    private ArrayList<String> mUri = new ArrayList<>();
    private Context mContext;

    public upcomingRecyclerAdapter(ArrayList<String> displayName, ArrayList<String> artistName, ArrayList<String> dateTime, ArrayList<String> type, ArrayList<String> uri, Context mContext) {
        this.mdisplayName = displayName;
        this.martistName = artistName;
        this.mdateTime = dateTime;
        this.mtype = type;
        this.mUri = uri;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public upcomingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.upcoming_tab, viewGroup, false);
        upcomingViewHolder viewHolder = new upcomingViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull upcomingViewHolder upcomingViewHolder, final int i) {
        Log.d(TAG, mdisplayName.get(i));
        upcomingViewHolder.displayName.setText(mdisplayName.get(i));
        upcomingViewHolder.artistName.setText(martistName.get(i));
        upcomingViewHolder.dateTime.setText(mdateTime.get(i));
        upcomingViewHolder.type.setText(mtype.get(i));

        upcomingViewHolder.card.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(mUri.get(i)));
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(browserIntent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mdisplayName.size();
    }

    public class upcomingViewHolder extends RecyclerView.ViewHolder{
        TextView displayName;
        TextView artistName;
        TextView dateTime;
        TextView type;
        CardView card;
        LinearLayout upcomingLayout;


        public upcomingViewHolder(@NonNull View itemView) {
            super(itemView);
            displayName = itemView.findViewById(R.id.displayName);
            artistName = itemView.findViewById(R.id.artistUpcoming);
            dateTime = itemView.findViewById(R.id.dateTime);
            type = itemView.findViewById(R.id.type);
            card = itemView.findViewById(R.id.card_view);
            upcomingLayout = itemView.findViewById(R.id.upcomingLayout);
        }
    }
}
