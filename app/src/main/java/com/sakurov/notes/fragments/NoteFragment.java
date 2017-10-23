package com.sakurov.notes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sakurov.notes.R;
import com.sakurov.notes.entities.Note;
import com.sakurov.notes.entities.User;

/**
 * Created by sakurov on 23.10.17.
 */

public class NoteFragment extends Fragment {

    TextView text, author, created, edited;

    Note mNote;
    User mUser;

    public NoteFragment setUser(User user) {
        this.mUser = user;
        return this;
    }

    public NoteFragment setNote(Note note) {
        this.mNote = note;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_note, container, false);
        text = rootView.findViewById(R.id.text);
        author = rootView.findViewById(R.id.author);
        created = rootView.findViewById(R.id.created);
        edited = rootView.findViewById(R.id.edited);

        text.setText(mNote.getText());
        author.setText("Author: " + mUser.getName());
        created.setText("Created: " + mNote.getDateCreated());
        edited.setText("Edited: " + mNote.getDateEdited());
        return rootView;
    }
}
