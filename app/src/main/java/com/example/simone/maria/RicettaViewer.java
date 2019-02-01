package com.example.simone.maria;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class RicettaViewer extends AppCompatActivity{

    private IngredientiAdapter adapter;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent=getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricetta_viewer);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        final int id =  intent.getIntExtra("position",0);
        //final Ricetta ricetta=(Ricetta) intent.getParcelableExtra("ricetta");
        final Ricetta ricetta = db.getRicetta(id);
        List<Ingrediente> ingredienti = db.getIngredientiFromRicetta(ricetta);
        List<Passo> passi = db.getPassiFromRicetta(ricetta);
        RecyclerView recyclerView = findViewById(R.id.rvAnimals);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(RicettaViewer.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        adapter = new IngredientiAdapter(this, ingredienti);
        recyclerView.setAdapter(adapter);
        TextView titolo = (TextView) findViewById(R.id.ricetta_titolo);
        titolo.setText(ricetta.getName());
        // Lookup the recyclerview in activity layout
        RecyclerView rvPassi = (RecyclerView) findViewById(R.id.preparazione);
        PassoAdapter paAdapter = new PassoAdapter(passi);
        rvPassi.setAdapter(paAdapter);
        rvPassi.setLayoutManager(new LinearLayoutManager(this));
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), RicettaEdit.class);
                myIntent.putExtra("position", id); //Optional parameters
                view.getContext().startActivity(myIntent);
            }

        });
        NestedScrollView nsv = findViewById(R.id.scrollViewViewer);
        nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }
        });
    }
}
