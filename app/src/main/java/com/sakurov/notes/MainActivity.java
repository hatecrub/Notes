package com.sakurov.notes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sakurov.notes.entities.User;
import com.sakurov.notes.fragments.AuthFragment;
import com.sakurov.notes.fragments.ChooseFragment;
import com.sakurov.notes.fragments.NotesListFragment;
import com.sakurov.notes.helpers.DBSource;

public class MainActivity extends AppCompatActivity {

    //    private long mTimer;
    SharedPreferences mPreferences;

    private boolean mLogOffEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreferences = getPreferences(Context.MODE_PRIVATE);
        boolean isUser = mPreferences.getBoolean(AuthFragment.IS_USER, false);

        if (isUser) {
            User user = new User(mPreferences.getString(AuthFragment.USER_NAME, null),
                    mPreferences.getString(AuthFragment.USER_PASS, null));

            DBSource dbSource = new DBSource(this);

            if (dbSource.checkUser(user)) {
                user.setId(dbSource.getUserId(user));
                addAsRootFragment(NotesListFragment.newInstance(user));
            } else {
                Toast.makeText(this, getString(R.string.user_dont_exist), Toast.LENGTH_SHORT).show();
                addAsRootFragment(ChooseFragment.newInstance());
            }
            
        } else {
            addAsRootFragment(ChooseFragment.newInstance());
        }
    }

    @Override
    public void onBackPressed() {
//        if (getFragmentManager().getBackStackEntryCount() > 0) {
//            super.onBackPressed();
//        } else if (System.currentTimeMillis() - mTimer > 1000) {
//            mTimer = System.currentTimeMillis();
//            Toast.makeText(this, "Click again to exit", Toast.LENGTH_SHORT).show();
//        } else
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
                mPreferences.edit().clear().apply();
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
}
