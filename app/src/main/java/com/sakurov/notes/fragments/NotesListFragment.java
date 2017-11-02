package com.sakurov.notes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sakurov.notes.adapters.ItemRecyclerAdapter;
import com.sakurov.notes.utils.PrefsManager;
import com.sakurov.notes.R;
import com.sakurov.notes.entities.Item;
import com.sakurov.notes.data.DataSource;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotesListFragment extends BaseFragment {

    @BindView(R.id.notes_list)
    RecyclerView notesRecycler;

    public static NotesListFragment newInstance() {
        return new NotesListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notes_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        setTitle(getString(R.string.title_my_notes));

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DataSource dataSource = new DataSource(getContext());
        List<Item> items = dataSource.getAllRecords(PrefsManager.getInstance().getCurrentUserID());

        notesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        notesRecycler.setAdapter(new ItemRecyclerAdapter(items, this));

        enableLogOutMenuItem(true);
    }

    @OnClick(R.id.fab)
    void onClick() {
        hideLandContainer();
        replaceFragment(ViewPagerFragment.newInstance(), true);
    }
}