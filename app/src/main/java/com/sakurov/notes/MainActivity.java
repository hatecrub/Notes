package com.sakurov.notes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sakurov.notes.entities.User;
import com.sakurov.notes.fragments.ChooseFragment;
import com.sakurov.notes.fragments.NotesListFragment;
import com.sakurov.notes.helpers.DBSource;

public class MainActivity extends AppCompatActivity {

    PrefsManager mPrefsManager;

    private boolean mLogOffEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            mPrefsManager = PrefsManager.getInstance();

            if (mPrefsManager.isUserRemembered()) {
                User user = mPrefsManager.getCurrentUser();

                DBSource dbSource = new DBSource(this);

                if (dbSource.checkUser(user)) {
                    mPrefsManager.setCurrentUserId(dbSource.getUserId(user));
                    addAsRootFragment(NotesListFragment.newInstance());
                } else {
                    Toast.makeText(this, getString(R.string.user_dont_exist), Toast.LENGTH_SHORT).show();
                    addAsRootFragment(ChooseFragment.newInstance());
                }

            } else {
                addAsRootFragment(ChooseFragment.newInstance());
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).setVisible(mLogOffEnabled);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.log_out) {
            if (mLogOffEnabled) {
                mPrefsManager.clear();
                addAsRootFragment(ChooseFragment.newInstance());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void addAsRootFragment(Fragment fragment) {
        setLogOffEnabled(false);

        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void setLogOffEnabled(boolean flag) {
        mLogOffEnabled = flag;
        invalidateOptionsMenu();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("orientation", true);
    }

    public void setActionBarTitle(String title) {
        try {
            getSupportActionBar().setTitle(title);
        } catch (NullPointerException e) {
            Log.d(this.getClass().getSimpleName(), "No ActionBar to set title!");
        }
    }
}
