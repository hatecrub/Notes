package com.sakurov.notes.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
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

public class EditNotificationFragment extends BaseFragment {
    private EditText mEditNote;
    private Note mNote;
    private DBSource mSource;

    public static EditNotificationFragment newInstance() {
        return new EditNotificationFragment();
    }

    public static EditNotificationFragment newInstance(Note note) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(NOTE, note);

        EditNotificationFragment fragment = new EditNotificationFragment();
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
        setActionBarTitle(FRAGMENT_TITLE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_note, container, false);
        TextInputLayout textIn = rootView.findViewById(R.id.in_layout);

        mEditNote = rootView.findViewById(R.id.text);
        FloatingActionButton fabDone = rootView.findViewById(R.id.fab_done);

        readBundle(getArguments());

        String FRAGMENT_TITLE;
        if (mNote != null) {
            FRAGMENT_TITLE = "Edit note";
            textIn.setHint(FRAGMENT_TITLE);
            mEditNote.setText(mNote.getText());
        } else {
            FRAGMENT_TITLE = "New note";
            textIn.setHint(FRAGMENT_TITLE);
        }

        if (savedInstanceState == null) {
            mEditNote.requestFocus();
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }

        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputValid()) {
                    if (mNote == null) {
                        mNote = new Note(PrefsManager.getInstance().getCurrentUserID(),
                                mEditNote.getText().toString());
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
