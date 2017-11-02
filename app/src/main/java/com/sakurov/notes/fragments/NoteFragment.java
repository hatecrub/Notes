package com.sakurov.notes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sakurov.notes.R;
import com.sakurov.notes.data.DataSource;
import com.sakurov.notes.entities.Note;
import com.sakurov.notes.utils.Utils;
import com.sakurov.notes.utils.PrefsManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoteFragment extends BaseFragment {

    @BindView(R.id.text)
    TextView noteText;
    @BindView(R.id.author)
    TextView noteAuthor;
    @BindView(R.id.created)
    TextView noteDateCreated;
    @BindView(R.id.edited)
    TextView noteDateEdited;

    private Note mNote;

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
        View rootView = inflater.inflate(R.layout.fragment_note, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        readBundle(getArguments());
        setTitle(getString(R.string.title_note));
        updateDisplay();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        enableLogOutMenuItem(true);
    }

    @Override
    protected void update() {
        DataSource dataSource = new DataSource(getContext());
        if (mNote != null)
            mNote = dataSource.getNoteById(mNote.getId());

        updateDisplay();
    }

    private void updateDisplay() {
        noteText.setText(mNote.getText());
        noteAuthor.setText(
                String.format("%s%s", getString(R.string.author), PrefsManager.getInstance().getCurrentUserName()));
        noteDateCreated.setText(
                String.format("%s%s", getString(R.string.created), Utils.getDate(mNote.getDateCreated())));
        noteDateEdited.setText(
                String.format("%s%s", getString(R.string.edited), Utils.getDate(mNote.getDateEdited())));
    }

    @OnClick(R.id.fab_edit)
    public void onViewClicked() {
        if (mNote != null) {
            hideLandContainer();
            replaceFragment(EditNoteFragment.newInstance(mNote), true);
        }
    }
}