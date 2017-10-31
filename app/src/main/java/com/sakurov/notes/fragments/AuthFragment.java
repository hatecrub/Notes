package com.sakurov.notes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.sakurov.notes.utils.PrefsManager;
import com.sakurov.notes.R;
import com.sakurov.notes.entities.User;
import com.sakurov.notes.data.DataSource;

public class AuthFragment extends BaseFragment {

    final static int IN = 100;
    final static int UP = 200;

    private final static String ACTION = "action";

    private DataSource mSource;
    private EditText mUserName, mUserPassword;
    private Button mActionButton;
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
        setTitle(getString(R.string.title_auth));
        super.onActivityCreated(savedInstanceState);
        mSource = new DataSource(getActivity());

        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputValid()) {
                    User user = new User(mUserName.getText().toString(),
                            mUserPassword.getText().toString());
                    switch (mAction) {
                        case IN: {
                            if (mSource.checkUserPass(user)) {
                                user.setId(mSource.getUserId(user));
                                setUserAndReplace(user);
                            } else {
                                showToast(R.string.user_dont_exist);
                            }
                            break;
                        }
                        case UP: {
                            if (!mSource.isUserExist(user)) {
                                user.setId(mSource.addUser(user));
                                setUserAndReplace(user);
                            } else {
                                showToast(R.string.user_exist);
                            }
                            break;
                        }
                    }
                } else {
                    showToast(R.string.field_empty);
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_auth, container, false);
        mUserName = rootView.findViewById(R.id.name);
        mUserPassword = rootView.findViewById(R.id.password);
        mRememberCheck = rootView.findViewById(R.id.remember);
        mActionButton = rootView.findViewById(R.id.action);

        readBundle(getArguments());

        if (savedInstanceState == null) {
            showSoftKeyboard(mUserName);
        }

        switch (mAction) {
            case IN: {
                mActionButton.setText(getString(R.string.sign_in));
                break;
            }
            case UP: {
                mActionButton.setText(getString(R.string.sign_up));
                break;
            }
        }

        return rootView;
    }

    private boolean isInputValid() {
        return !(mUserName.getText().toString().isEmpty() || mUserPassword.getText().toString().isEmpty());
    }

    private void setUserAndReplace(User user) {
        PrefsManager.getInstance().setCurrentUser(user, mRememberCheck.isChecked());
        addAsRootFragment(NotesListFragment.newInstance());
    }
}