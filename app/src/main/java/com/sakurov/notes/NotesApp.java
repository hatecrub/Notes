package com.sakurov.notes;

import android.app.Application;

/**
 * Created by sakurov on 26.10.17.
 */

public class NotesApp extends Application {

    private PrefsManager mPrefManager;

    @Override
    public void onCreate() {
        super.onCreate();

        PrefsManager.initInstance(getApplicationContext());
        mPrefManager = PrefsManager.getInstance();
    }
}
