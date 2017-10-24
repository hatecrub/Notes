package com.sakurov.notes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.sakurov.notes.entities.User;
import com.sakurov.notes.fragments.ChooseFragment;
import com.sakurov.notes.fragments.EditNoteFragment;
import com.sakurov.notes.fragments.NoteFragment;
import com.sakurov.notes.fragments.NotesListFragment;

public class MainActivity extends AppCompatActivity implements Communicator {

    public final static String NOTES_LIST_FRAGMENT = NotesListFragment.class.getSimpleName();
    public final static String NOTE_FRAGMENT = NoteFragment.class.getSimpleName();
    public final static String EDIT_NOTE_FRAGMENT = EditNoteFragment.class.getSimpleName();

    FragmentManager mFragmentManager;

    private User mUser;

    private NotesListFragment notesListFragment;
    private NoteFragment noteFragment;
    private EditNoteFragment editNoteFragment;

    long mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getFragmentManager();

        if (mUser == null) {
//            ChooseFragment chooseFragment = new ChooseFragment();
//            mFragmentManager.
//                    beginTransaction().
//                    add(R.id.container, chooseFragment).
//                    addToBackStack(chooseFragment.getClass().getSimpleName()).
//                    commit();
            replaceFragment(new ChooseFragment(), false);
        } else {
            if (notesListFragment == null) {
                notesListFragment = new NotesListFragment().setUser(mUser);
            }
            replaceFragment(notesListFragment, false);
        }
    }

    @Override
    public void replaceFragment(Fragment fragment, boolean addToBack) {
        Log.d("replace fragment", fragment.getClass().getSimpleName() + mFragmentManager.getBackStackEntryCount());
        String backStateName = fragment.getClass().getSimpleName();

        boolean fragmentPopped = mFragmentManager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped) {
            if (addToBack) {
                mFragmentManager.
                        beginTransaction().
                        replace(R.id.container, fragment).
                        addToBackStack(fragment.getClass().getSimpleName()).
                        commit();
            } else {
                mFragmentManager.
                        beginTransaction().
                        replace(R.id.container, fragment).
                        commit();
            }
        }
    }

    @Override
    public void replaceFragment(String fragmentName) {
        if (NOTES_LIST_FRAGMENT.equals(fragmentName)) {
            if (notesListFragment == null) {
                notesListFragment = new NotesListFragment();
                if (mUser != null)
                    notesListFragment.setUser(mUser);
            }
            mFragmentManager.
                    beginTransaction().
                    replace(R.id.container, notesListFragment).
                    addToBackStack(NOTES_LIST_FRAGMENT).
                    commit();
        } else if (NOTE_FRAGMENT.equals(fragmentName)) {

        } else if (EDIT_NOTE_FRAGMENT.equals(fragmentName)) {

        }
    }

    //    @Override
//    public void onBackPressed() {
//        if (mFragmentManager.getBackStackEntryCount() > 0) {
//            mFragmentManager.popBackStack();
//        } else {
//            if (System.currentTimeMillis() - mTimer > 1000) {
//                mTimer = System.currentTimeMillis();
//                Toast.makeText(this, "Click again to exit", Toast.LENGTH_SHORT).show();
//            } else
//                super.onBackPressed();
//        }
//    }

    @Override
    public void clearBackStack() {
        FragmentManager manager = getFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out: {
                mUser = null;
                clearBackStack();
                replaceFragment(new ChooseFragment(), false);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setUser(User user) {
        this.mUser = user;
    }
}
