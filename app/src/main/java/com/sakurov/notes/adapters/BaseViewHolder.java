package com.sakurov.notes.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sakurov.notes.entities.Item;

abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    BaseViewHolder(View itemView) {
        super(itemView);
    }

    abstract void bind(Item item);
}
