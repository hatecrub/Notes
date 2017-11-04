package com.sakurov.notes.adapters;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.sakurov.notes.R;
import com.sakurov.notes.entities.Item;
import com.sakurov.notes.entities.Notification;
import com.sakurov.notes.fragments.NotesListFragment;
import com.sakurov.notes.fragments.NotificationFragment;
import com.sakurov.notes.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NotificationsAdapterDelegate extends AdapterDelegate<List<Item>> {

    private NotesListFragment mFragment;

    NotificationsAdapterDelegate(NotesListFragment mFragment, List<Item> items) {
        this.mFragment = mFragment;
    }

    @Override
    protected boolean isForViewType(@NonNull List<Item> items, int position) {
        return items.get(position).getItemType() == Item.NOTIFICATION ||
                items.get(position).getItemType() == Item.NOTIFICATION_OUTDATED;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new NotificationViewHolder(LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_notification, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<Item> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        ((NotificationViewHolder) holder).bind(items.get(position));
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

        //Тут я просто тестил, не сломается ли что-то. не призыв делать так всегда.
        //Кажется хранить ссылку на список - это не затратно.
        private Notification item;

        NotificationViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void bind(Item item) {
            Notification notification = (Notification) item;
            this.item = notification;
            notificationText.setText(notification.getText());
            notificationDateCreated.setText(Utils.getDate(notification.getDateCreated()));
            notificationTime.setText(Utils.getDate(notification.getTimeInMillis()));
            if (item.getItemType() == Item.NOTIFICATION_OUTDATED) {
                notificationTime.
                        setPaintFlags(notificationTime.getPaintFlags() |
                                Paint.STRIKE_THRU_TEXT_FLAG);
                notificationIcon.setImageResource(R.drawable.ic_notifications_black_48dp);
            }
        }

        @OnClick(R.id.root)
        void onClick() {
            mFragment.showLandContainer();

            int container = mFragment.isLand() ? R.id.container_land : R.id.container;

            mFragment.getFragmentManager().popBackStack();

            mFragment.getFragmentManager().beginTransaction()
                    .replace(container,
                            NotificationFragment.newInstance(item))
                    .addToBackStack(NotificationFragment.class.getSimpleName())
                    .commit();
        }
    }
}
