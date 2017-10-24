package com.sakurov.notes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sakurov.notes.fragments.ChooseFragment;

public class MainActivity extends AppCompatActivity implements Communicator {

    FragmentManager mFragmentManager;

    long mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();

        ChooseFragment chooseFragment = new ChooseFragment();

        mFragmentManager.
                beginTransaction().
                add(R.id.container, chooseFragment).
                addToBackStack(chooseFragment.getClass().getSimpleName()).
                commit();

    }

    @Override
    public void replaceFragment(Fragment fragment, boolean addToBack) {

        String backStateName = fragment.getClass().getName();

        boolean fragmentPopped = mFragmentManager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped) {
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
    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() > 0) {
            mFragmentManager.popBackStack();
        } else {
            if (System.currentTimeMillis() - mTimer > 1000) {
                mTimer = System.currentTimeMillis();
                Toast.makeText(this, "Click again to exit", Toast.LENGTH_SHORT).show();
            } else
                super.onBackPressed();
        }
    }

    @Override
    public void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out: {
//                clearBackStack();
                replaceFragment(new ChooseFragment(), false);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
