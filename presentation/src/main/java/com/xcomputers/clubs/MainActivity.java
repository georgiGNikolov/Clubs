package com.xcomputers.clubs;


import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.xcomputers.clubs.clubs.clubslist.ClubsRecyclerDisplayView;
import com.xcomputers.clubs.clubs.clubsswipe.ClubsSwipeDisplayContainer;
import com.xcomputers.clubs.clubs.clubsswipe.ClubsSwipeDisplayView;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private static final String CURRENT_VIEW_KEY = "MainActivity.CURRENT_VIEW_KEY";
    private static final int POSITION_RECYLER = 0;
    private static final int POSITION_SWIPE = 1;
    private int currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.clubs_toolbar_lbl);
        }
        attachFragment(savedInstanceState);
    }

    private void attachFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            currentView = POSITION_RECYLER;
            getSupportFragmentManager().beginTransaction().add(R.id.container, new ClubsRecyclerDisplayView(), ClubsRecyclerDisplayView.TAG).commit();
        } else {
            currentView = savedInstanceState.getInt(CURRENT_VIEW_KEY);
            if (currentView == POSITION_RECYLER) {
                attachRecyclerFrag();
            } else {
                attachSwipeFrag();
            }
        }
    }

    private void attachRecyclerFrag() {
        if (getSupportFragmentManager().findFragmentByTag(ClubsRecyclerDisplayView.TAG) == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ClubsRecyclerDisplayView(), ClubsRecyclerDisplayView.TAG).commit();
        } else {
            Fragment frag = getSupportFragmentManager().findFragmentByTag(ClubsRecyclerDisplayView.TAG);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, frag, ClubsRecyclerDisplayView.TAG).commit();
        }
    }

    private void attachSwipeFrag() {
        if (getSupportFragmentManager().findFragmentByTag(ClubsSwipeDisplayContainer.TAG) == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ClubsSwipeDisplayContainer(), ClubsSwipeDisplayContainer.TAG).commit();
        } else {
            Fragment frag = getSupportFragmentManager().findFragmentByTag(ClubsSwipeDisplayContainer.TAG);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, frag, ClubsSwipeDisplayContainer.TAG).commit();
        }
    }

    private void toggleView() {
        if (currentView == POSITION_RECYLER) {
            currentView = POSITION_SWIPE;
            attachSwipeFrag();
        } else {
            currentView = POSITION_RECYLER;
            attachRecyclerFrag();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_VIEW_KEY, currentView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            toggleView();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
