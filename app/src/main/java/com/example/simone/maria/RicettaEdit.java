package com.example.simone.maria;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.List;

public class RicettaEdit extends AppCompatActivity {

    private IngredientiAdapter adapter;
    private Ricetta ricetta;
    EditText titolo;
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent=getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricetta_edit);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        int id =  intent.getIntExtra("position",0);
        Ricetta ricetta = db.getRicetta(id);
        List<Ingrediente> ingredienti = db.getIngredientiFromRicetta(ricetta);
        List<Passo> passi = db.getPassiFromRicetta(ricetta);
        ingredienti.add(new Ingrediente(null, "Aggiungi ingrediente", null, ricetta.getId()));
        passi.add(new Passo(null, passi.size() + 1, ricetta.getId(), "Aggiungi passo", 0));
        RecyclerView recyclerView = findViewById(R.id.rvAnimalsEdit);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(RicettaEdit.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        adapter = new IngredientiAdapter(this, ingredienti);
        recyclerView.setAdapter(adapter);
        titolo = (EditText) findViewById(R.id.ricetta_titolo_edit);
        titolo.setText(ricetta.getName());
        // Lookup the recyclerview in activity layout
        RecyclerView rvPassi = (RecyclerView) findViewById(R.id.preparazione_edit);
        PassoEditAdapter paAdapter = new PassoEditAdapter(passi);
        rvPassi.setAdapter(paAdapter);
        rvPassi.setLayoutManager(new LinearLayoutManager(this));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.saver, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                ricetta.setmName(titolo.getText().toString());
                db.updateRicetta(ricetta);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
