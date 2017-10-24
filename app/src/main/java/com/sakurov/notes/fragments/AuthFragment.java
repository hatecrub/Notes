package com.sakurov.notes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sakurov.notes.R;
import com.sakurov.notes.entities.User;
import com.sakurov.notes.helpers.DBSource;

public class AuthFragment extends BaseFragment {

    private final static String ACTION = "action";

    public final static int IN = 100;
    public final static int UP = 200;

    private EditText mUserName, mUserPassword;
    private DBSource mSource;
    private int action;

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
            action = bundle.getInt(ACTION);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSource = new DBSource(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_auth, container, false);
        mUserName = rootView.findViewById(R.id.name);
        mUserPassword = rootView.findViewById(R.id.password);
        Button actionButton = rootView.findViewById(R.id.action);

        readBundle(getArguments());

        switch (action) {
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
                    switch (action) {
                        case IN: {
                            if (mSource.checkUser(user)) {
                                user.setId(mSource.getUserId(user));
                                replaceFragment(NotesListFragment.newInstance(user), false);
                            } else {
                                Toast.makeText(getActivity(),
                                        "User do not exist or password is incorrect!",
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                            break;
                        }
                        case UP: {
                            user.setId(mSource.addUser(user));
                            replaceFragment(NotesListFragment.newInstance(user), false);
                            break;
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Fill all fields, please!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return rootView;
    }

    private boolean isInputValid() {
        return !(mUserName.getText().toString().isEmpty() || mUserPassword.getText().toString().isEmpty());
    }
}
