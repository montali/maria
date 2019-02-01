package com.example.simone.maria;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper db;
    ArrayList<Ricetta> ricette;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(getApplicationContext());
        // ...
        // Lookup the recyclerview in activity layout
        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
        // Initialize contacts
        ArrayList<Ingrediente> setIngredienti = new ArrayList<Ingrediente>();
        setIngredienti.add(new Ingrediente(1, "Sugo", 200, 1));
        setIngredienti.add(new Ingrediente(2, "Pasta", 100, 1));
        setIngredienti.add(new Ingrediente(3, "Sale", 20, 1));
        long[] tags = {};
        db.createRicetta(new Ricetta("Pasta al sugo", 1, "Buona e nutriente", setIngredienti), tags);
        ricette = db.getAllRicette();
        // Create adapter passing in the sample user data
        RicettaAdapter adapter = new RicettaAdapter(ricette);
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        // That's all!
    }
}
