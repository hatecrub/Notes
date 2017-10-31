package com.sakurov.notes.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sakurov.notes.fragments.EditNoteFragment;
import com.sakurov.notes.fragments.EditNotificationFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[]{"New Note", "New Notification"};

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return EditNoteFragment.newInstance();
            case 1:
                return EditNotificationFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}

