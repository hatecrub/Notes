package com.sakurov.notes;

import android.animation.LayoutTransition;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sakurov.notes.entities.User;
import com.sakurov.notes.fragments.ChooseFragment;
import com.sakurov.notes.fragments.NotesListFragment;
import com.sakurov.notes.data.DataSource;
import com.sakurov.notes.utils.PrefsManager;

public class MainActivity extends AppCompatActivity {

    private static final String IS_LAND = "isLand";

    PrefsManager mPrefsManager;

    private boolean mLogOffEnabled = false;

    private boolean isLand = false;
    private boolean isLandContainerShown = false;

    View detailsFrame;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        boolean oldIsLand;
        if (savedInstanceState != null) {
            oldIsLand = savedInstanceState.getBoolean(IS_LAND);
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                if (oldIsLand && !isLand) {
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container_land);
                    if (fragment != null) {
                        getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                        getSupportFragmentManager().executePendingTransactions();
                        replaceFragment(fragment, R.id.container);
                    }
                } else if (!oldIsLand && isLand) {
                    if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
                        if (fragment != null) {
                            getSupportFragmentManager().popBackStack();
                            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                            getSupportFragmentManager().executePendingTransactions();
                            showContainerLand();
                            replaceFragment(fragment, R.id.container_land);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPrefsManager = PrefsManager.getInstance();

        detailsFrame = findViewById(R.id.container_land);
        isLand = detailsFrame != null;

        if (savedInstanceState == null) {

            ((ViewGroup) findViewById(R.id.root)).getLayoutTransition()
                    .enableTransitionType(LayoutTransition.CHANGING);

            if (mPrefsManager.isUserRemembered()) {
                User user = mPrefsManager.getCurrentUser();

                DataSource dbSource = new DataSource(this);

                if (dbSource.checkUserPass(user)) {
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
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            getSupportFragmentManager().popBackStack();
            hideContainerLand();
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
            showContainerLand();
        } else
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
                hideContainerLand();
                addAsRootFragment(ChooseFragment.newInstance());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void addAsRootFragment(Fragment fragment) {
        setLogOffEnabled(false);

        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        getSupportFragmentManager()
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
        outState.putBoolean(IS_LAND, isLand);
    }

    public void setActionBarTitle(String title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

    public void hideContainerLand() {
        if (isLand && isLandContainerShown) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                    detailsFrame.getLayoutParams();
            params.weight = 0.0f;
            detailsFrame.setLayoutParams(params);
            isLandContainerShown = false;
        }
    }

    public void showContainerLand() {
        if (isLand && !isLandContainerShown) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                    detailsFrame.getLayoutParams();
            params.weight = 1.0f;
            detailsFrame.setLayoutParams(params);
            isLandContainerShown = true;
        }
    }

    public boolean isLand() {
        return isLand;
    }

    void replaceFragment(Fragment fragment, int container) {
        getSupportFragmentManager().beginTransaction()
                .replace(container, fragment)
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }
}