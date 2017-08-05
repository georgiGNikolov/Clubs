package com.xcomputers.clubs.clubs;

import com.neykov.mvp.RxPresenter;
import com.xcomputers.networking.clubs.Club;
import com.xcomputers.networking.clubs.ClubsService;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xComputers on 05/08/2017.
 */

public class ClubsPresenter extends RxPresenter<ClubsDisplayView> {

    private ClubsService service;
    private List<Club> clubList;

    public ClubsPresenter() {
        service = new ClubsService();
        clubList = new ArrayList<>();
    }

    public void getData() {

      /*  if (!clubList.isEmpty()) {
            doWhenViewBound(clubsDisplayView -> clubsDisplayView.displayClubs(clubList));
        } else {*/
            doWhenViewBound(ClubsDisplayView::showLoading);
            //TODO check if the dataBase contains data
           /* if (db contains){
                //TODO get from db and show list

                //TODO after get from db fire the request silently and update the database
            }else{*/
           this.add(
                service.getClubs()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(deliver())
                        .subscribe(delivery -> delivery.split(
                                (clubsDisplayView, clubsApiResponses) -> {
                                    this.clubList = clubsApiResponses;
                                    clubsDisplayView.hideLoading();
                                    if(clubList.isEmpty()){
                                        clubsDisplayView.displayNoDataErrorMessage();
                                    }else{
                                        clubsDisplayView.displayClubs(clubsApiResponses);
                                        //updateDataBase(clubsApiResponses);
                                    }
                                }, (clubsDisplayView, throwable) -> {
                                    clubsDisplayView.hideLoading();
                                    if (throwable instanceof ConnectException || throwable instanceof UnknownHostException) {
                                        clubsDisplayView.displayNoInternetErrorMessage();
                                    } else {
                                        clubsDisplayView.displayNoDataErrorMessage();
                                    }
                                }
                        )));
            }
        //}
    //}

    private void updateSilently(){
        service.getClubs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(clubs -> {
                    this.clubList = clubs;
                    updateDataBase(clubList);
                    doWhenViewBound(clubsDisplayView -> clubsDisplayView.displayClubs(clubList));
                });
    }

    private void updateDataBase(List<Club> list) {

        //TODO write in db
    }
}
