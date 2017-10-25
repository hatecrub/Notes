package com.sakurov.notes.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sakurov.notes.R;
import com.sakurov.notes.entities.Note;
import com.sakurov.notes.entities.User;
import com.sakurov.notes.helpers.DBSource;

abstract class BaseFragment extends Fragment {

    protected static final String USER = "user";
    protected static final String NOTE = "note";

    protected Note mNote;
    protected User mUser;

    protected DBSource mSource;

    abstract protected void readBundle(Bundle bundle);

    void replaceFragment(Fragment fragment, boolean addToBackStack) {
        Log.d(this.getClass().getSimpleName(),
                "OnReplaceFragment to " +
                        fragment.getClass().getSimpleName() + ": " +
                        getFragmentManager().getBackStackEntryCount());

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

/*    @Override
    public void onResume() {
        super.onResume();
        Log.d(this.getClass().getSimpleName(),
                "OnResume: " +
                        getFragmentManager().getBackStackEntryCount());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(this.getClass().getSimpleName(),
                "OnCreate: " +
                        getFragmentManager().getBackStackEntryCount());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(this.getClass().getSimpleName(),
                "OnPause: " +
                        getFragmentManager().getBackStackEntryCount());
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(this.getClass().getSimpleName(),
                "OnStop: " +
                        getFragmentManager().getBackStackEntryCount());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(this.getClass().getSimpleName(),
                "OnDestroy: " +
                        getFragmentManager().getBackStackEntryCount());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(this.getClass().getSimpleName(),
                "OnDetach: " +
                        getFragmentManager().getBackStackEntryCount());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(this.getClass().getSimpleName(),
                "OnAttach: " +
                        getFragmentManager().getBackStackEntryCount());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(this.getClass().getSimpleName(),
                "OnCreateView: " +
                        getFragmentManager().getBackStackEntryCount());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(this.getClass().getSimpleName(),
                "OnActivityCreated: " +
                        getFragmentManager().getBackStackEntryCount());
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(this.getClass().getSimpleName(),
                "OnStart: " +
                        getFragmentManager().getBackStackEntryCount());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(this.getClass().getSimpleName(),
                "OnDestroyView: " +
                        getFragmentManager().getBackStackEntryCount());
    }*/
}
