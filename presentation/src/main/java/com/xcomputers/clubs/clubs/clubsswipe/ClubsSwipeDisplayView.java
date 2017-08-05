package com.xcomputers.clubs.clubs.clubsswipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xcomputers.clubs.R;
import com.xcomputers.clubs.clubs.ClubsDetailsActivity;
import com.xcomputers.networking.clubs.Club;

/**
 * Created by xComputers on 05/08/2017.
 */

public class ClubsSwipeDisplayView extends Fragment {



    private static final String CLUB_KEY = "ClubsSwipeDisplayView.CLUB_KEY";

    private ImageView clubImage;
    private TextView clubNameTV;

    private Club club;

    public static ClubsSwipeDisplayView newInstance(Club club){
        ClubsSwipeDisplayView fragment = new ClubsSwipeDisplayView();
        Bundle arguments = new Bundle();
        arguments.putParcelable(CLUB_KEY, club);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() == null || !getArguments().containsKey(CLUB_KEY)){
            throw new IllegalArgumentException("The supplied Club is null for " + this.getClass().getCanonicalName());
        }
        club = getArguments().getParcelable(CLUB_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.clubs_swipe_display_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clubImage = (ImageView) view.findViewById(R.id.club_image);
        clubNameTV = (TextView) view.findViewById(R.id.club_name);
        Glide.with(getContext()).load(club.getFullSizeUrl()).placeholder(R.drawable.noimage).into(clubImage);
        clubNameTV.setText(club.getName());
        view.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ClubsDetailsActivity.class);
            intent.putExtra(ClubsDetailsActivity.DETAILS_CLUB_KEY, club);
            startActivity(intent);
        });
    }
}
