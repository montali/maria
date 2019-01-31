package com.example.simone.maria;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class IngredientiAdapter extends RecyclerView.Adapter<IngredientiAdapter.ViewHolder> {

    private List<Ingrediente> mIngredienti;

    public IngredientiAdapter(Context context,List<Ingrediente> ingredienti){
        mIngredienti=ingredienti;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView nameTextView;
        public TextView gramsTextView;
        public ImageView ricettaImageView;
        private Context context;

        public ViewHolder(Context context, View itemView){
            super(itemView);
            nameTextView=(TextView) itemView.findViewById(R.id.ingrediente_name);
            ricettaImageView=(ImageView) itemView.findViewById(R.id.immagine_ingrediente);
            gramsTextView=(TextView) itemView.findViewById(R.id.ingrediente_grams);
            this.context = context;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                Ingrediente ingrediente = mIngredienti.get(position);
                // We can access the data within the views
                Toast.makeText(context, ingrediente.getName(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public IngredientiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.ingrediente, parent, false);

        // Return a new holder instance
        IngredientiAdapter.ViewHolder viewHolder = new IngredientiAdapter.ViewHolder(context,contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(IngredientiAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Ingrediente ingrediente = mIngredienti.get(position);
        String quantity="";
        // Set item views based on your views and data model
        TextView textView = viewHolder.nameTextView;
        textView.setText(ingrediente.getName());
        TextView description = viewHolder.gramsTextView;
        ImageView imageView=viewHolder.ricettaImageView;
        imageView.setImageResource(ingrediente.getDrawableId());
        quantity=Integer.toString(ingrediente.getGrams()) + " g";
        description.setText(quantity);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mIngredienti.size();
    }
}


