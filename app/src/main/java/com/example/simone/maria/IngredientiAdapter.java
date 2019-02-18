package com.example.simone.maria;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;


public class IngredientiAdapter extends RecyclerView.Adapter<IngredientiAdapter.ViewHolder> {

    private ArrayList<Ingrediente> mIngredienti;
    private DatabaseHelper db;
    private boolean editing;
    private int ricetta_id;
    private Context context;

    IngredientiAdapter(Context context, int ricetta_id, boolean editing) {
        db = new DatabaseHelper(context);
        mIngredienti = db.getIngredientiFromRicetta(db.getRicetta(ricetta_id));
        this.editing = editing;
        this.ricetta_id = ricetta_id;
        if (editing) {
            mIngredienti.add(new Ingrediente(null, context.getString(R.string.add), null, ricetta_id));
        }
    }

    private static boolean isInteger(String s) {
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) return false;
                else continue;
            }
            if (Character.digit(s.charAt(i), 10) < 0) return false;
        }
        return true;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull IngredientiAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Ingrediente ingrediente = mIngredienti.get(position);
        // Set item views based on your views and data model
        TextView textView = viewHolder.nameTextView;
        textView.setText(ingrediente.getName());
        TextView description = viewHolder.gramsTextView;
        if (ingrediente.getId() != null) {
            String quantity = Integer.toString(ingrediente.getGrams()) + " g";
            description.setText(quantity);
        } else {
            description.setText("");
        }
    }

    @Override
    @NonNull
    public IngredientiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View ingredientiView = inflater.inflate(R.layout.ingrediente, parent, false);

        // Return a new holder instance
        return new IngredientiAdapter.ViewHolder(context, ingredientiView);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mIngredienti.size();
    }

    void updateList() {
        int currentSize = mIngredienti.size();
        ArrayList<Ingrediente> newList = db.getIngredientiFromRicetta(db.getRicetta(ricetta_id));
        if (editing)
            newList.add(new Ingrediente(null, context.getString(R.string.add), null, ricetta_id));
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new IngredientiDiffCallback(this.mIngredienti, newList));
        this.mIngredienti.clear();
        this.mIngredienti.addAll(newList);
        notifyItemRangeRemoved(0, currentSize);
        notifyItemRangeInserted(0, newList.size());
        diffResult.dispatchUpdatesTo(this);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nameTextView;
        TextView gramsTextView;
        private Context context;

        ViewHolder(Context context, View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.ingrediente_name);
            gramsTextView = itemView.findViewById(R.id.ingrediente_grams);
            this.context = context;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                final Ingrediente ingrediente = mIngredienti.get(position);
                // We can access the data within the views

                if (editing) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
                    if (ingrediente.getId() == null)
                        builder.setTitle(R.string.add_ingrediente);
                    else
                        builder.setTitle(R.string.edit_ingrediente);
                    View viewInflated = LayoutInflater.from(this.context).inflate(R.layout.text_input_dialog, (ViewGroup) view, false);
                    final EditText inputname = viewInflated.findViewById(R.id.input);
                    final EditText inputqty = viewInflated.findViewById(R.id.input_quantity);
                    if (ingrediente.getId() == null) {
                        inputname.setHint(R.string.hint_nome);
                        inputqty.setHint(R.string.hint_grams);
                    } else {
                        inputname.setText(ingrediente.getmName());
                        inputqty.setText(String.format(Locale.getDefault(), "%d", ingrediente.getGrams()));
                    }

                    builder.setView(viewInflated);

                    builder.setPositiveButton(R.string.ok, ((DialogInterface dialog, int which) -> {
                        dialog.dismiss();
                        ingrediente.setmName(inputname.getText().toString());
                        if (isInteger(inputqty.getText().toString()))
                            ingrediente.setGrams(Integer.parseInt(inputqty.getText().toString()));
                        else
                            ingrediente.setGrams(0);
                        if (ingrediente.getId() == null) {
                            ingrediente.setId(db.getMaxIngrediente() + 1);
                            ingrediente.setRicetta_id(ricetta_id);
                            db.createIngrediente(ingrediente);
                            mIngredienti.add(new Ingrediente(null, context.getString(R.string.add), null, ricetta_id));
                        } else {
                            db.updateIngrediente(ingrediente);
                        }
                        updateList();
                    }));

                    builder.setNegativeButton(R.string.annulla, ((DialogInterface dialog, int which) -> dialog.cancel()));


                    builder.setNeutralButton(R.string.elimina, ((DialogInterface dialog, int which) -> {
                        dialog.dismiss();
                        if (ingrediente.getId() != null) {
                            db.deleteIngrediente(ingrediente.getId());
                        }
                        updateList();
                    }));


                    builder.show();
                }
            }
        }
    }

}


