package com.sakurov.notes.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sakurov.notes.R;
import com.sakurov.notes.fragments.EditNoteFragment;
import com.sakurov.notes.fragments.EditNotificationFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    private String mTabTitles[];

    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mTabTitles = new String[]{context.getString(R.string.title_new_note),
                context.getString(R.string.title_new_notification)};
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
        return mTabTitles[position];
    }
}