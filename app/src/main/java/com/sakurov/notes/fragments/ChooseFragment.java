package com.sakurov.notes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sakurov.notes.R;

public class ChooseFragment extends BaseFragment implements View.OnClickListener {

    public static ChooseFragment newInstance() {
        return new ChooseFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_choose, container, false);
        Button signInButton = rootView.findViewById(R.id.sign_in);
        Button signUpButton = rootView.findViewById(R.id.sign_up);

        setTitle(getString(R.string.title_notes));

        signInButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        int action;
        if (view.getId() == R.id.sign_in) {
            action = AuthFragment.IN;
        } else {
            action = AuthFragment.UP;
        }
        replaceFragment(AuthFragment.newInstance(action), true);
    }
}