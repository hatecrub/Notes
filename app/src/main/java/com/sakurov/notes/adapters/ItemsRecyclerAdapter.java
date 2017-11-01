package com.sakurov.notes.adapters;

import android.graphics.Paint;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sakurov.notes.R;
import com.sakurov.notes.entities.Item;
import com.sakurov.notes.entities.Note;
import com.sakurov.notes.entities.Notification;
import com.sakurov.notes.fragments.NoteFragment;
import com.sakurov.notes.fragments.NotificationFragment;

import java.util.ArrayList;
import java.util.List;

public class ItemsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Item> mItems = new ArrayList<>();
    private FragmentManager mFragmentManager;

    public ItemsRecyclerAdapter(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    public void updateList(List<Item> items) {
        this.mItems = items;
        notifyDataSetChanged();
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
        int itemType = mItems.get(position).getItemType();

        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (itemType == Item.NOTE)
            transaction.replace(R.id.container,
                    NoteFragment.newInstance((Note) mItems.get(position)));
        else
            transaction.replace(R.id.container,
                    NotificationFragment.newInstance((Notification) mItems.get(position)));

        transaction.addToBackStack(NoteFragment.class.getSimpleName())
                .commit();
    }

    private class NoteViewHolder extends BaseViewHolder {

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
                    replaceFragment(getAdapterPosition());
                }
            });
        }

        @Override
        public void bind(Item item) {
            Note note = (Note) item;
            noteText.setText(note.getText());
            noteDateCreated.setText(note.getDateCreated());
        }
    }

    private class NotificationViewHolder extends BaseViewHolder {

        LinearLayout rootView;
        TextView notificationText;
        TextView notificationDateCreated;
        TextView notificationTime;
        ImageView notificationIcon;

        NotificationViewHolder(View view) {
            super(view);
            rootView = view.findViewById(R.id.root);
            notificationText = view.findViewById(R.id.text);
            notificationDateCreated = view.findViewById(R.id.created);
            notificationTime = view.findViewById(R.id.time);
            notificationIcon = view.findViewById(R.id.ic_image);

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    replaceFragment(getAdapterPosition());
                }
            });
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
    }

    abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        BaseViewHolder(View itemView) {
            super(itemView);
        }

        abstract void bind(Item item);
    }
}