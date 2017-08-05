package com.xcomputers.clubs.clubs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xcomputers.clubs.R;
import com.xcomputers.networking.clubs.Club;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by xComputers on 05/08/2017.
 */

public class ClubsDetailsActivity extends AppCompatActivity {

    public static final String DETAILS_CLUB_KEY = "ClubsDetailsActivity.DETAILS_CLUB_KEY";

    private Club club;
    private TextView clubNameTV;
    private TextView clubCreatedAtTV;
    private ImageView clubImage;
    private Button emailBtn;
    private Button fbBtn;
    private Button callBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        club = getIntent().getParcelableExtra(DETAILS_CLUB_KEY);
        if (club == null) {
            throw new IllegalArgumentException("The supplied Club is null for " + this.getClass().getCanonicalName());
        }
        initFields();
        setDataToFields();
        initClickListeners();
    }

    private void initClickListeners(){
        emailBtn.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", club.getEmail(), null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
            Intent chooser = Intent.createChooser(emailIntent, getString(R.string.send_email_action_lbl));
            if(getPackageManager().resolveActivity(chooser, 0) != null) {
                startActivity(chooser);
            }else{
                Toast.makeText(this, R.string.implicit_intent_error, Toast.LENGTH_SHORT).show();
            }
        });
        fbBtn.setOnClickListener(v -> {
            String url = getString(R.string.fb_url);
            url = url.concat(club.getFbUrl());
            Intent fbIntent = new Intent(Intent.ACTION_VIEW);
            fbIntent.setData(Uri.parse(url));
            if(getPackageManager().resolveActivity(fbIntent, 0) != null) {
                startActivity(fbIntent);
            }else{
                Toast.makeText(this, R.string.implicit_intent_error, Toast.LENGTH_SHORT).show();
            }
        });
        callBtn.setOnClickListener(v -> {
            //The task did not specify whether this action should be DIAL or CALL so I arbitrarily chose DIAL
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", club.getPhoneNumber(), null));
            if(getPackageManager().resolveActivity(intent, 0) != null) {
                startActivity(intent);
            }else{
                Toast.makeText(this, R.string.implicit_intent_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initFields() {
        clubNameTV = (TextView) findViewById(R.id.club_name);
        clubCreatedAtTV = (TextView) findViewById(R.id.club_date);
        clubImage = (ImageView) findViewById(R.id.club_image);
        emailBtn = (Button) findViewById(R.id.email_button);
        fbBtn = (Button) findViewById(R.id.fb_button);
        callBtn = (Button) findViewById(R.id.call_button);
    }

    private void setDataToFields() {
        clubNameTV.setText(club.getName());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(club.getCreatedAt());
        clubCreatedAtTV.setText(calendar.get(Calendar.YEAR)
                + " "
                + calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                + " "
                + calendar.get(Calendar.DAY_OF_MONTH));
        Glide.with(this).load(club.getFullSizeUrl()).placeholder(R.drawable.noimage).into(clubImage);
    }
}
