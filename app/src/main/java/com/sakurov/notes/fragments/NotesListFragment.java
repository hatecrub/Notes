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
import com.sakurov.notes.R;
import com.sakurov.notes.entities.Note;
import com.sakurov.notes.entities.User;
import com.sakurov.notes.helpers.DBSource;

import java.util.ArrayList;
import java.util.List;

public class NotesListFragment extends BaseFragment {

    private List<Note> mNotes = new ArrayList<>();

    private NotesRecyclerAdapter mNotesRecyclerAdapter;

    public static NotesListFragment newInstance(User user) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(USER, user);

        NotesListFragment fragment = new NotesListFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected void readBundle(Bundle bundle) {
        if (bundle != null) {
            mUser = bundle.getParcelable(USER);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSource = new DBSource(getActivity());
        mNotes = mSource.getNotes(mUser.getId());
        mNotesRecyclerAdapter.updateList(mNotes);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_notes_list, container, false);
        RecyclerView notesRecycler = rootView.findViewById(R.id.notes_list);
        notesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        readBundle(getArguments());

        mNotesRecyclerAdapter = new NotesRecyclerAdapter(getFragmentManager(), mUser);
        notesRecycler.setAdapter(mNotesRecyclerAdapter);

        FloatingActionButton fabAddNote = rootView.findViewById(R.id.fab);
        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(EditNoteFragment.newInstance(mUser), true);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mNotes = mSource.getNotes(mUser.getId());
        mNotesRecyclerAdapter.updateList(mNotes);
    }
}
