package com.sakurov.notes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sakurov.notes.adapters.PagerAdapter;
import com.sakurov.notes.R;

public class ViewPagerFragment extends BaseFragment {

    public static ViewPagerFragment newInstance() {
        return new ViewPagerFragment();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.viewpager_fragment, container, false);

        ViewPager viewPager = rootView.findViewById(R.id.view_pager);
        viewPager.setAdapter(new PagerAdapter(getChildFragmentManager(), getContext()));

        setTitle(getString(R.string.title_add));

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            showSoftKeyboard(null);
        }
    }
}