package com.sakurov.notes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sakurov.notes.Communicator;
import com.sakurov.notes.R;

/**
 * Created by sakurov on 23.10.17.
 */

public class ChooseFragment extends Fragment implements View.OnClickListener {

    Button signIn, signUp;
    Communicator mCommunicator;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCommunicator = (Communicator) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_choose, container, false);

        signIn = rootView.findViewById(R.id.sign_in);
        signUp = rootView.findViewById(R.id.sign_up);

        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in: {
                mCommunicator.replaceFragment(new AuthFragment().setAction(AuthFragment.IN));
                break;
            }
            case R.id.sign_up: {
                mCommunicator.replaceFragment(new AuthFragment().setAction(AuthFragment.UP));
                break;
            }
        }
    }

}
