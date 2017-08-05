package com.xcomputers.clubs.clubs.clubslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neykov.mvp.support.ViewFragment;
import com.xcomputers.clubs.R;
import com.xcomputers.clubs.clubs.ClubsDetailsActivity;
import com.xcomputers.networking.clubs.Club;
import com.xcomputers.clubs.clubs.ClubsDisplayView;
import com.xcomputers.clubs.clubs.ClubsPresenter;

import java.util.List;

/**
 * Created by xComputers on 05/08/2017.
 */

public class ClubsRecyclerDisplayView extends ViewFragment<ClubsPresenter> implements ClubsDisplayView {

    public static final String TAG = "ClubsRecyclerDisplayView.TAG";

    private RecyclerView clubsRecycler;
    private RelativeLayout errorLayout;
    private Button retryBtn;
    private TextView errorMessageTextView;
    private ProgressBar loadingBar;
    private ClubsRecyclerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.clubs_recycler_display_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clubsRecycler = (RecyclerView) view.findViewById(R.id.clubs_recycler);
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
        clubsRecycler.setVisibility(View.VISIBLE);
        if (adapter == null) {
            adapter = new ClubsRecyclerAdapter(clubList, club -> {
                Intent intent = new Intent(getContext(), ClubsDetailsActivity.class);
                intent.putExtra(ClubsDetailsActivity.DETAILS_CLUB_KEY, club);
                startActivity(intent);
            });
            clubsRecycler.setAdapter(adapter);
        } else {
            adapter.setData(clubList);
        }
        if(clubsRecycler.getLayoutManager() == null){
            clubsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    @Override
    public void displayNoInternetErrorMessage() {
        clubsRecycler.setVisibility(View.GONE);
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
        clubsRecycler.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
        retryBtn.setVisibility(View.GONE);
        errorMessageTextView.setText(R.string.lbl_no_data);
    }

    @Override
    public void showLoading() {
        errorLayout.setVisibility(View.GONE);
        clubsRecycler.setVisibility(View.GONE);
        loadingBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingBar.setVisibility(View.GONE);
    }

    @Override
    public ClubsPresenter createPresenter() {
        return new ClubsPresenter();
    }
}
