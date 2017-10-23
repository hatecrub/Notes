package com.sakurov.notes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sakurov.notes.entities.Note;
import com.sakurov.notes.entities.User;
import com.sakurov.notes.fragments.NoteFragment;

import java.util.List;

/**
 * Created by sakurov on 23.10.17.
 */

public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.NoteViewHolder> {

    private List<Note> mNotes;
    private Communicator mCommunicator;
    private User mUser;

    public void updateCommunicator(Communicator communicator) {
        this.mCommunicator = communicator;
    }

    public void updateList(List<Note> notes) {
        this.mNotes = notes;
    }

    public NotesRecyclerAdapter(List<Note> notes, User user) {
        this.mNotes = notes;
        this.mUser = user;
    }

    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false));
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note note = mNotes.get(position);
        holder.text.setText(note.getText());
        holder.createdBy.setText("Created " + note.getDateCreated());
    }

    @Override
    public int getItemCount() {
        return mNotes != null ? mNotes.size() : 0;
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        LinearLayout root;
        TextView text;
        TextView createdBy;

        NoteViewHolder(View view) {
            super(view);
            root = view.findViewById(R.id.root);
            text = view.findViewById(R.id.text);
            createdBy = view.findViewById(R.id.created);

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCommunicator != null && mUser != null)
                        mCommunicator.replaceFragment(new NoteFragment().
                                setNote(mNotes.get(getAdapterPosition())).setUser(mUser));
                }
            });
        }
    }
}
