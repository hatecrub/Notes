package com.sakurov.notes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.sakurov.notes.Communicator;
import com.sakurov.notes.R;
import com.sakurov.notes.entities.Note;
import com.sakurov.notes.entities.User;
import com.sakurov.notes.helpers.DBSource;

/**
 * Created by sakurov on 23.10.17.
 */

public class EditNoteFragment extends Fragment {

    private EditText editNote;
    private FloatingActionButton fabDone;

    private DBSource mSource;
    private Communicator mCommunicator;

    private Note mNote;
    private User mUser;

    public EditNoteFragment setNote(Note note) {
        this.mNote = note;
        return this;
    }

    public EditNoteFragment setUser(User user) {
        this.mUser = user;
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
        View rootView = inflater.inflate(R.layout.fragment_edit_note, container, false);

        mSource = new DBSource(getActivity());

        editNote = rootView.findViewById(R.id.text);
        fabDone = rootView.findViewById(R.id.fab_done);

        if (mNote != null) {
            editNote.setText(mNote.getText());
        }

        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputValid()) {
                    if (mNote == null) {
                        mNote = new Note(mUser.getId(), editNote.getText().toString());
                        mNote.setId(mSource.addNote(mNote));
                        mCommunicator.replaceFragment(new NotesListFragment().setUser(mUser), false);
                    } else {
                        mNote.setText(editNote.getText().toString());
                        mNote.setDateEdited(System.currentTimeMillis());
                        mSource.updateNote(mNote);
                        mCommunicator.replaceFragment(new NotesListFragment().setUser(mUser), false);
                    }
                } else
                    Toast.makeText(getActivity(), "Note can't be empty!", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private boolean isInputValid() {
        return !editNote.getText().toString().isEmpty();
    }
}
