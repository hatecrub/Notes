package com.sakurov.notes.adapters;

import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sakurov.notes.R;
import com.sakurov.notes.entities.Item;
import com.sakurov.notes.entities.Note;
import com.sakurov.notes.entities.Notification;
import com.sakurov.notes.fragments.NoteFragment;
import com.sakurov.notes.fragments.NotesListFragment;
import com.sakurov.notes.fragments.NotificationFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ItemsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Item> mItems = new ArrayList<>();
    private FragmentManager mFragmentManager;
    private boolean isLand = false;
    private NotesListFragment fragment;

    public ItemsRecyclerAdapter(FragmentManager fragmentManager,
                                NotesListFragment fragment,
                                List<Item> items) {
        mFragmentManager = fragmentManager;
        this.fragment = fragment;
        this.mItems = items;
        notifyDataSetChanged();
    }

    public void updateFlag(boolean isLand) {
        this.isLand = isLand;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case Item.NOTE:
                return new NoteViewHolder(LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.item_note, parent, false));
            default:
                return new NotificationViewHolder(LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.item_notification, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder) holder).bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getItemType();
    }

    private void replaceFragment(int position) {
        fragment.showLandContainer();

        int container = isLand ? R.id.container_land : R.id.container;

        Fragment fragment = (mItems.get(position).getItemType() == Item.NOTE) ?
                NoteFragment.newInstance((Note) mItems.get(position)) :
                NotificationFragment.newInstance((Notification) mItems.get(position));

        mFragmentManager.popBackStack();

        mFragmentManager.beginTransaction()
                .replace(container, fragment)
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
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
            noteDateCreated.setText(note.getDateCreated());
        }

        @OnClick(R.id.root)
        void onClick() {
            replaceFragment(getAdapterPosition());
        }
    }

    class NotificationViewHolder extends BaseViewHolder {

        @BindView(R.id.text)
        TextView notificationText;
        @BindView(R.id.created)
        TextView notificationDateCreated;
        @BindView(R.id.time)
        TextView notificationTime;
        @BindView(R.id.ic_image)
        ImageView notificationIcon;

        NotificationViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void bind(Item item) {
            Notification notification = (Notification) item;
            notificationText.setText(notification.getText());
            notificationDateCreated.setText(notification.getDateCreated());
            notificationTime.setText(notification.getTime());
            if (this.getItemViewType() == Item.NOTIFICATION_OUTDATED) {
                notificationTime.
                        setPaintFlags(notificationTime.getPaintFlags() |
                                Paint.STRIKE_THRU_TEXT_FLAG);
                notificationIcon.setImageResource(R.drawable.ic_notifications_black_48dp);
            }
        }

        @OnClick(R.id.root)
        void onClick() {
            replaceFragment(getAdapterPosition());
        }
    }

    abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        BaseViewHolder(View itemView) {
            super(itemView);
        }

        abstract void bind(Item item);
    }
}