package com.example.simone.maria;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.ArrayList;

public class IngredientiDiffCallback extends DiffUtil.Callback {


    ArrayList<Ingrediente> oldIngredienti;
    ArrayList<Ingrediente> newIngredienti;

    public IngredientiDiffCallback(ArrayList<Ingrediente> newIngredienti, ArrayList<Ingrediente> oldIngredienti) {
        this.newIngredienti = newIngredienti;
        this.oldIngredienti = oldIngredienti;
    }

    @Override
    public int getOldListSize() {
        return oldIngredienti.size();
    }

    @Override
    public int getNewListSize() {
        return newIngredienti.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldIngredienti.get(oldItemPosition).getId() == newIngredienti.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldIngredienti.get(oldItemPosition).equals(newIngredienti.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }

}
