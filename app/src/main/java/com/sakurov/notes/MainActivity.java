package com.sakurov.notes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sakurov.notes.entities.User;
import com.sakurov.notes.fragments.AuthFragment;
import com.sakurov.notes.fragments.ChooseFragment;
import com.sakurov.notes.fragments.NotesListFragment;
import com.sakurov.notes.helpers.DBSource;

public class MainActivity extends AppCompatActivity {

    SharedPreferences mPreferences;

    private boolean mLogOffEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            mPreferences = getPreferences(Context.MODE_PRIVATE);
            boolean isUser = mPreferences.getBoolean(AuthFragment.IS_USER_REMEMBERED, false);

            if (isUser) {
                User user = new User(mPreferences.getString(AuthFragment.USER_NAME, null),
                        mPreferences.getString(AuthFragment.USER_PASS, null));

                DBSource dbSource = new DBSource(this);

                if (dbSource.checkUser(user)) {
                    user.setId(dbSource.getUserId(user));
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.log_out) {
            if (mLogOffEnabled) {
                getPreferences(Context.MODE_PRIVATE).edit().clear().apply();
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
