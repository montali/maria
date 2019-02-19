package com.example.simone.maria;


import android.support.v7.util.DiffUtil;

import java.util.ArrayList;

class TagDiffCallback extends DiffUtil.Callback {


    private final ArrayList<Tag> oldTags;
    private final ArrayList<Tag> newTags;

    TagDiffCallback(ArrayList<Tag> newTags, ArrayList<Tag> oldTags) {
        this.newTags = newTags;
        this.oldTags = oldTags;
    }

    @Override
    public int getOldListSize() {
        return oldTags.size();
    }

    @Override
    public int getNewListSize() {
        return newTags.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        if (newTags.get(newItemPosition).getId() != null && oldTags.get(oldItemPosition).getId() != null)
            return oldTags.get(oldItemPosition).getId().equals(newTags.get(newItemPosition).getId());
        else
            return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldTags.get(oldItemPosition).equals(newTags.get(newItemPosition));
    }

}
