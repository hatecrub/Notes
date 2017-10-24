package com.sakurov.notes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.sakurov.notes.fragments.ChooseFragment;

public class MainActivity extends AppCompatActivity implements Communicator {

    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();

        replaceFragment(new ChooseFragment(),true);
    }

    @Override
    public void replaceFragment(Fragment fragment, boolean addToBack) {
        Log.d("back", "" + mFragmentManager.getBackStackEntryCount());
        if (addToBack) {
            mFragmentManager.
                    beginTransaction().
                    replace(R.id.container, fragment).
                    addToBackStack(fragment.getClass().getSimpleName()).
                    commit();
        } else {
            mFragmentManager.
                    beginTransaction().
                    replace(R.id.container, fragment).
                    commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
