package com.example.simone.maria;


import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.ArrayList;

public class TagDiffCallback extends DiffUtil.Callback {


    private ArrayList<Tag> oldTags;
    private ArrayList<Tag> newTags;

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
        return oldTags.get(oldItemPosition).getId().equals(newTags.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldTags.get(oldItemPosition).equals(newTags.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }

}
