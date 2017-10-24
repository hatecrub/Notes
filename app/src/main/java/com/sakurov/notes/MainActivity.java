package com.sakurov.notes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Toast;

import com.sakurov.notes.fragments.ChooseFragment;

public class MainActivity extends AppCompatActivity {

    private long mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragmentManager().
                beginTransaction().
                add(R.id.container, ChooseFragment.newInstance()).
                addToBackStack(ChooseFragment.class.getSimpleName()).
                commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else if (System.currentTimeMillis() - mTimer > 1000) {
            mTimer = System.currentTimeMillis();
            Toast.makeText(this, "Click again to exit", Toast.LENGTH_SHORT).show();
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
