package com.xcomputers.clubs.clubs;

import com.xcomputers.networking.clubs.Club;

import java.util.List;

/**
 * Created by xComputers on 05/08/2017.
 */

public interface ClubsDisplayView {


    void displayClubs(List<Club> clubs);
    void displayNoInternetErrorMessage();
    void displayNoDataErrorMessage();
    void showLoading();
    void hideLoading();
}
