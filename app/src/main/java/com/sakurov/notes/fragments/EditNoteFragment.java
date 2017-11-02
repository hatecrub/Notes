package com.sakurov.notes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sakurov.notes.utils.PrefsManager;
import com.sakurov.notes.R;
import com.sakurov.notes.entities.Note;
import com.sakurov.notes.data.DataSource;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditNoteFragment extends BaseFragment {

    @BindView(R.id.text)
    EditText mEditNote;

    @BindView(R.id.in_layout)
    TextInputLayout textIn;

    private Note mNote;

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
        unbinder = ButterKnife.bind(this, rootView);

        readBundle(getArguments());

        String fragmentTitle;
        if (mNote != null) {
            fragmentTitle = getString(R.string.title_edit_note);
            setTitle(fragmentTitle);
            mEditNote.setText(mNote.getText());
            if (savedInstanceState == null) {
                showSoftKeyboard(mEditNote);
            }
        } else {
            fragmentTitle = getString(R.string.title_new_note);
            if (savedInstanceState == null) {
                mEditNote.requestFocus();
            }
        }

        textIn.setHint(fragmentTitle);

        return rootView;
    }

    private boolean isInputValid() {
        return !mEditNote.getText().toString().isEmpty();
    }

    @OnClick(R.id.fab_done)
    void onClick() {
        if (isInputValid()) {
            DataSource dataSource = new DataSource(getContext());
            if (mNote == null) {
                mNote = new Note(PrefsManager.getInstance().getCurrentUserID(),
                        mEditNote.getText().toString());
                mNote.setId(dataSource.addNote(mNote));
                getParentFragment().getFragmentManager().popBackStack();
                if (getParentFragment().getFragmentManager().getBackStackEntryCount() > 1) {
                    showLandContainer();
                }
            } else {
                mNote.setText(mEditNote.getText().toString());
                dataSource.updateNote(mNote);
                getFragmentManager().popBackStack();
                showLandContainer();
                updateLandContainer();
            }
        } else
            showToast(R.string.empty_note);
    }
}