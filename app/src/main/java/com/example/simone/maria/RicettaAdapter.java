package com.example.simone.maria;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RicettaAdapter extends RecyclerView.Adapter<RicettaAdapter.ViewHolder> {
        public List<Ricetta> mRicette;

    DatabaseHelper db;
    public RicettaAdapter(List<Ricetta> ricette){
            mRicette=ricette;
        }
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            public TextView nameTextView;
            public TextView descriptionTextView;
            public ImageView ricettaImageView;
            private Context context;

            public ViewHolder(Context context, View itemView){
                super(itemView);
                nameTextView=(TextView) itemView.findViewById(R.id.ricetta_name);
                ricettaImageView=(ImageView) itemView.findViewById(R.id.immagine_ricetta);
                descriptionTextView=(TextView) itemView.findViewById(R.id.ricetta_description);
                this.context = context;
                itemView.setOnClickListener(this);
            }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                // We can access the data within the views
                Intent myIntent = new Intent(view.getContext(), RicettaViewer.class);
                myIntent.putExtra("position", position); //Optional parameters
                view.getContext().startActivity(myIntent);
            }
        }
        }
        @Override
        public RicettaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.item_ricetta, parent, false);

            // Return a new holder instance
            ViewHolder viewHolder = new ViewHolder(context,contactView);
            return viewHolder;
        }

        // Involves populating data into the item through holder
        @Override
        public void onBindViewHolder(RicettaAdapter.ViewHolder viewHolder, int position) {
            // Get the data model based on position
            Ricetta contact = mRicette.get(position);
            String subDescription="";
            // Set item views based on your views and data model
            TextView textView = viewHolder.nameTextView;
            textView.setText(contact.getName());
            TextView description = viewHolder.descriptionTextView;
            ImageView imageView=viewHolder.ricettaImageView;
            imageView.setImageResource(contact.getDrawableId());
            if(contact.getmDescription().length()>50) {
                subDescription = contact.getmDescription().substring(0, 50) + "...";
            } else{
                subDescription = contact.getmDescription();
            }
            description.setText(subDescription);
        }

        // Returns the total count of items in the list
        @Override
        public int getItemCount() {
            return mRicette.size();
        }
    }

