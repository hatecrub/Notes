package com.sakurov.notes.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager;
import com.sakurov.notes.entities.Item;
import com.sakurov.notes.fragments.NotesListFragment;

import java.util.List;

public class ItemRecyclerAdapter extends RecyclerView.Adapter {

    private AdapterDelegatesManager<List<Item>> delegatesManager;
    private List<Item> items;

    public ItemRecyclerAdapter(List<Item> items, NotesListFragment fragment) {
        this.items = items;
        notifyDataSetChanged();
        // Delegates
        delegatesManager = new AdapterDelegatesManager<>();
        delegatesManager.addDelegate(new NotesAdapterDelegate(fragment, items));
        delegatesManager.addDelegate(new NotificationsAdapterDelegate(fragment, items));
    }

    @Override
    public int getItemViewType(int position) {
        return delegatesManager.getItemViewType(items, position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return delegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        delegatesManager.onBindViewHolder(items, position, holder);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        delegatesManager.onBindViewHolder(items, position, holder, payloads);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}