package com.sakurov.notes.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.sakurov.notes.R;
import com.sakurov.notes.entities.User;
import com.sakurov.notes.helpers.DBSource;

public class AuthFragment extends BaseFragment {

    final static int IN = 100;
    final static int UP = 200;

    private final static String ACTION = "action";

    public static final String USER_NAME = "user_name";
    public static final String USER_PASS = "user_pass";
    public static final String IS_USER = "is_user";

    private EditText mUserName, mUserPassword;
    private CheckBox mRememberCheck;

    private int mAction;

    public static AuthFragment newInstance(int action) {

        Bundle bundle = new Bundle();
        bundle.putInt(ACTION, action);

        AuthFragment fragment = new AuthFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected void readBundle(Bundle bundle) {
        if (bundle != null) {
            mAction = bundle.getInt(ACTION);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        FRAGMENT_TITLE = "Authentication";
        super.onActivityCreated(savedInstanceState);
        mSource = new DBSource(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_auth, container, false);
        mUserName = rootView.findViewById(R.id.name);
        mUserPassword = rootView.findViewById(R.id.password);
        mRememberCheck = rootView.findViewById(R.id.remember);
        Button actionButton = rootView.findViewById(R.id.action);

        readBundle(getArguments());

        switch (mAction) {
            case IN: {
                actionButton.setText(getString(R.string.sign_in));
                break;
            }
            case UP: {
                actionButton.setText(getString(R.string.sign_up));
                break;
            }
        }

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputValid()) {
                    User user = new User(mUserName.getText().toString(),
                            mUserPassword.getText().toString());

                    SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);

                    if (mRememberCheck.isChecked()) {
                        preferences.edit()
                                .putString(USER_NAME, user.getName())
                                .putString(USER_PASS, user.getPassword())
                                .apply();
                    } else {
                        preferences.edit().clear().apply();
                    }

                    preferences.edit().putBoolean(IS_USER, mRememberCheck.isChecked()).apply();

                    switch (mAction) {
                        case IN: {
                            if (mSource.checkUser(user)) {
                                user.setId(mSource.getUserId(user));
                                addAsRootFragment(NotesListFragment.newInstance(user));
                            } else {
                                Toast.makeText(getActivity(),
                                        R.string.user_dont_exist,
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                            break;
                        }
                        case UP: {
                            user.setId(mSource.addUser(user));
                            addAsRootFragment(NotesListFragment.newInstance(user));
                            break;
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.field_empty, Toast.LENGTH_SHORT).show();
                }

            }
        });
        return rootView;
    }

    private boolean isInputValid() {
        return !(mUserName.getText().toString().isEmpty() || mUserPassword.getText().toString().isEmpty());
    }
}
