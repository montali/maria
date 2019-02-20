package com.example.simone.maria;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


class ImageAdapter extends PagerAdapter {
    private final Context context;
    private final DatabaseHelper db;
    private final Ricetta ricetta;

    ImageAdapter(Context context, Ricetta ricetta) {
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
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageURI(db.getImmaginiFromRicetta(ricetta).get(position).getPhoto_uri());
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
