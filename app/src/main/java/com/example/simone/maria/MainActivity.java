package com.example.simone.maria;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import co.naughtyspirit.showcaseview.ShowcaseView;
import co.naughtyspirit.showcaseview.targets.Target;
import co.naughtyspirit.showcaseview.targets.TargetView;
import co.naughtyspirit.showcaseview.utils.PositionsUtil;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    DatabaseHelper db;
    ArrayList<Ricetta> ricette;
    RicettaAdapter adapter;
    private ShowcaseView sv = null;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(getApplicationContext());
        RecyclerView rvRicette = (RecyclerView) findViewById(R.id.rvRicette);
        ricette = db.getAllRicette();
        // Create adapter passing in the sample user data
        adapter = new RicettaAdapter(db, ricette, this);
        // Attach the adapter to the recyclerview to populate items
        rvRicette.setAdapter(adapter);
        // Set layout manager to position the items
        rvRicette.setLayoutManager(new LinearLayoutManager(this));
        // That's all!
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(rvRicette);
        fab = findViewById(R.id.main_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = db.createRicetta(new Ricetta(null, db.getMaxRicetta() + 1, null, null));
                Intent myIntent = new Intent(view.getContext(), RicettaEdit.class);
                myIntent.putExtra("position", id); //Optional parameters
                view.getContext().startActivity(myIntent);
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        adapter.filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.filter(query);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sv != null) {
            sv.hide();
            sv = null;
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            Target target = new TargetView(fab, TargetView.ShowcaseType.CIRCLE);

            sv = new ShowcaseView.Builder(this, this.getClass().getName())
                    .setTarget(target)
                    .setDescription(getString(R.string.welcome_message),
                            PositionsUtil.ItemPosition.CENTER)
                    .setButton(PositionsUtil.ItemPosition.BOTTOM_RIGHT)
                    .build();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }

        adapter.updateList(db.getAllRicette());


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.info:
                Intent aboutIntent = new Intent(this, About.class);
                this.startActivity(aboutIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        if (db != null)
            db.closeDB();
        super.onDestroy();
    }

}
