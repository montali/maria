package com.example.simone.maria;

import android.support.v7.util.DiffUtil;

import java.util.ArrayList;

class IngredientiDiffCallback extends DiffUtil.Callback {


    private final ArrayList<Ingrediente> oldIngredienti;
    private final ArrayList<Ingrediente> newIngredienti;

    IngredientiDiffCallback(ArrayList<Ingrediente> newIngredienti, ArrayList<Ingrediente> oldIngredienti) {
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
        if (newIngredienti.get(newItemPosition).getId() != null && oldIngredienti.get(oldItemPosition).getId() != null)
            return oldIngredienti.get(oldItemPosition).getId().equals(newIngredienti.get(newItemPosition).getId());
        else
            return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldIngredienti.get(oldItemPosition).equals(newIngredienti.get(newItemPosition));
    }

}
