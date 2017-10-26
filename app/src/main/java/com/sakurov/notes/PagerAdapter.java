package com.sakurov.notes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sakurov.notes.fragments.EditNoteFragment;
import com.sakurov.notes.fragments.EditNotificationFragment;

/**
 * Created by sakurov on 26.10.17.
 */

public class PagerAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                EditNoteFragment tab1 = EditNoteFragment.newInstance();
                return tab1;
            case 1:
                EditNotificationFragment tab2 = EditNotificationFragment.newInstance();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
