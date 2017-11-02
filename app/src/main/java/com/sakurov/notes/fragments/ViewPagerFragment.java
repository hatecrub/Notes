package com.sakurov.notes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sakurov.notes.R;
import com.sakurov.notes.adapters.PagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ViewPagerFragment extends BaseFragment {

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    public static ViewPagerFragment newInstance() {
        return new ViewPagerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.viewpager_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);

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