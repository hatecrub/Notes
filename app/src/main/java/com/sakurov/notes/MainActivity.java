package com.sakurov.notes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sakurov.notes.fragments.ChooseFragment;

public class MainActivity extends AppCompatActivity implements Communicator {

    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();

        ChooseFragment chooseFragment = new ChooseFragment();

        mFragmentManager.
                beginTransaction().
                add(R.id.container, chooseFragment).
                addToBackStack(null).
                commit();

    }

    @Override
    public void replaceFragment(Fragment fragment) {
        mFragmentManager.
                beginTransaction().
                replace(R.id.container, fragment).
                addToBackStack(null).
                commit();
    }
}
