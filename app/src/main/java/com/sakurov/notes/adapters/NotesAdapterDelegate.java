package com.sakurov.notes.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.sakurov.notes.R;
import com.sakurov.notes.entities.Item;
import com.sakurov.notes.entities.Note;
import com.sakurov.notes.fragments.NoteFragment;
import com.sakurov.notes.fragments.NotesListFragment;
import com.sakurov.notes.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotesAdapterDelegate extends AdapterDelegate<List<Item>> {

    private NotesListFragment mFragment;
    private List<Item> items;

    NotesAdapterDelegate(NotesListFragment mFragment, List<Item> items) {
        this.mFragment = mFragment;
        this.items = items;
    }

    @Override
    protected boolean isForViewType(@NonNull List<Item> items, int position) {
        return items.get(position).getItemType() == Item.NOTE;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new NoteViewHolder(LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_note, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<Item> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        ((NoteViewHolder) holder).bind(items.get(position));
    }

    class NoteViewHolder extends BaseViewHolder {

        @BindView(R.id.text)
        TextView noteText;
        @BindView(R.id.created)
        TextView noteDateCreated;

        NoteViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void bind(Item item) {
            Note note = (Note) item;
            noteText.setText(note.getText());
            noteDateCreated.setText(Utils.getDate(note.getDateCreated()));
        }

        @OnClick(R.id.root)
        void onClick() {
            mFragment.showLandContainer();

            int container = mFragment.isLand() ? R.id.container_land : R.id.container;

            mFragment.getFragmentManager().popBackStack();

            mFragment.getFragmentManager().beginTransaction()
                    .replace(container,
                            NoteFragment.newInstance((Note) items.get(getAdapterPosition())))
                    .addToBackStack(NoteFragment.class.getSimpleName())
                    .commit();
        }
    }
}
