package com.xcomputers.networking.clubs;

import com.xcomputers.networking.base.BaseService;
import com.xcomputers.networking.base.RetrofitInterface;

import java.util.List;

import rx.Observable;

/**
 * Created by xComputers on 05/08/2017.
 */
@RetrofitInterface(retrofitApi = ClubsApi.class)
public class ClubsService extends BaseService<ClubsApi> {

    public Observable<List<Club>> getClubs(){
        return serviceApi.getClubs();
    }
}
