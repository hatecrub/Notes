package com.sakurov.notes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.sakurov.notes.R;
import com.sakurov.notes.entities.Note;
import com.sakurov.notes.entities.User;
import com.sakurov.notes.helpers.DBSource;

public class EditNoteFragment extends BaseFragment {

    private EditText mEditNote;

    public static EditNoteFragment newInstance(User user) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(USER, user);

        EditNoteFragment fragment = new EditNoteFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    public static EditNoteFragment newInstance(Note note) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(NOTE, note);

        EditNoteFragment fragment = new EditNoteFragment();
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSource = new DBSource(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_note, container, false);

        mEditNote = rootView.findViewById(R.id.text);
        FloatingActionButton fabDone = rootView.findViewById(R.id.fab_done);

        readBundle(getArguments());

        if (mNote != null) {
            FRAGMENT_TITLE = "Edit note";
            mEditNote.setText(mNote.getText());
        } else
            FRAGMENT_TITLE = "New note";

        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputValid()) {
                    if (mNote == null) {
                        mNote = new Note(mUser.getId(), mEditNote.getText().toString());
                        mNote.setId(mSource.addNote(mNote));
                    } else {
                        mNote.setText(mEditNote.getText().toString());
                        mSource.updateNote(mNote);
                    }
                    getFragmentManager().popBackStack();
                } else
                    Toast.makeText(getActivity(), R.string.empty_note, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private boolean isInputValid() {
        return !mEditNote.getText().toString().isEmpty();
    }
}
