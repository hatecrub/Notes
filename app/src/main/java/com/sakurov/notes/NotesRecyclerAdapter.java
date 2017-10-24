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

public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.NoteViewHolder> {

    private List<Note> mNotes;
    private Communicator mCommunicator;
    private User mUser;

    public void setCommunicator(Communicator communicator) {
        this.mCommunicator = communicator;
    }

    public void updateList(List<Note> notes) {
        this.mNotes = notes;
    }

    public NotesRecyclerAdapter(List<Note> notes, User user) {
        updateList(notes);
        this.mUser = user;
    }

    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false));
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note note = mNotes.get(position);
        if (note.getText().length() > 60)
            holder.text.setText(note.getText().substring(0, 56) + "...");
        else
            holder.text.setText(note.getText());
        holder.created.setText("Created " + note.getDateCreated());
    }

    @Override
    public int getItemCount() {
        return mNotes != null ? mNotes.size() : 0;
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        LinearLayout root;
        TextView text;
        TextView created;

        NoteViewHolder(View view) {
            super(view);
            root = view.findViewById(R.id.root);
            text = view.findViewById(R.id.text);
            created = view.findViewById(R.id.created);

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCommunicator != null && mUser != null)
                        mCommunicator.replaceFragment(new NoteFragment().
                                setNote(mNotes.get(getAdapterPosition())).setUser(mUser), true);
                }
            });
        }
    }
}
