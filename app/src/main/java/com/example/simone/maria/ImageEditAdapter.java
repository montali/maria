package com.example.simone.maria;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;


public class ImageEditAdapter extends PagerAdapter {
    Context context;
    private DatabaseHelper db;
    private Ricetta ricetta;

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
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.image_edit_view, container, false);
        ImageView imageView = (ImageView) layout.findViewById(R.id.imageViewEdit);
        imageView.setImageURI(db.getImmaginiFromRicetta(ricetta).get(position).getPhoto_uri());
        ImageButton deleteButton = (ImageButton) layout.findViewById(R.id.buttonDeletePhoto);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle(R.string.delete_confirmation_title)
                        .setMessage(R.string.delete_confirmation_text)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                db.deleteImmagine(db.getImmaginiFromRicetta(ricetta).get(position).getId());
                                destroyItem(container, position, layout);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });


        container.addView(layout);

        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
