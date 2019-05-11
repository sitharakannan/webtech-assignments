package com.example.skamalak.eventsearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.skamalak.eventsearch.R;

import java.util.ArrayList;

public class artistRecyclerAdapter extends RecyclerView.Adapter<artistRecyclerAdapter.artistViewHolder> {

    private static final String TAG = "artistRecyclerAdapter";
    private ArrayList<String> martistheading = new ArrayList<>();
    private ArrayList<String> martistnameVal = new ArrayList<>();
    private ArrayList<String> mfollowersVal  = new ArrayList<>();
    private ArrayList<String> mpopularityVal = new ArrayList<>();
    private ArrayList<String> mcheckatVal = new ArrayList<>();
    private ArrayList<String> martistImgUrl = new ArrayList<>();
    private String mcategory;
    private Integer mbreakIndex;
    private Context mContext;


    public artistRecyclerAdapter(ArrayList<String> artistImgUrl, ArrayList<String>artistnameVal, ArrayList<String> followersVal, ArrayList<String> popularityVal, ArrayList<String> checkatVal, String category, Integer breakIndex, Context mContext) {
        this.martistnameVal = artistnameVal;
        this.mfollowersVal = followersVal;
        this.mpopularityVal = popularityVal;
        this.mcheckatVal = checkatVal;
        this.martistheading = artistnameVal;
        this.martistImgUrl = artistImgUrl;
        this.mcategory = category;
        this.mContext = mContext;
        this.mbreakIndex = breakIndex;
    }

    @NonNull
    @Override
    public artistViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.artist_tab, viewGroup, false);
        artistViewHolder viewHolder = new artistViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull artistViewHolder artistViewHolder, int i) {

        try{
            if(i == 0){
                artistViewHolder.artistHeading.setText(martistnameVal.get(i));
                artistViewHolder.artistHeading.setVisibility(View.VISIBLE);
                artistViewHolder.artistName.setText(martistnameVal.get(i));
                artistViewHolder.followers.setText(mfollowersVal.get(i));
                artistViewHolder.popularity.setText(mpopularityVal.get(i));
                //artistViewHolder.checkAt.setText(mcheckatVal.get(i));
                artistViewHolder.checkAt.setText(Html.fromHtml("<a href='"+mcheckatVal.get(i)+"'>Spotify</a> "));
                artistViewHolder.checkAt.setMovementMethod(LinkMovementMethod.getInstance());
                artistViewHolder.table.setVisibility(View.VISIBLE);
//
            }
            else{
                artistViewHolder.table.setVisibility(View.INVISIBLE);
                artistViewHolder.artistHeading.setVisibility(View.INVISIBLE);
            }
        }
        catch(Exception e){

        }

//        if(mbreakIndex >= 0 &&  i== mbreakIndex){
//            artistViewHolder.artistHeading.setText(martistnameVal.get(1));
//            artistViewHolder.artistHeading.setVisibility(View.VISIBLE);
//        }
        if(mcategory.equals("Music")){

        }
        else{
            artistViewHolder.table.setVisibility(View.GONE);
        }
        Glide.with(mContext)
                .asBitmap()
                .load(martistImgUrl.get(i))
                .into(artistViewHolder.artistImgURLs);


//        if(i<2){
//
//        artistViewHolder.artistHeading.setText(martistheading.get(i));
//        artistViewHolder.artistName.setText(martistnameVal.get(i));
//        artistViewHolder.followers.setText(mfollowersVal.get(i));
//        artistViewHolder.popularity.setText(mpopularityVal.get(i));
//        artistViewHolder.checkAt.setText(mcheckatVal.get(i));
//
//        }



    }

    @Override
    public int getItemCount() {
        return martistImgUrl.size();
    }

    public class artistViewHolder extends RecyclerView.ViewHolder{
        ImageView artistImgURLs;
        TextView artistHeading;
        TextView artistName;
        TextView followers;
        TextView popularity;
        TextView checkAt;
        RelativeLayout table;

        LinearLayout artistLayout;

        public artistViewHolder(@NonNull View itemView) {
            super(itemView);
            artistImgURLs = itemView.findViewById(R.id.artistImg);
            artistHeading = itemView.findViewById(R.id.artistHeading);
            artistName = itemView.findViewById(R.id.artistNameVal);
            followers = itemView.findViewById(R.id.followersVal);
            popularity = itemView.findViewById(R.id.popularityVal);
            checkAt = itemView.findViewById(R.id.checkatVal);
            table = itemView.findViewById(R.id.artistTable);
            artistLayout = itemView.findViewById(R.id.artistLayout);
        }
    }
}

