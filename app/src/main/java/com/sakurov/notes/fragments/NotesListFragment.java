package com.sakurov.notes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sakurov.notes.NotesRecyclerAdapter;
import com.sakurov.notes.PrefsManager;
import com.sakurov.notes.R;
import com.sakurov.notes.entities.Note;
import com.sakurov.notes.helpers.DBSource;

import java.util.List;

public class NotesListFragment extends BaseFragment {

    private NotesRecyclerAdapter mNotesRecyclerAdapter;

    public static NotesListFragment newInstance() {
        return new NotesListFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        FRAGMENT_TITLE = "My notes";
        super.onActivityCreated(savedInstanceState);

        DBSource dbSource = new DBSource(getActivity());
        List<Note> notes = dbSource.getNotes(PrefsManager.getInstance().getCurrentUserID());
        mNotesRecyclerAdapter.updateList(notes);

        enableLogOutMenuItem(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_notes_list, container, false);
        RecyclerView notesRecycler = rootView.findViewById(R.id.notes_list);
        notesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        mNotesRecyclerAdapter = new NotesRecyclerAdapter(getFragmentManager());
        notesRecycler.setAdapter(mNotesRecyclerAdapter);

        FloatingActionButton fabAddNote = rootView.findViewById(R.id.fab);
        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(EditNoteFragment.newInstance(), true);
            }
        });

        return rootView;
    }
}
