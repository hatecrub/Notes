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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthFragment extends BaseFragment {

    final static int IN = 100;
    final static int UP = 200;

    private final static String ACTION = "action";

    @BindView(R.id.name)
    EditText mUserName;
    @BindView(R.id.password)
    EditText mUserPassword;
    @BindView(R.id.action)
    Button mActionButton;
    @BindView(R.id.remember)
    CheckBox mRememberCheck;

    public static AuthFragment newInstance(int action) {

        Bundle bundle = new Bundle();
        bundle.putInt(ACTION, action);

        AuthFragment fragment = new AuthFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setTitle(getString(R.string.title_auth));
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_auth, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        if (savedInstanceState == null) {
            showSoftKeyboard(mUserName);
        }

        switch (getArguments().getInt(ACTION)) {
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

    @OnClick(R.id.action)
    void onActionClick() {
        if (isInputValid()) {
            DataSource dataSource = new DataSource(getContext());
            User user = new User(mUserName.getText().toString(),
                    mUserPassword.getText().toString());
            switch (getArguments().getInt(ACTION)) {
                case IN: {
                    if (dataSource.checkUserPass(user)) {
                        user.setId(dataSource.getUserId(user));
                        setUserAndReplace(user);
                    } else {
                        showToast(R.string.user_do_not_exist);
                    }
                    break;
                }
                case UP: {
                    if (!dataSource.isUserExist(user)) {
                        user.setId(dataSource.addUser(user));
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
}