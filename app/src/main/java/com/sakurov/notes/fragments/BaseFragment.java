package com.sakurov.notes.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sakurov.notes.MainActivity;
import com.sakurov.notes.R;

abstract class BaseFragment extends Fragment {

    protected static final String NOTE = "note";

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

        setActionBarTitle(FRAGMENT_TITLE);
        enableLogOutMenuItem(false);
    }

    protected void enableLogOutMenuItem(boolean flag) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setLogOffEnabled(flag);
    }

    protected void setActionBarTitle(String title) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setActionBarTitle(title);
    }
}
