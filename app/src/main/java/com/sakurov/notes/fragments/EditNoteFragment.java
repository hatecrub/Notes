package com.sakurov.notes.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.sakurov.notes.PrefsManager;
import com.sakurov.notes.R;
import com.sakurov.notes.entities.Note;
import com.sakurov.notes.helpers.DBSource;

public class EditNoteFragment extends BaseFragment {

    private EditText mEditNote;
    FloatingActionButton mFabDone;
    private Note mNote;
    private DBSource mSource;

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSource = new DBSource(getActivity());

        mFabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputValid()) {
                    if (mNote == null) {
                        mNote = new Note(PrefsManager.getInstance().getCurrentUserID(),
                                mEditNote.getText().toString());
                        mNote.setId(mSource.addNote(mNote));
                        getParentFragment().getFragmentManager().popBackStack();
                    } else {
                        mNote.setText(mEditNote.getText().toString());
                        mSource.updateNote(mNote);
                        getFragmentManager().popBackStack();
                    }
                } else
                    Toast.makeText(getActivity(), R.string.empty_note, Toast.LENGTH_SHORT).show();
            }
        });
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
            FRAGMENT_TITLE = "Edit note";
            setTitle(FRAGMENT_TITLE);
            mEditNote.setText(mNote.getText());

            if (savedInstanceState == null) {
                Log.d(FRAGMENT_TITLE,"open keyboard");
                mEditNote.requestFocus();
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                        .toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }

        } else {
            FRAGMENT_TITLE = "New note";

            if (savedInstanceState == null) {
                Log.d(FRAGMENT_TITLE,"open keyboard");
                mEditNote.requestFocus();
                ((InputMethodManager) getParentFragment().getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                        .toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }

        textIn.setHint(FRAGMENT_TITLE);

        return rootView;
    }

    private boolean isInputValid() {
        return !mEditNote.getText().toString().isEmpty();
    }
}
