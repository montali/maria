package com.example.simone.maria;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RicettaViewer extends AppCompatActivity{

    private IngredientiAdapter ingredientiAdapter;
    private TagAdapter tagAdapter;
    private PassoAdapter passiAdapter;
    private ImageAdapter imageAdapter;
    private TextView titolo;
    private TextView descrizione;
    private TextView calorie;
    private ImageView caloriesLogo;
    private TextView people;
    private final DatabaseHelper db = new DatabaseHelper(this);
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent=getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricetta_viewer);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        id = intent.getIntExtra("position", 0);

        Ricetta ricetta = db.getRicetta(id);
        if (ricetta == null)
            finish();
        RecyclerView rvTags = findViewById(R.id.rvTags);
        LinearLayoutManager horizontalTagLayoutManager
                = new LinearLayoutManager(RicettaViewer.this, LinearLayoutManager.HORIZONTAL, false);
        rvTags.setLayoutManager(horizontalTagLayoutManager);
        tagAdapter = new TagAdapter(this, id, false);
        rvTags.setAdapter(tagAdapter);

        RecyclerView recyclerView = findViewById(R.id.rvAnimals);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(RicettaViewer.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        ingredientiAdapter = new IngredientiAdapter(this, id, false);
        recyclerView.setAdapter(ingredientiAdapter);

        ViewPager viewPager = findViewById(R.id.photo_pager);
        imageAdapter = new ImageAdapter(this, ricetta);
        viewPager.setAdapter(imageAdapter);


        titolo = findViewById(R.id.ricetta_titolo);
        descrizione = findViewById(R.id.ricetta_view_desc);
        calorie = findViewById(R.id.calorie_counter);
        people = findViewById(R.id.people_counter);
        caloriesLogo = findViewById(R.id.caloriesLogo);


        // Lookup the recyclerview in activity layout
        RecyclerView rvPassi = findViewById(R.id.preparazione);
        passiAdapter = new PassoAdapter(this, id, false);
        rvPassi.setAdapter(passiAdapter);
        rvPassi.setLayoutManager(new LinearLayoutManager(this));
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((View view) -> {
                Intent myIntent = new Intent(view.getContext(), RicettaEdit.class);
                myIntent.putExtra("position", id); //Optional parameters
                view.getContext().startActivity(myIntent);
        });
        NestedScrollView nsv = findViewById(R.id.scrollViewViewer);
        nsv.setOnScrollChangeListener((NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) -> {
                if (scrollY > oldScrollY) {
                    fab.hide();
                } else {
                    fab.show();
                }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Ricetta ricetta = db.getRicetta(id);
        titolo.setText(ricetta.getName());
        descrizione.setText(ricetta.getmDescription());
        String peopleText = ricetta.getPeople().toString() + getString(R.string.people);
        people.setText(peopleText);
        String calorieText = "";
        if (ricetta.getCalories() != 0) {
            calorieText = ricetta.getCalories().toString() + getString(R.string.calories_abbrv);
            caloriesLogo.setImageResource(R.drawable.ic_weight_solid);
        } else {
            caloriesLogo.setImageResource(R.color.transparent);
        }
        calorie.setText(calorieText);
        ingredientiAdapter.updateList();
        tagAdapter.updateList();
        passiAdapter.updateList();
        imageAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
            db.closeDB();
        super.onDestroy();
    }
}
