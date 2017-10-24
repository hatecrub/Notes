package com.sakurov.notes.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sakurov.notes.Communicator;
import com.sakurov.notes.R;
import com.sakurov.notes.entities.User;
import com.sakurov.notes.helpers.DBSource;

public class AuthFragment extends Fragment {

    public final static int IN = 100;
    public final static int UP = 200;

    private EditText userName, userPassword;
    private Button actionButton;

    private Communicator mCommunicator;
    private DBSource mSource;

    private int action;

    public AuthFragment setAction(int action) {
        this.action = action;
        return this;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCommunicator = (Communicator) getActivity();
        mSource = new DBSource(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_auth, container, false);

        userName = rootView.findViewById(R.id.name);
        userPassword = rootView.findViewById(R.id.password);
        actionButton = rootView.findViewById(R.id.action);

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
                    User user = new User(userName.getText().toString(),
                            userPassword.getText().toString());
                    switch (action) {
                        case IN: {
                            if (mSource.checkUser(user)) {
                                user.setId(mSource.getUserId(user));
                                mCommunicator.setUser(user);
                                mCommunicator.replaceFragment(new NotesListFragment().setUser(user), false);
                            } else {
                                Toast.makeText(getActivity(), "User do not exist or password incorrect!", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                        case UP: {
                            user.setId(mSource.addUser(user));
                            mCommunicator.setUser(user);
                            mCommunicator.replaceFragment(new NotesListFragment().setUser(user), false);
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
        return !(userName.getText().toString().isEmpty() || userPassword.getText().toString().isEmpty());
    }
}
