package com.logos.mvp.logosswipe;

import android.database.sqlite.SQLiteDatabase;

import greendao.DaoMaster;
import greendao.DaoSession;

/**
 * Created by Sylvain on 30/01/15.
 */
public class App extends android.app.Application {
    private static App _INSTANCE = null;
    private DaoSession _DaoSession ;


    public static App getInstance() {
        return _INSTANCE;
    }

    @Override
    public void onCreate() {
        _INSTANCE = this;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this.getApplicationContext(), "forestWaves-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        _DaoSession = daoMaster.newSession();
    }


    public static DaoSession getSession() {
        return getInstance()._DaoSession;
    }
}
