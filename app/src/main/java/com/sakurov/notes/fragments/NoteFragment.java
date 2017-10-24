package com.sakurov.notes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sakurov.notes.Communicator;
import com.sakurov.notes.R;
import com.sakurov.notes.entities.Note;
import com.sakurov.notes.entities.User;

public class NoteFragment extends Fragment {

    private TextView text, author, created, edited;
    private FloatingActionButton fabEdit;

    private Note mNote;
    private User mUser;

    private Communicator mCommunicator;

    public NoteFragment setUser(User user) {
        this.mUser = user;
        return this;
    }

    public NoteFragment setNote(Note note) {
        this.mNote = note;
        return this;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCommunicator = (Communicator) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_note, container, false);
        text = rootView.findViewById(R.id.text);
        author = rootView.findViewById(R.id.author);
        created = rootView.findViewById(R.id.created);
        edited = rootView.findViewById(R.id.edited);
        fabEdit = rootView.findViewById(R.id.fab_edit);

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCommunicator != null && mUser != null && mNote != null)
                    mCommunicator.replaceFragment(new EditNoteFragment().setUser(mUser).setNote(mNote), true);
            }
        });

        text.setText(mNote.getText());
        author.setText("Author: " + mUser.getName());
        created.setText("Created: " + mNote.getDateCreated());
        edited.setText("Edited: " + mNote.getDateEdited());
        return rootView;
    }
}
