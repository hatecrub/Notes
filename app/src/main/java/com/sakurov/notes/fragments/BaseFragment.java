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

import butterknife.Unbinder;

abstract class BaseFragment extends Fragment {

    protected static final String NOTE = "note";
    public static final String NOTIFICATION = "notification";

    //капсом через нижнее подчеркивание только константы
    private String FRAGMENT_TITLE;
    private MainActivity mainActivity;
    protected Unbinder unbinder;

    void replaceFragment(BaseFragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);

        //часто видел что одно действие пишут без {}
        // мое мнение, что лучше писать все равно, ибо если понадобится что-то добавить в иф
        // - можно сделать себе труднонаходимый баг.
        //подмечу что это не наставление исправить и писать всегда со скобками, просто мое мнение.
        if (addToBackStack)
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }

    public void addAsRootFragment(BaseFragment fragment) {
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        replaceFragment(fragment, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        if (FRAGMENT_TITLE != null)
            setActionBarTitle(FRAGMENT_TITLE);
        enableLogOutMenuItem(false);
    }

    protected void enableLogOutMenuItem(boolean flag) {
        mainActivity.setLogOffEnabled(flag);
    }

    protected void setActionBarTitle(String title) {
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

    public void showLandContainer() {
        mainActivity.showContainerLand();
    }

    public void hideLandContainer() {
        mainActivity.hideContainerLand();
    }

    public boolean isLand() {
        return mainActivity.isLand();
    }

    protected void update() {
    }

    protected void updateLandContainer() {
        if (isLand()) {
            BaseFragment fragment = (BaseFragment) getFragmentManager().findFragmentById(R.id.container_land);
            if (fragment != null)
                fragment.update();
        }
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null)
            unbinder.unbind();
        super.onDestroyView();
    }
}