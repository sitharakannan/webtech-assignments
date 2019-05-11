package com.example.skamalak.eventsearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EventDetails extends AppCompatActivity {

    //private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private int clickerId = 0;
    private String eventHeading;
    private String eventId;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        eventHeading = extras.getString("EXTRA_EVENTNAME");
        setTitle(eventHeading);
        eventId = extras.getString("EXTRA_EVENTID");
        setTitle(eventHeading);
        setContentView(R.layout.activity_event_details);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsEvent);
        tabLayout.setupWithViewPager(mViewPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_event_details, menu);
        MenuItem favIcon = menu.findItem(R.id.action_favorite);

        if (mPreferences.contains(eventId)) {
            favIcon.setIcon(R.drawable.heart_fill_red);
        }
        else{
            favIcon.setIcon(R.drawable.heart_fill_white);
        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            Log.d("EventDetails", String.valueOf(clickerId));
            if (mPreferences.contains(eventId)) {

                item.setIcon(R.drawable.heart_fill_white);
                String msg = eventHeading + " was removed from favorites";
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

                Log.d("clickingImgs removal: ", eventId);
                mEditor.remove(eventId);
                mEditor.commit();

            }
            else {

                item.setIcon(R.drawable.heart_fill_red);
                String msg = eventHeading + " was added to favorites";
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

                Log.d("clickingImgs add: ", eventId);


                mEditor.putString(eventId, "True");
                mEditor.commit();

            }
        }

        if (id == R.id.action_twitter) {
            Log.d("EventDetails", "in tweet it");


        }
        return super.onOptionsItemSelected(item);
    }


    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new eventFragment(), "EVENT");
        adapter.addFragment(new artistFragment(), "ARTIST(S)");
        adapter.addFragment(new venueFragment(), "VENUE");
        adapter.addFragment(new upcomingFragment(), "UPCOMING");
        viewPager.setAdapter(adapter);
    }

    public class SectionsPageAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public SectionsPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }
}






