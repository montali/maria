package com.example.simone.maria;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class ImageAdapter extends PagerAdapter {
    Context context;
    private DatabaseHelper db;
    private Ricetta ricetta;

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
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageURI(db.getImmaginiFromRicetta(ricetta).get(position).getPhoto_uri());
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
