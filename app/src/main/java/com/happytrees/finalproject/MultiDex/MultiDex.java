package com.happytrees.finalproject.MultiDex;

import android.content.Context;

import com.orm.SugarApp;
import com.orm.SugarContext;

/**
 * Created by Boris on 3/7/2018.
 */

public class MultiDex extends SugarApp {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        android.support.multidex.MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        SugarContext.terminate();
        super.onTerminate();
    }
}
