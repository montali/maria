package com.example.simone.maria;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;


class ImageEditAdapter extends PagerAdapter {
    private final Context context;
    private final DatabaseHelper db;
    private final Ricetta ricetta;

    ImageEditAdapter(Context context, Ricetta ricetta) {
        this.context = context;
        this.db = new DatabaseHelper(context);
        this.ricetta = ricetta;
    }

    @Override
    public int getCount() {
        return db.getImmaginiFromRicetta(ricetta).size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    @NonNull
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.image_edit_view, container, false);
        ImageView imageView = layout.findViewById(R.id.imageViewEdit);
        imageView.setImageURI(db.getImmaginiFromRicetta(ricetta).get(position).getPhoto_uri());
        ImageButton deleteButton = layout.findViewById(R.id.buttonDeletePhoto);
        deleteButton.setOnClickListener((View view) ->
                new AlertDialog.Builder(context)
                        .setTitle(R.string.delete_confirmation_title)
                        .setMessage(R.string.delete_confirmation_text)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, (DialogInterface dialog, int whichButton) -> {
                            db.deleteImmagine(db.getImmaginiFromRicetta(ricetta).get(position).getId());
                            destroyItem(container, position, layout);
                            notifyDataSetChanged();
                        })
                        .setNegativeButton(android.R.string.no, null).show()
        );
        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

}
