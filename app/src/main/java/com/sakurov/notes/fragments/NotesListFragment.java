package com.sakurov.notes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sakurov.notes.Communicator;
import com.sakurov.notes.NotesRecyclerAdapter;
import com.sakurov.notes.R;
import com.sakurov.notes.entities.Note;
import com.sakurov.notes.entities.User;
import com.sakurov.notes.helpers.DBSource;

import java.util.ArrayList;
import java.util.List;

public class NotesListFragment extends Fragment {

    private User mUser;
    private List<Note> mNotes = new ArrayList<>();

    private RecyclerView mNotesRecycler;
    private NotesRecyclerAdapter mNotesRecyclerAdapter;

    private FloatingActionButton mFabAdd;

    private Communicator mCommunicator;

    private DBSource mSource;

    public NotesListFragment setUser(User user) {
        this.mUser = user;
        return this;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCommunicator = (Communicator) getActivity();
        if (mNotesRecyclerAdapter != null) {
            mNotesRecyclerAdapter.setCommunicator(mCommunicator);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_notes_list, container, false);

        mSource = new DBSource(getActivity());
        mNotes = mSource.getNotes(mUser);
        mNotesRecycler = rootView.findViewById(R.id.notes_list);
        mNotesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNotesRecyclerAdapter = new NotesRecyclerAdapter(mNotes, mUser);
        mNotesRecycler.setAdapter(mNotesRecyclerAdapter);

        mFabAdd = rootView.findViewById(R.id.fab);
        mFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCommunicator.replaceFragment(new EditNoteFragment().setUser(mUser), true);
            }
        });

        return rootView;
    }
}
