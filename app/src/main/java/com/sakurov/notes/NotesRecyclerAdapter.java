package com.sakurov.notes;

import android.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sakurov.notes.entities.Note;
import com.sakurov.notes.entities.User;
import com.sakurov.notes.fragments.NoteFragment;

import java.util.List;

public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.NoteViewHolder> {

    private List<Note> mNotes;
    private FragmentManager mFragmentManager;
    private User mUser;

    public void updateList(List<Note> notes) {
        this.mNotes = notes;
    }

    public NotesRecyclerAdapter(FragmentManager fragmentManager, User user) {
        this.mFragmentManager = fragmentManager;
        this.mUser = user;
    }

    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_note, parent, false));
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        final Note note = mNotes.get(position);
        holder.noteText.setText(note.getText());
        holder.noteDateCreated.setText("Created: " + note.getDateCreated());
    }

    @Override
    public int getItemCount() {
        return mNotes != null ? mNotes.size() : 0;
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        LinearLayout rootView;
        TextView noteText;
        TextView noteDateCreated;

        NoteViewHolder(View view) {
            super(view);
            rootView = view.findViewById(R.id.root);
            noteText = view.findViewById(R.id.text);
            noteDateCreated = view.findViewById(R.id.created);

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Adapter", "BackStackCountBefore: " + mFragmentManager.getBackStackEntryCount());
                    mFragmentManager
                            .beginTransaction()
                            .replace(R.id.container,
                                    NoteFragment.newInstance(mUser, mNotes.get(getAdapterPosition())))
                            .addToBackStack(NoteFragment.class.getSimpleName())
                            .commit();
                    Log.d("Adapter", "BackStackCountAfter: " + mFragmentManager.getBackStackEntryCount());
                }
            });
        }
    }
}
