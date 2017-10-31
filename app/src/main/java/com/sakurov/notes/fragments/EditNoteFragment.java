package com.sakurov.notes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.sakurov.notes.utils.PrefsManager;
import com.sakurov.notes.R;
import com.sakurov.notes.entities.Note;
import com.sakurov.notes.data.DataSource;

public class EditNoteFragment extends BaseFragment {

    private EditText mEditNote;
    FloatingActionButton mFabDone;
    private Note mNote;
    private DataSource mDataSource;

    public static EditNoteFragment newInstance() {
        return new EditNoteFragment();
    }

    public static EditNoteFragment newInstance(Note note) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(NOTE, note);
        EditNoteFragment fragment = new EditNoteFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_edit_note, container, false);
        TextInputLayout textIn = rootView.findViewById(R.id.in_layout);

        mEditNote = rootView.findViewById(R.id.text);
        mFabDone = rootView.findViewById(R.id.fab_done);

        readBundle(getArguments());

        String FRAGMENT_TITLE;
        if (mNote != null) {
            FRAGMENT_TITLE = getString(R.string.title_edit_note);
            setTitle(FRAGMENT_TITLE);
            mEditNote.setText(mNote.getText());
            if (savedInstanceState == null) {
                showSoftKeyboard(mEditNote);
            }
        } else {
            FRAGMENT_TITLE = getString(R.string.title_new_note);
            if (savedInstanceState == null) {
                mEditNote.requestFocus();
            }
        }

        textIn.setHint(FRAGMENT_TITLE);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDataSource = new DataSource(getActivity());

        mFabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputValid()) {
                    if (mNote == null) {
                        mNote = new Note(PrefsManager.getInstance().getCurrentUserID(),
                                mEditNote.getText().toString());
                        mNote.setId(mDataSource.addNote(mNote));
                        getParentFragment().getFragmentManager().popBackStack();
                    } else {
                        mNote.setText(mEditNote.getText().toString());
                        mDataSource.updateNote(mNote);
                        getFragmentManager().popBackStack();
                    }
                } else
                    Toast.makeText(getActivity(), R.string.empty_note, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isInputValid() {
        return !mEditNote.getText().toString().isEmpty();
    }
}
