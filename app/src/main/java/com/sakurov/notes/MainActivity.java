package com.sakurov.notes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.sakurov.notes.fragments.ChooseFragment;

public class MainActivity extends AppCompatActivity {

//    private long mTimer;

    private boolean mLogOffEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addAsRootFragment(ChooseFragment.newInstance());
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
            if (mLogOffEnabled)
                addAsRootFragment(ChooseFragment.newInstance());
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
