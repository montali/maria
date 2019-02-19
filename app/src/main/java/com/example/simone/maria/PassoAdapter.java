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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class PassoAdapter extends RecyclerView.Adapter<PassoAdapter.ViewHolder> {
    private final ArrayList<Passo> mPassi;
    private final DatabaseHelper db;
    private final boolean editing;
    private final int ricetta_id;

    PassoAdapter(Context context, int ricetta_id, boolean editing) {
        db = new DatabaseHelper(context);
        mPassi = db.getPassiFromRicetta(db.getRicetta(ricetta_id));
        this.editing = editing;
        this.ricetta_id = ricetta_id;
        if (editing) {
            mPassi.add(new Passo(null, mPassi.size() + 1, ricetta_id, context.getString(R.string.passo_add), 0));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PassoAdapter.ViewHolder viewHolder, int position) {
        Passo passo = mPassi.get(position);
        TextView passoNum = viewHolder.passoNumberTextView;
        TextView passoTex = viewHolder.passoTextView;
        ImageView checkBox = viewHolder.checkBox;
        passoNum.setText(String.format(Locale.getDefault(), "%d", position + 1));
        passoTex.setText(passo.getDescription());
        if (passo.getId() == null)
            checkBox.setImageResource(R.drawable.ic_plus_solid);

    }

    @Override
    @NonNull
    public PassoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View passiView = inflater.inflate(R.layout.passo_preparazione, parent, false);
        return new ViewHolder(context, passiView);
    }

    void updateList() {
        int currentSize = mPassi.size();
        ArrayList<Passo> newList = db.getPassiFromRicetta(db.getRicetta(ricetta_id));
        if (editing)
            newList.add(new Passo(null, mPassi.size() + 1, ricetta_id, "Aggiungi passo", 0));
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new PassiDiffCallback(this.mPassi, newList));
        this.mPassi.clear();
        this.mPassi.addAll(newList);
        notifyItemRangeRemoved(0, currentSize);
        notifyItemRangeInserted(0, newList.size());
        diffResult.dispatchUpdatesTo(this);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mPassi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView passoNumberTextView;
        private final TextView passoTextView;
        private final ImageView checkBox;
        private final Context context;

        ViewHolder(Context context, View itemView) {
            super(itemView);
            passoNumberTextView = itemView.findViewById(R.id.passo_number);
            passoTextView = itemView.findViewById(R.id.passo_desc);
            checkBox = itemView.findViewById(R.id.checkbox_passo);
            if (!editing)
                checkBox.setImageResource(R.drawable.ic_check_solid_grey);
            this.context = context;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                final Passo passo = mPassi.get(position);
                if (editing) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
                    if (passo.getId() == null)
                        builder.setTitle(context.getString(R.string.passo_add) + Integer.toString(position + 1));
                    else
                        builder.setTitle(context.getString(R.string.passo_edit) + Integer.toString(position + 1));
                    View viewInflated = LayoutInflater.from(this.context).inflate(R.layout.passo_input_dialog, (ViewGroup) view, false);
                    final EditText inputdesc = viewInflated.findViewById(R.id.input_passo_desc);
                    if (passo.getId() != null) {
                        inputdesc.setText(passo.getDescription());
                    }
                    builder.setView(viewInflated);
                    builder.setPositiveButton(R.string.ok, (DialogInterface dialog, int which) -> {
                            dialog.dismiss();
                            passo.setDescription(inputdesc.getText().toString());
                            if (passo.getId() == null) {
                                passo.setId(db.getMaxPasso() + 1);
                                db.createPasso(passo);
                                checkBox.setImageResource(R.color.transparent);
                                mPassi.add(new Passo(null, mPassi.size() + 1, ricetta_id, context.getString(R.string.passo_add), 0));
                            } else {
                                db.updatePasso(passo);
                            }
                            updateList();
                    });
                    builder.setNegativeButton(R.string.annulla, (DialogInterface dialog, int which) -> dialog.cancel());
                    builder.setNeutralButton(R.string.elimina, (DialogInterface dialog, int which) -> {
                            dialog.dismiss();
                            if (passo.getId() != null) {
                                db.deletePasso(passo.getId());
                            }
                            updateList();
                    });

                    builder.show();


                } else {
                    if(passoTextView.getAlpha()==1) {
                        passoTextView.setAlpha((float) 0.4);
                        passoNumberTextView.setAlpha((float) 0.4);
                        checkBox.setImageResource(R.drawable.ic_check_solid);
                    } else{
                        passoTextView.setAlpha((float) 1);
                        passoNumberTextView.setAlpha((float) 1);
                        checkBox.setImageResource(R.drawable.ic_check_solid_grey);
                    }
                }
            }
        }
    }


    }

