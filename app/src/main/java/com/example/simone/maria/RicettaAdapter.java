package com.example.simone.maria;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RicettaAdapter extends RecyclerView.Adapter<RicettaAdapter.ViewHolder> {
    private DatabaseHelper db;
    private ArrayList<Ricetta> ricette;
    private Context context;
    private Ricetta mRecentlyDeletedItem;
    private ArrayList<Tag> mRecentlyDeletedTags;
    private ArrayList<Passo> mRecentlyDeletedPassi;
    private ArrayList<Immagine> mRecentlyDeletedImmagini;
    private ArrayList<Ingrediente> mRecentlyDeletedIngredienti;
    private int mRecentlyDeletedItemPosition;


    RicettaAdapter(DatabaseHelper db, ArrayList<Ricetta> ricette, Context context) {
        this.db = db;
        this.ricette = ricette;
        this.context = context;
    }

        @Override
        @NonNull
        public RicettaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            // Inflate the custom layout
            View ricettaView = inflater.inflate(R.layout.item_ricetta, parent, false);

            // Return a new holder instance
            return new ViewHolder(context, ricettaView);
        }

        // Involves populating data into the item through holder
        @Override
        public void onBindViewHolder(@NonNull RicettaAdapter.ViewHolder viewHolder, int position) {
            // Get the data model based on position
            Ricetta ricetta = ricette.get(position);
            String subDescription="";
            // Set item views based on your views and data model
            TextView textView = viewHolder.nameTextView;
            textView.setText(ricetta.getName());
            TextView description = viewHolder.descriptionTextView;
            ImageView imageView=viewHolder.ricettaImageView;
            if (db.getImmaginiFromRicetta(ricetta).size() != 0)
                imageView.setImageURI(db.getImmaginiFromRicetta(ricetta).get(0).getPhoto_uri());
            if (ricetta.getmDescription() != null) {
                if (ricetta.getmDescription().length() > 50) {
                    subDescription = ricetta.getmDescription().substring(0, 50) + "...";
            } else{
                    subDescription = ricetta.getmDescription();
                }
            }
            description.setText(subDescription);
        }

        // Returns the total count of items in the list
        @Override
        public int getItemCount() {
            return ricette.size();
        }

    void filter(String query) {
        ArrayList<Ricetta> newRicette = db.searchRicette(query);
        updateList(newRicette);
    }

    void updateList(ArrayList<Ricetta> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new RicettaDiffCallback(this.ricette, newList));
        int currentSize = ricette.size();
        this.ricette.clear();
        this.ricette.addAll(newList);
        notifyItemRangeRemoved(0, currentSize);
        notifyItemRangeInserted(0, newList.size());
        diffResult.dispatchUpdatesTo(this);
    }

    public Context getContext() {
        return context;
    }

    void deleteItem(int position) {
        mRecentlyDeletedItem = new Ricetta(ricette.get(position));
        mRecentlyDeletedItemPosition = position;

        mRecentlyDeletedTags = db.getTagsFromRicetta(mRecentlyDeletedItem);
        mRecentlyDeletedPassi = db.getPassiFromRicetta(mRecentlyDeletedItem);
        mRecentlyDeletedImmagini = db.getImmaginiFromRicetta(mRecentlyDeletedItem);
        mRecentlyDeletedIngredienti = db.getIngredientiFromRicetta(mRecentlyDeletedItem);
        for (int i = 0; i < mRecentlyDeletedTags.size(); i++) {
            db.deleteTag(mRecentlyDeletedTags.get(i));
        }
        for (int i = 0; i < mRecentlyDeletedPassi.size(); i++) {
            db.deletePasso(mRecentlyDeletedPassi.get(i).getId());
        }
        for (int i = 0; i < mRecentlyDeletedImmagini.size(); i++) {
            db.deleteImmagine(mRecentlyDeletedImmagini.get(i).getId());
        }
        for (int i = 0; i < mRecentlyDeletedIngredienti.size(); i++) {
            db.deleteIngrediente(mRecentlyDeletedIngredienti.get(i).getId());
        }


        db.deleteRicetta(ricette.get(position).getId());

        ricette.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        View view = ((MainActivity) context).findViewById(R.id.layout_main);
        Snackbar snackbar = Snackbar.make(view, "Ricetta eliminata",
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, v -> undoDelete());
        snackbar.show();
    }

    private void undoDelete() {
        ricette.add(mRecentlyDeletedItemPosition,
                mRecentlyDeletedItem);
        db.createRicetta(mRecentlyDeletedItem);
        for (int i = 0; i < mRecentlyDeletedTags.size(); i++) {
            db.createTag(mRecentlyDeletedTags.get(i));
        }
        for (int i = 0; i < mRecentlyDeletedPassi.size(); i++) {
            db.createPasso(mRecentlyDeletedPassi.get(i));
        }
        for (int i = 0; i < mRecentlyDeletedImmagini.size(); i++) {
            db.createImmagine(mRecentlyDeletedImmagini.get(i));
        }
        for (int i = 0; i < mRecentlyDeletedIngredienti.size(); i++) {
            db.createIngrediente(mRecentlyDeletedIngredienti.get(i));
        }
        notifyItemInserted(mRecentlyDeletedItemPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTextView;
        TextView descriptionTextView;
        ImageView ricettaImageView;
        private Context context;

        ViewHolder(Context context, View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.ricetta_name);
            ricettaImageView = itemView.findViewById(R.id.immagine_ricetta);
            descriptionTextView = itemView.findViewById(R.id.ricetta_description);
            this.context = context;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                // We can access the data within the views
                Intent myIntent = new Intent(context, RicettaViewer.class);
                myIntent.putExtra("position", ricette.get(position).getId()); //Optional parameters
                view.getContext().startActivity(myIntent);
            }
        }
        }
}

