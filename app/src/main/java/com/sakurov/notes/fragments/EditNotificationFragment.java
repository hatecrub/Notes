package com.sakurov.notes.fragments;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sakurov.notes.NotificationReceiver;
import com.sakurov.notes.utils.PrefsManager;
import com.sakurov.notes.R;
import com.sakurov.notes.entities.Notification;
import com.sakurov.notes.data.DataSource;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditNotificationFragment extends BaseFragment {

    private final static String TIME = "time";

    private EditText mEditNotification;
    private Notification mNotification;
    private DataSource mSource;
    private FloatingActionButton mFabDone;

    static final int DATE_DIALOG_ID = 1;
    static final int TIME_DIALOG_ID = 2;
    private TextView mDateAndTimeDisplay;

    private Calendar mCalendar = Calendar.getInstance();

    public static EditNotificationFragment newInstance() {
        return new EditNotificationFragment();
    }

    public static EditNotificationFragment newInstance(Notification notification) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(NOTIFICATION, notification);
        EditNotificationFragment fragment = new EditNotificationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    protected void readBundle(Bundle bundle) {
        if (bundle != null) {
            mNotification = bundle.getParcelable(NOTIFICATION);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mCalendar.setTimeInMillis(savedInstanceState.getLong(TIME));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_notification, container, false);
        TextInputLayout textIn = rootView.findViewById(R.id.in_layout);

        mEditNotification = rootView.findViewById(R.id.text);
        mFabDone = rootView.findViewById(R.id.fab_done);

        mDateAndTimeDisplay = rootView.findViewById(R.id.date_and_time);
        Button pickDate = rootView.findViewById(R.id.pick_date);
        Button pickTime = rootView.findViewById(R.id.pick_time);

        readBundle(getArguments());

        String FRAGMENT_TITLE;
        if (mNotification != null) {
            FRAGMENT_TITLE = getString(R.string.title_edit_notification);
            setTitle(FRAGMENT_TITLE);
            mEditNotification.setText(mNotification.getText());
            if (savedInstanceState == null) {
                mCalendar.setTimeInMillis(mNotification.getTimeInMillis());
                updateDisplay();
                showSoftKeyboard(mEditNotification);
            }
        } else {
            FRAGMENT_TITLE = getString(R.string.title_new_notification);
            if (savedInstanceState == null) {
                updateDisplay();
                mEditNotification.requestFocus();
            }
        }

        textIn.setHint(FRAGMENT_TITLE);

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateDialog(DATE_DIALOG_ID).show();
            }
        });

        pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateDialog(TIME_DIALOG_ID).show();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSource = new DataSource(getActivity());

        mFabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputValid()) {
                    long time = mCalendar.getTimeInMillis();

                    if (mNotification == null) {
                        mNotification = new Notification(PrefsManager.getInstance().getCurrentUserID(),
                                mEditNotification.getText().toString(), time);
                        mNotification.setId(mSource.addNotification(mNotification));
                        addAlarm(mNotification, getParentFragment().getActivity().getApplicationContext());
                        getParentFragment().getFragmentManager().popBackStack();
                    } else {
                        mNotification.setText(mEditNotification.getText().toString());
                        mNotification.setTimeInMillis(time);
                        mSource.updateNotification(mNotification);
                        addAlarm(mNotification, getActivity().getApplicationContext());
                        getFragmentManager().popBackStack();
                    }
                } else
                    showToast(R.string.empty_note);
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private void updateDisplay() {
        mDateAndTimeDisplay.setText(
                new SimpleDateFormat("dd.MM.yyyy HH:mm")
                        .format(mCalendar.getTime()));
    }

    private DatePickerDialog.OnDateSetListener dateListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int yr, int monthOfYear,
                                      int dayOfMonth) {
                    mCalendar.set(Calendar.YEAR, yr);
                    mCalendar.set(Calendar.MONTH, monthOfYear);
                    mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDisplay();
                }
            };

    private TimePickerDialog.OnTimeSetListener timeListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    mCalendar.set(Calendar.MINUTE, minute);
                    updateDisplay();
                }
            };

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(getContext(),
                        dateListener,
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH));
            case TIME_DIALOG_ID:
                return new TimePickerDialog(getContext(),
                        timeListener,
                        mCalendar.get(Calendar.HOUR_OF_DAY),
                        mCalendar.get(Calendar.MINUTE),
                        true);
        }
        return null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(TIME, mCalendar.getTimeInMillis());
    }

    private boolean isInputValid() {
        return !mEditNotification.getText().toString().isEmpty()
                && !(mCalendar.getTimeInMillis() < System.currentTimeMillis());
    }

    private void addAlarm(Notification notification, Context context) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(NOTIFICATION, notification);

        Intent notificationIntent = new Intent(context, NotificationReceiver.class);
        notificationIntent.putExtra(NOTIFICATION, bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, notification.getTimeInMillis(), pendingIntent);
        }
    }
}