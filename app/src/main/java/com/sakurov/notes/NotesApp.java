package com.sakurov.notes;

import android.app.Application;

import com.sakurov.notes.utils.PrefsManager;

public class NotesApp extends Application {

    PrefsManager mPrefManager;

    @Override
    public void onCreate() {
        super.onCreate();

        PrefsManager.initInstance(getApplicationContext());
        mPrefManager = PrefsManager.getInstance();
    }
}
