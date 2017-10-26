package com.sakurov.notes;

import android.content.Context;
import android.content.SharedPreferences;

import com.sakurov.notes.entities.User;

public class PrefsManager {

    private static PrefsManager mInstance;
    private SharedPreferences mPrefs;

    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_name";
    private static final String USER_PASS = "user_pass";
    private static final String IS_USER_REMEMBERED = "is_user_remembered";

    private PrefsManager() {
    }

    public static void initInstance(Context context) {
        mInstance = new PrefsManager();
        if (mInstance.mPrefs == null) {
            mInstance.mPrefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        }
    }

    public static PrefsManager getInstance() {
        return mInstance;
    }

    public long getCurrentUserID() {
        return mPrefs.getLong(USER_ID, 0);
    }

    public String getCurrentUserName() {
        return mPrefs.getString(USER_NAME, null);
    }

    public String getCurrentUserPass() {
        return mPrefs.getString(USER_PASS, null);
    }

    public boolean isUserRemembered() {
        return mPrefs.getBoolean(IS_USER_REMEMBERED, false);
    }

    public void setCurrentUserId(long userId) {
        mPrefs.edit()
                .putLong(USER_ID, userId)
                .apply();
    }

    public void setCurrentUserName(String userName) {
        mPrefs.edit()
                .putString(USER_NAME, userName)
                .apply();
    }

    public void setCurrentUserPass(String userPass) {
        mPrefs.edit()
                .putString(USER_PASS, userPass)
                .apply();
    }

    public void setRemembered(boolean isRemembered) {
        mPrefs.edit()
                .putBoolean(IS_USER_REMEMBERED, isRemembered)
                .apply();
    }

    public void clear() {
        mPrefs.edit().clear().apply();
    }

    public void setCurrentUser(User user, boolean isRemembered) {
        setRemembered(isRemembered);
        setCurrentUserId(user.getId());
        setCurrentUserName(user.getName());
        setCurrentUserPass(user.getPassword());
    }

    public User getCurrentUser() {
        return new User(getCurrentUserID(), getCurrentUserName(), getCurrentUserPass());
    }
}
