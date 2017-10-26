package com.sakurov.notes.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sakurov.notes.MainActivity;
import com.sakurov.notes.NotesRecyclerAdapter;
import com.sakurov.notes.R;
import com.sakurov.notes.entities.Note;
import com.sakurov.notes.helpers.DBSource;

import java.util.ArrayList;
import java.util.List;

public class NotesListFragment extends BaseFragment {

    public static final int ADD_NOTE_REQUEST = 800;

    private List<Note> mNotes = new ArrayList<>();
    private NotesRecyclerAdapter mNotesRecyclerAdapter;
    private DBSource mSource;

    public static NotesListFragment newInstance() {
        return new NotesListFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        FRAGMENT_TITLE = "My notes";
        super.onActivityCreated(savedInstanceState);

        mSource = new DBSource(getActivity());
        mNotes = mSource.getNotes(getCurrentUserId(getActivity()));
        mNotesRecyclerAdapter.updateList(mNotes);

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
                EditNoteDialogFragment fragment = EditNoteDialogFragment.newInstance();
                fragment.setTargetFragment(NotesListFragment.this, ADD_NOTE_REQUEST);
                fragment.show(getFragmentManager(), EditNoteDialogFragment.class.getSimpleName());
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ADD_NOTE_REQUEST) {
                updateRecycler();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateRecycler() {
        if (getActivity() != null) {
            mNotes = mSource.getNotes(getCurrentUserId(getActivity()));
            mNotesRecyclerAdapter.updateList(mNotes);
        }
    }
}
