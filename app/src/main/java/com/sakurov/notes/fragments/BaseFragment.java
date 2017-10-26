package com.sakurov.notes.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sakurov.notes.MainActivity;
import com.sakurov.notes.R;

import static com.sakurov.notes.fragments.AuthFragment.USER_ID;
import static com.sakurov.notes.fragments.AuthFragment.USER_NAME;

abstract class BaseFragment extends Fragment {

    protected String FRAGMENT_TITLE = "Notes";

    void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        if (addToBackStack)
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }

    public void addAsRootFragment(Fragment fragment) {
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        replaceFragment(fragment, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setActionBarTitle(FRAGMENT_TITLE);

        enableLogOutMenuItem(false);
    }

    public static long getCurrentUserId(Activity activity) {
        return activity.getPreferences(Context.MODE_PRIVATE).getLong(USER_ID, 0);
    }

    public static String getCurrentUserName(Activity activity) {
        return activity.getPreferences(Context.MODE_PRIVATE).getString(USER_NAME, null);
    }

    protected void enableLogOutMenuItem(boolean flag) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setLogOffEnabled(flag);
    }
}
