package com.sakurov.notes.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.sakurov.notes.PrefsManager;
import com.sakurov.notes.R;
import com.sakurov.notes.entities.User;
import com.sakurov.notes.helpers.DBSource;

public class AuthFragment extends BaseFragment {

    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_PASS = "user_pass";
    public static final String IS_USER_REMEMBERED = "is_user_remembered";

    final static int IN = 100;
    final static int UP = 200;

    private final static String ACTION = "action";

    private DBSource mSource;
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

    private void readBundle(Bundle bundle) {
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

        if (savedInstanceState == null) {
            mUserName.requestFocus();
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }

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

                    switch (mAction) {
                        case IN: {
                            if (mSource.checkUser(user)) {
                                user.setId(mSource.getUserId(user));
                                PrefsManager.getInstance().setCurrentUser(user, mRememberCheck.isChecked());
                                addAsRootFragment(NotesListFragment.newInstance());
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
                            PrefsManager.getInstance().setCurrentUser(user, mRememberCheck.isChecked());
                            addAsRootFragment(NotesListFragment.newInstance());
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
