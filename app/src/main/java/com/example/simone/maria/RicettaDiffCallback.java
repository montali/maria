package com.example.simone.maria;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.ArrayList;

public class RicettaDiffCallback extends DiffUtil.Callback {


    ArrayList<Ricetta> oldRicette;
    ArrayList<Ricetta> newRicette;

    public RicettaDiffCallback(ArrayList<Ricetta> newRicette, ArrayList<Ricetta> oldRicette) {
        this.newRicette = newRicette;
        this.oldRicette = oldRicette;
    }

    @Override
    public int getOldListSize() {
        return oldRicette.size();
    }

    @Override
    public int getNewListSize() {
        return newRicette.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldRicette.get(oldItemPosition).getId() == newRicette.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldRicette.get(oldItemPosition).equals(newRicette.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }

}
