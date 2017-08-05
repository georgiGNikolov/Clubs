package com.xcomputers.clubs;

import android.app.Application;

import com.xcomputers.clubs.clubs.util.DbHelper;
import com.xcomputers.networking.base.RetrofitManager;

/**
 * Created by xComputers on 05/08/2017.
 */

public class ClubsApplication extends Application {

    public static final String BASE_URL = "https://my-json-server.typicode.com/martin-lalev/typicode_test/";

    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitManager.getInstance().init(BASE_URL);
        DbHelper.init(this);
    }
}
