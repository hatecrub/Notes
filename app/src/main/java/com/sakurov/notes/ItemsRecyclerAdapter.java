package com.sakurov.notes;

import android.content.Context;
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

import com.sakurov.notes.entities.Item;
import com.sakurov.notes.entities.Note;
import com.sakurov.notes.entities.Notification;
import com.sakurov.notes.fragments.NoteFragment;
import com.sakurov.notes.fragments.NotificationFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ItemsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Item> mItems = new ArrayList<>();
    private Context mContext;
    private FragmentManager mFragmentManager;

    public ItemsRecyclerAdapter(Context context, FragmentManager fragmentManager) {
        mContext = context;
        mFragmentManager = fragmentManager;
    }

    public void updateList(List<Item> items) {
        this.mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
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
        Item item = mItems.get(position);
        switch (holder.getItemViewType()) {
            case 1: {
                NoteViewHolder noteViewHolder = (NoteViewHolder) holder;
                noteViewHolder.noteText.setText(item.getItemText());
                noteViewHolder.noteDateCreated.setText(item.getItemDateCreated());
                break;
            }
            default: {
                NotificationViewHolder notificationViewHolder = (NotificationViewHolder) holder;
                notificationViewHolder.notificationText.setText(item.getItemText());
                notificationViewHolder.notificationDateCreated.setText(item.getItemDateCreated());
                notificationViewHolder.notificationTime.setText(new Date(item.getItemTimeMillis()).toString());
                if (holder.getItemViewType() == Item.NOTIFICATION_OUTDATED) {
                    notificationViewHolder.notificationTime.
                            setPaintFlags(notificationViewHolder.notificationTime.getPaintFlags() |
                                    Paint.STRIKE_THRU_TEXT_FLAG);
                    notificationViewHolder.notificationIcon.setImageResource(R.drawable.ic_notifications_black_48dp);
                }
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getItemType();
    }

    private class NoteViewHolder extends RecyclerView.ViewHolder {

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
    }

    private class NotificationViewHolder extends RecyclerView.ViewHolder {

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
}
