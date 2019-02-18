package com.example.simone.maria;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PassoEditAdapter extends RecyclerView.Adapter<PassoEditAdapter.ViewHolder> {
    private List<Passo> mPassi;

    public PassoEditAdapter(List<Passo> passi){mPassi=passi;}
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView passoNumberTextView;
        public TextView passoTextView;
        public ImageView checkBox;
        private Context context;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            passoNumberTextView = (TextView) itemView.findViewById(R.id.passo_number);
            passoTextView = (TextView) itemView.findViewById(R.id.passo_desc);
            checkBox=(ImageView) itemView.findViewById(R.id.checkbox_passo);
            this.context = context;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Passo passo = mPassi.get(position);
                if(passoTextView.getAlpha()==1) {
                    passoTextView.setAlpha((float) 0.4);
                    passoNumberTextView.setAlpha((float) 0.4);
                    checkBox.setAlpha((float) 0.4);
                    passo.setDone(1);
                } else{
                    passoTextView.setAlpha((float) 1);
                    passoNumberTextView.setAlpha((float) 1);
                    checkBox.setAlpha((float) 1);
                    passo.setDone(0);
                }




            }
        }
    }
        @Override
        public PassoEditAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View passiView = inflater.inflate(R.layout.passo_preparazione, parent, false);
            ViewHolder viewHolder = new ViewHolder(context,passiView);
            return viewHolder;
        }
        @Override
        public void onBindViewHolder(PassoEditAdapter.ViewHolder viewHolder, int position){
            Passo passo = mPassi.get(position);
            TextView passoNum = viewHolder.passoNumberTextView;
            TextView passoTex = viewHolder.passoTextView;
            passoNum.setText(Integer.toString(position+1));
            passoTex.setText(passo.getDescription());
        }
        // Returns the total count of items in the list
        @Override
        public int getItemCount() {
            return mPassi.size();
        }
    }

