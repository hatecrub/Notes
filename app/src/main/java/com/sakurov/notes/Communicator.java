package com.sakurov.notes;

import android.app.Fragment;

import com.sakurov.notes.entities.User;

public interface Communicator {

    void replaceFragment(Fragment fragment, boolean addToBack);

    void replaceFragment(String fragmentName);

    void clearBackStack();

    void setUser(User user);
}
