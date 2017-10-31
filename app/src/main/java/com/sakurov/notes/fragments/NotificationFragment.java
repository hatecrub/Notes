package com.sakurov.notes.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sakurov.notes.utils.PrefsManager;
import com.sakurov.notes.R;
import com.sakurov.notes.entities.Notification;

import java.util.Date;

public class NotificationFragment extends BaseFragment {

    private Notification mNotification;

    public static NotificationFragment newInstance(Notification notification) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(NOTIFICATION, notification);

        NotificationFragment fragment = new NotificationFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    protected void readBundle(Bundle bundle) {
        if (bundle != null) {
            mNotification = bundle.getParcelable(NOTIFICATION);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setTitle("Notification");
        super.onActivityCreated(savedInstanceState);
        enableLogOutMenuItem(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);

        TextView notificationText = rootView.findViewById(R.id.text);
        TextView notificationAuthor = rootView.findViewById(R.id.author);
        TextView notificationDateCreated = rootView.findViewById(R.id.created);
        TextView notificationDateEdited = rootView.findViewById(R.id.edited);
        TextView notificationTime = rootView.findViewById(R.id.time);

        FloatingActionButton fabEditNote = rootView.findViewById(R.id.fab_edit);

        readBundle(getArguments());

        fabEditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNotification != null) {
                    replaceFragment(EditNotificationFragment.newInstance(mNotification), true);
                }
            }
        });

        notificationText.setText(mNotification.getText());
        notificationAuthor.setText(String.format("%s%s", getString(R.string.author),
                PrefsManager.getInstance().getCurrentUserName()));
        notificationDateCreated.setText(String.format("%s%s", getString(R.string.created), mNotification.getDateCreated()));
        notificationDateEdited.setText(String.format("%s%s", getString(R.string.edited), mNotification.getDateEdited()));
        notificationTime.setText(String.format("%s%s", getString(R.string.time), new Date(mNotification.getTimeInMillis()).toString()));
        return rootView;
    }
}
