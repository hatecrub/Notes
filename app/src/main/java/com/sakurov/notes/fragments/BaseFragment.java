package com.sakurov.notes.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.sakurov.notes.MainActivity;
import com.sakurov.notes.R;

abstract class BaseFragment extends Fragment {

    protected static final String NOTE = "note";
    public static final String NOTIFICATION = "notification";

    private String FRAGMENT_TITLE;

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
        if (FRAGMENT_TITLE != null)
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

    public void setTitle(String fragmentTitle) {
        this.FRAGMENT_TITLE = fragmentTitle;
    }

    protected void showSoftKeyboard(EditText editText) {
        if (editText != null)
            editText.requestFocus();

        InputMethodManager inputMethodManager = ((InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE));

        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    protected void showToast(int stringId) {
        Toast.makeText(getActivity(), getString(stringId), Toast.LENGTH_SHORT).show();
    }
}