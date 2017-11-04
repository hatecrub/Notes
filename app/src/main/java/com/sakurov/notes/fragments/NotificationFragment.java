package com.sakurov.notes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sakurov.notes.R;
import com.sakurov.notes.data.DataSource;
import com.sakurov.notes.entities.Notification;
import com.sakurov.notes.utils.Utils;
import com.sakurov.notes.utils.PrefsManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationFragment extends BaseFragment {

    @BindView(R.id.text)
    TextView notificationText;
    @BindView(R.id.author)
    TextView notificationAuthor;
    @BindView(R.id.created)
    TextView notificationDateCreated;
    @BindView(R.id.edited)
    TextView notificationDateEdited;
    @BindView(R.id.time)
    TextView notificationTime;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        readBundle(getArguments());
        setTitle(getString(R.string.title_notification));
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
        if (mNotification != null)
            mNotification = dataSource.getNotificationById(mNotification.getId());

        updateDisplay();
    }

    private void updateDisplay() {
        notificationText.setText(mNotification.getText());
        notificationAuthor.setText(
                //в другом классе написал про такое
                getString(R.string.author, PrefsManager.getInstance().getCurrentUserName()));
        notificationDateCreated.setText(
                String.format("%s%s", getString(R.string.created), Utils.getDate(mNotification.getDateCreated())));
        notificationDateEdited.setText(
                String.format("%s%s", getString(R.string.edited), Utils.getDate(mNotification.getDateEdited())));
        notificationTime.setText(
                String.format("%s%s", getString(R.string.time), Utils.getDate(mNotification.getTimeInMillis())));
    }

    @OnClick(R.id.fab_edit)
    public void onViewClicked() {
        if (mNotification != null) {
            hideLandContainer();
            replaceFragment(EditNotificationFragment.newInstance(mNotification), true);
        }
    }
}