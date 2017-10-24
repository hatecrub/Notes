package com.sakurov.notes;

import android.support.v4.app.Fragment;

public interface Communicator {

    void replaceFragment(Fragment fragment, boolean addToBack);

}
