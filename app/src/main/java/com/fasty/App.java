package com.fasty;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.v7.preference.PreferenceManager;

import com.fasty.helper.FileHelper;
import com.fasty.helper.TypeFaceHelper;
import com.fasty.provider.icon.IconCache;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.FirebaseDatabase;
import com.orm.SugarContext;

/**
 * Created by Kosh on 24 May 2016, 7:51 PM
 */

public class App extends Application {

    private static App instance;
    private FirebaseAnalytics firebaseAnalytics;
    private IconCache iconCache;

    @Override public void onCreate() {
        super.onCreate();
        instance = this;
        SugarContext.init(this.getApplicationContext());
        FileHelper.initFolderName(getString(R.string.app_name));
        TypeFaceHelper.generateTypeface(this.getApplicationContext());
        PreferenceManager.setDefaultValues(this, R.xml.fa_settings, false);
        FirebaseDatabase.getInstance().setPersistenceEnabled(false);
    }

    @NonNull public static App getInstance() {
        return instance;
    }

    @NonNull public FirebaseAnalytics getFirebaseAnalytics() {
        if (firebaseAnalytics == null) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        }
        return firebaseAnalytics;
    }

    @NonNull public IconCache getIconCache() {
        if (iconCache == null) {
            iconCache = new IconCache(this.getApplicationContext());
        }
        return iconCache;
    }

    public void flushIconPack() {
        if (iconCache != null) {
            iconCache.flush();
            iconCache = null;
        }
    }
}
