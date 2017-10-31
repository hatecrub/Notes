package com.sakurov.notes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sakurov.notes.adapters.ItemsRecyclerAdapter;
import com.sakurov.notes.utils.PrefsManager;
import com.sakurov.notes.R;
import com.sakurov.notes.entities.Item;
import com.sakurov.notes.data.DataSource;

import java.util.List;

public class NotesListFragment extends BaseFragment {

    //    private NotesRecyclerAdapter mNotesRecyclerAdapter;
    private ItemsRecyclerAdapter mItemsRecyclerAdapter;

    public static NotesListFragment newInstance() {
        return new NotesListFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setTitle("My notes");
        super.onActivityCreated(savedInstanceState);

        DataSource dbSource = new DataSource(getActivity());
//        List<Note> notes = dbSource.getNotes(PrefsManager.getInstance().getCurrentUserID());
//        mNotesRecyclerAdapter.updateList(notes);
        List<Item> items = dbSource.getAllRecords(PrefsManager.getInstance().getCurrentUserID());
        mItemsRecyclerAdapter.updateList(items);

        enableLogOutMenuItem(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_notes_list, container, false);
        RecyclerView notesRecycler = rootView.findViewById(R.id.notes_list);
        notesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

//        mNotesRecyclerAdapter = new NotesRecyclerAdapter(getFragmentManager());
//        notesRecycler.setAdapter(mNotesRecyclerAdapter);
        mItemsRecyclerAdapter = new ItemsRecyclerAdapter(getFragmentManager());
        notesRecycler.setAdapter(mItemsRecyclerAdapter);

        FloatingActionButton fabAddNote = rootView.findViewById(R.id.fab);
        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(ViewPagerFragment.newInstance(), true);
            }
        });

        return rootView;
    }

}
