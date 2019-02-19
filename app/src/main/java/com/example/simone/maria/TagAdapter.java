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

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {
    private ArrayList<Tag> mTags;
    private DatabaseHelper db;
    private boolean editing;
    private int ricettaId;
    private Context context;

    TagAdapter(Context context, int ricettaId, boolean editing) {
        db = new DatabaseHelper(context);
        mTags = db.getTagsFromRicetta(db.getRicetta(ricettaId));
        this.editing = editing;
        this.ricettaId = ricettaId;
        if (editing) {
            mTags.add(new Tag(null, context.getString(R.string.add), ricettaId));
        }
    }


    @Override
    public void onBindViewHolder(@NonNull TagAdapter.ViewHolder viewHolder, int position) {
        Tag tag = mTags.get(position);
        TextView tagTextView = viewHolder.tagTextView;
        tagTextView.setText(tag.getTagName());
    }

    @Override
    @NonNull
    public TagAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View tagView = inflater.inflate(R.layout.tag, parent, false);
        return new TagAdapter.ViewHolder(context, tagView);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mTags.size();
    }

    void updateList() {
        int currentSize = mTags.size();
        ArrayList<Tag> newList = db.getTagsFromRicetta(db.getRicetta(ricettaId));
        if (editing) {
            newList.add(new Tag(null, context.getString(R.string.add), ricettaId));
        }
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new TagDiffCallback(this.mTags, newList));
        this.mTags.clear();
        this.mTags.addAll(newList);
        notifyItemRangeRemoved(0, currentSize);
        notifyItemRangeInserted(0, newList.size());
        diffResult.dispatchUpdatesTo(this);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tagTextView;
        private Context context;

        ViewHolder(Context context, View itemView) {
            super(itemView);
            tagTextView = itemView.findViewById(R.id.tag_name);
            this.context = context;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                final Tag tag = mTags.get(position);
                if (editing) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
                    if (tag.getId() == null)
                        builder.setTitle(R.string.add_tag);
                    else
                        builder.setTitle(R.string.edit_tag);
                    View viewInflated = LayoutInflater.from(this.context).inflate(R.layout.tag_input_dialog, (ViewGroup) view, false);
                    final EditText inputTag = viewInflated.findViewById(R.id.input_tag);
                    if (tag.getId() == null)
                        inputTag.setHint(R.string.tag);
                    else
                        inputTag.setText(tag.getTagName());
                    builder.setView(viewInflated);
                    builder.setPositiveButton(R.string.ok, (DialogInterface dialog, int which) -> {
                            dialog.dismiss();
                            tag.setTagName(inputTag.getText().toString());
                            if (tag.getId() == null) {
                                tag.setId(db.getMaxTag() + 1);
                                tag.setRicettaId(ricettaId);
                                db.createTag(tag);
                                mTags.add(new Tag(null, context.getString(R.string.add), ricettaId));
                            } else {
                                db.updateTag(tag);
                            }
                            updateList();
                    });
                    builder.setNegativeButton(R.string.annulla, (DialogInterface dialog, int which) -> dialog.cancel());
                    builder.setNeutralButton(R.string.elimina, (DialogInterface dialog, int which) -> {
                            dialog.dismiss();
                            if (tag.getId() != null) {
                                db.deleteTag(tag);
                            }
                            updateList();
                    });
                    builder.show();
                }
            }
        }
    }


}
