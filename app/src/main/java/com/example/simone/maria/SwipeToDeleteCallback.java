package com.example.simone.maria;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
    private final RicettaAdapter mAdapter;

    SwipeToDeleteCallback(RicettaAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.mAdapter = adapter;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (viewHolder.getAdapterPosition() != RecyclerView.NO_POSITION) {
            int position = viewHolder.getAdapterPosition();
            mAdapter.deleteItem(position);
        }
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }


}