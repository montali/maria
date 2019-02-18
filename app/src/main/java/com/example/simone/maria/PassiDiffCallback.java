package com.example.simone.maria;


import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.ArrayList;

public class PassiDiffCallback extends DiffUtil.Callback {


    private ArrayList<Passo> oldPassi;
    private ArrayList<Passo> newPassi;

    PassiDiffCallback(ArrayList<Passo> newPassi, ArrayList<Passo> oldPassi) {
        this.newPassi = newPassi;
        this.oldPassi = oldPassi;
    }

    @Override
    public int getOldListSize() {
        return oldPassi.size();
    }

    @Override
    public int getNewListSize() {
        return newPassi.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldPassi.get(oldItemPosition).getId().equals(newPassi.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldPassi.get(oldItemPosition).equals(newPassi.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }

}
