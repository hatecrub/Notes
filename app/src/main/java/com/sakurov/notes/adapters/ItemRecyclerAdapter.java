package com.sakurov.notes.adapters;

import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter;
import com.sakurov.notes.entities.Item;
import com.sakurov.notes.fragments.NotesListFragment;

import java.util.ArrayList;
import java.util.List;

public class ItemRecyclerAdapter extends ListDelegationAdapter<List<Item>> {

    public ItemRecyclerAdapter(NotesListFragment fragment) {
        this.items = new ArrayList<>();
        delegatesManager.addDelegate(new NotesAdapterDelegate(fragment, items));
        delegatesManager.addDelegate(new NotificationsAdapterDelegate(fragment, items));
    }

    public void setData(List<Item> data) {
        items.clear();
        items.addAll(data);
        notifyDataSetChanged();
    }
}