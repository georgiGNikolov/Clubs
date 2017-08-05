package com.xcomputers.networking.clubs;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by xComputers on 05/08/2017.
 */

interface ClubsApi {

    @GET("Clubs")
    Observable<List<Club>> getClubs();
}
