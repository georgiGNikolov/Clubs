package com.xcomputers.clubs.clubs;

import com.neykov.mvp.RxPresenter;
import com.xcomputers.clubs.clubs.util.DbHelper;
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

        if (!clubList.isEmpty()) {
            //if the cache is not empty we use that for fastest results
            doWhenViewBound(clubsDisplayView -> clubsDisplayView.displayClubs(clubList));
        } else {
            //the cache is empty so we need to get data from somewhere
            doWhenViewBound(ClubsDisplayView::showLoading);
            //the database is faster than a network request so we try that first
            List<Club> clubs = DbHelper.getInstance().getAllData();
           if (clubs.size() > 0){
               //great! we have data in the database! we show the user this data and we
               // fire a network request silently to check if there is any new data or if the existing data needs updating
               this.clubList = clubs;
                doWhenViewBound(clubsDisplayView -> {
                clubsDisplayView.displayClubs(clubs);
                });
               updateSilently();
            }else{
               //There is no data in the memory cache and the database is empty as well
               // we have no choice but to request the data from the server
           this.add(
                service.getClubs()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(deliver())
                        .subscribe(delivery -> delivery.split(
                                (clubsDisplayView, clubsApiResponses) -> {
                                    //we have a response
                                    this.clubList = clubsApiResponses;
                                    clubsDisplayView.hideLoading();
                                    if(clubList.isEmpty()){
                                        //the server returned an empty array
                                        clubsDisplayView.displayNoDataErrorMessage();
                                    }else{
                                        //there is a non empty response
                                        // we show the data to the user and we write the data in the database for future use
                                        clubsDisplayView.displayClubs(clubsApiResponses);
                                        addInitialEnties(clubsApiResponses);
                                    }
                                }, (clubsDisplayView, throwable) -> {
                                    clubsDisplayView.hideLoading();
                                    //there was a problem with the request
                                    if (throwable instanceof ConnectException || throwable instanceof UnknownHostException) {
                                        //this either means there is no internet connection or the server is down
                                        // we display a message with a retry button which will initiate the same flow again
                                        clubsDisplayView.displayNoInternetErrorMessage();
                                    } else {
                                        //some other kind of exception was thrown somewhere in the chain
                                        clubsDisplayView.displayNoDataErrorMessage();
                                    }
                                }
                        )));
            }
        }
    }

    private void updateSilently(){
        service.getClubs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(clubs -> {
                    updateDataBase(clubs);
                    doWhenViewBound(clubsDisplayView -> clubsDisplayView.displayClubs(clubList));
                },Throwable::printStackTrace);
    }

    private void addInitialEnties(List<Club> clubs){
        //used once only to add the entries to our empty database
        for(Club club : clubs){
            DbHelper.getInstance().addData(club);
        }
    }

    private void updateDataBase(List<Club> list) {
        if(clubList.size() == list.size()){
            checkDatesAndUpdate(list);
        }else{
            if(list.size() > clubList.size()){
                //The new items are more than the items we have. We loop to see which items we don't have
                List<Club> toAdd = new ArrayList<>();
                for(Club club : list){
                    if(!clubList.contains(club)){
                        toAdd.add(club);
                    }
                }
                for(Club club : toAdd){
                    DbHelper.getInstance().addData(club);
                }
                //Check if any of the old entries need updating
                checkDatesAndUpdate(list);
                //Refresh the memory cash with the new database data
                this.clubList = DbHelper.getInstance().getAllData();
            }else{
                //The items we have are more than the service returns. We need to see which items we need to delete
                List<Club> toDelete = new ArrayList<>();
                for(Club club : clubList){
                    if(!list.contains(club)){
                        toDelete.add(club);
                    }
                }
                //We delete said items
                for(Club club : toDelete){
                    DbHelper.getInstance().deleteData(club);
                }
                //We check if the rest of the items are up to date and update them if needed
                checkDatesAndUpdate(list);
                //We update the cache with the latest data from the database
                this.clubList = DbHelper.getInstance().getAllData();
            }
        }
    }

    private void checkDatesAndUpdate(List<Club> list){
        //we loop through all the enries and check if the entry from the server has been updated later than the entry we have
        // if so then we need to update our data
        for(Club fromServer : list) {
            for (Club fromCache : clubList) {
                if(fromCache.getId() == fromServer.getId()){
                    if(fromCache.getUpdatedAt().before(fromServer.getUpdatedAt())){
                        DbHelper.getInstance().updateData(fromServer);
                    }
                }
            }
        }
    }
}
