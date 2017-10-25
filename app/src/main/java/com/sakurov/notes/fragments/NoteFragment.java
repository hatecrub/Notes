package com.sakurov.notes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sakurov.notes.R;
import com.sakurov.notes.entities.Note;
import com.sakurov.notes.entities.User;

public class NoteFragment extends BaseFragment {

    public static NoteFragment newInstance(User user, Note note) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(USER, user);
        bundle.putParcelable(NOTE, note);

        NoteFragment fragment = new NoteFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected void readBundle(Bundle bundle) {
        if (bundle != null) {
            mNote = bundle.getParcelable(NOTE);
            mUser = bundle.getParcelable(USER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FRAGMENT_TITLE = "Note";

        View rootView = inflater.inflate(R.layout.fragment_note, container, false);

        TextView noteText = rootView.findViewById(R.id.text);
        TextView noteAuthor = rootView.findViewById(R.id.author);
        TextView noteDateCreated = rootView.findViewById(R.id.created);
        TextView noteDateEdited = rootView.findViewById(R.id.edited);
        FloatingActionButton fabEditNote = rootView.findViewById(R.id.fab_edit);

        readBundle(getArguments());

        fabEditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNote != null) {
                    replaceFragment(EditNoteFragment.newInstance(mNote), true);
                }
            }
        });

        noteText.setText(mNote.getText());
        noteAuthor.setText(String.format("%s%s", getString(R.string.author), mUser.getName()));
        noteDateCreated.setText(String.format("%s%s", getString(R.string.created), mNote.getDateCreated()));
        noteDateEdited.setText("Edited: " + mNote.getDateEdited());
        return rootView;
    }
}
