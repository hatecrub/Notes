package com.sakurov.notes.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sakurov.notes.PrefsManager;
import com.sakurov.notes.R;
import com.sakurov.notes.entities.Note;
import com.sakurov.notes.entities.Notification;
import com.sakurov.notes.helpers.DBSource;

import java.util.Calendar;

public class EditNotificationFragment extends BaseFragment {
    private EditText mEditNotification;
    private Notification mNotification;
    private DBSource mSource;
    private FloatingActionButton mFabDone;

    long mTime;

    static final int DATE_DIALOG_ID = 1;
    static final int TIME_DIALOG_ID = 2;
    private TextView dateDisplay;
    private Button pickDate;
    private int year, month, day;
    private TextView timeDisplay;
    private Button pickTime;
    private int hours, min;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSource = new DBSource(getActivity());

        if (savedInstanceState == null) {
            mEditNotification.requestFocus();
        }

        mTime = System.currentTimeMillis();

        mFabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputValid()) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day,
                            hours, min, 0);
                    mTime = calendar.getTimeInMillis();

                    if (mNotification == null) {
                        mNotification = new Notification(PrefsManager.getInstance().getCurrentUserID(),
                                mEditNotification.getText().toString(), mTime);
                        mNotification.setId(mSource.addNotification(mNotification));
                        getParentFragment().getFragmentManager().popBackStack();
                    } else {
                        mNotification.setText(mEditNotification.getText().toString());
                        mNotification.setTimeInMillis(mTime);
                        mSource.updateNotification(mNotification);
                        getFragmentManager().popBackStack();
                    }
                } else
                    Toast.makeText(getActivity(), R.string.empty_note, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_notification, container, false);
        TextInputLayout textIn = rootView.findViewById(R.id.in_layout);

        mEditNotification = rootView.findViewById(R.id.text);
        mFabDone = rootView.findViewById(R.id.fab_done);

        dateDisplay = rootView.findViewById(R.id.TextView01);
        pickDate = rootView.findViewById(R.id.Button01);

        timeDisplay = rootView.findViewById(R.id.TextView02);
        pickTime = rootView.findViewById(R.id.Button02);

        readBundle(getArguments());

        String FRAGMENT_TITLE;
        if (mNotification != null) {
            FRAGMENT_TITLE = "Edit notification";
            setTitle(FRAGMENT_TITLE);
            mEditNotification.setText(mNotification.getText());

            if (savedInstanceState == null) {
                mEditNotification.requestFocus();
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                        .toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(mNotification.getTimeInMillis());
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);
                hours = cal.get(Calendar.HOUR);
                min = cal.get(Calendar.MINUTE);
                updateDate();
                updateTime();
            }
        } else {
            FRAGMENT_TITLE = "New notification";
            if (savedInstanceState == null) {
                mEditNotification.requestFocus();
                ((InputMethodManager) getParentFragment().getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                        .toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                final Calendar cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);
                hours = cal.get(Calendar.HOUR);
                min = cal.get(Calendar.MINUTE);
                updateDate();
                updateTime();
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

    private void updateTime() {
        timeDisplay.setText(new StringBuilder().append(hours).append(':')
                .append(min));
    }

    private void updateDate() {
        dateDisplay.setText(new StringBuilder().append(day).append('-')
                .append(month + 1).append('-').append(year));
    }

    private DatePickerDialog.OnDateSetListener dateListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int yr, int monthOfYear,
                                      int dayOfMonth) {
                    year = yr;
                    month = monthOfYear;
                    day = dayOfMonth;
                    updateDate();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day,
                            hours, min, 0);
                    mTime = calendar.getTimeInMillis();
                }
            };

    private TimePickerDialog.OnTimeSetListener timeListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    hours = hourOfDay;
                    min = minute;
                    updateTime();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day,
                            hours, min, 0);
                    mTime = calendar.getTimeInMillis();
                }
            };

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(getContext(), dateListener, year, month, day);
            case TIME_DIALOG_ID:
                return new TimePickerDialog(getContext(), timeListener, hours, min, false);
        }
        return null;
    }

    private boolean isInputValid() {
        return !mEditNotification.getText().toString().isEmpty()
                && !(mTime < System.currentTimeMillis());
    }
}
