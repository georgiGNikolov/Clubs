package com.xcomputers.clubs.clubs.clubsswipe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neykov.mvp.support.ViewFragment;
import com.xcomputers.clubs.R;
import com.xcomputers.clubs.clubs.ClubsDisplayView;
import com.xcomputers.clubs.clubs.ClubsPresenter;
import com.xcomputers.networking.clubs.Club;

import java.util.List;

/**
 * Created by xComputers on 05/08/2017.
 */

public class ClubsSwipeDisplayContainer extends ViewFragment<ClubsPresenter> implements ClubsDisplayView {

    public static final String TAG = "ClubsSwipeDisplayContainer.TAG";

    private RelativeLayout errorLayout;
    private Button retryBtn;
    private TextView errorMessageTextView;
    private ProgressBar loadingBar;
    private ViewPager pager;
    private List<Club> clubs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.clubs_swipe_container_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pager = (ViewPager) view.findViewById(R.id.clubs_pager);
        errorLayout = (RelativeLayout) view.findViewById(R.id.error_layout);
        retryBtn = (Button) view.findViewById(R.id.retry_btn);
        errorMessageTextView = (TextView) view.findViewById(R.id.error_text_view);
        loadingBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        showLoading();
        getPresenter().getData();
    }

    @Override
    public void displayClubs(List<Club> clubList) {
        hideLoading();
        errorLayout.setVisibility(View.GONE);
        pager.setVisibility(View.VISIBLE);
        clubs = clubList;
        pager.setAdapter(new SwipeAdapter(getChildFragmentManager()));
    }

    @Override
    public void displayNoInternetErrorMessage() {
        pager.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
        retryBtn.setVisibility(View.VISIBLE);
        errorMessageTextView.setText(R.string.lbl_no_internet_error);
        retryBtn.setOnClickListener(v -> {
            showLoading();
            getPresenter().getData();
        });
    }

    @Override
    public void displayNoDataErrorMessage() {
        hideLoading();
        pager.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
        retryBtn.setVisibility(View.GONE);
        errorMessageTextView.setText(R.string.lbl_no_data);
    }

    @Override
    public void showLoading() {
        errorLayout.setVisibility(View.GONE);
        pager.setVisibility(View.GONE);
        loadingBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingBar.setVisibility(View.GONE);
    }

    private class SwipeAdapter extends FragmentPagerAdapter {

        public SwipeAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ClubsSwipeDisplayView.newInstance(clubs.get(position));
        }

        @Override
        public int getCount() {
            return clubs.size();
        }
    }


    @Override
    public ClubsPresenter createPresenter() {
        return new ClubsPresenter();
    }

}
