package com.sakurov.notes.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sakurov.notes.R;
import com.sakurov.notes.entities.Note;

public class NoteFragment extends BaseFragment {

    static final int EDIT_NOTE_REQUEST = 700;

    private static final String NOTE = "note";

    private Note mNote;
    private TextView noteText, noteAuthor, noteDateCreated, noteDateEdited;

    public static NoteFragment newInstance(Note note) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(NOTE, note);

        NoteFragment fragment = new NoteFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    protected void readBundle(Bundle bundle) {
        if (bundle != null) {
            mNote = bundle.getParcelable(NOTE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FRAGMENT_TITLE = "Note";

        View rootView = inflater.inflate(R.layout.fragment_note, container, false);

        noteText = rootView.findViewById(R.id.text);
        noteAuthor = rootView.findViewById(R.id.author);
        noteDateCreated = rootView.findViewById(R.id.created);
        noteDateEdited = rootView.findViewById(R.id.edited);

        FloatingActionButton fabEditNote = rootView.findViewById(R.id.fab_edit);

        readBundle(getArguments());

        fabEditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNote != null) {
                    EditNoteDialogFragment fragment = EditNoteDialogFragment.newInstance(mNote);
                    fragment.setTargetFragment(NoteFragment.this, EDIT_NOTE_REQUEST);
                    fragment.show(getFragmentManager(), EditNoteDialogFragment.class.getSimpleName());
                }
            }
        });

        setTextViews();
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == EDIT_NOTE_REQUEST) {
                mNote = data.getParcelableExtra(NOTE);
                if (getActivity() != null)
                    setTextViews();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setTextViews() {
        noteText.setText(mNote.getText());
        noteAuthor.setText(String.format("%s%s", getString(R.string.author),
                getCurrentUserName(getActivity())));
        noteDateCreated.setText(String.format("%s%s", getString(R.string.created), mNote.getDateCreated()));
        noteDateEdited.setText(String.format("%s%s", getString(R.string.edited), mNote.getDateEdited()));
    }
}
