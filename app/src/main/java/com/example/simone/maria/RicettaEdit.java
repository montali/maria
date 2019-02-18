package com.example.simone.maria;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.asksira.bsimagepicker.BSImagePicker;

import java.util.ArrayList;
import java.util.List;

public class RicettaEdit extends AppCompatActivity implements BSImagePicker.OnSingleImageSelectedListener,
        BSImagePicker.OnMultiImageSelectedListener {

    private Ricetta ricetta;
    EditText titolo;
    EditText descrizione;
    TextView peopleEdit;
    TextView caloriesEdit;
    private BSImagePicker multiSelectionPicker;
    private DatabaseHelper db = new DatabaseHelper(this);
    private ImageEditAdapter imageEditAdapter;
    private Context context;
    private int MAX_CALORIES = 2000;
    private int MIN_CALORIES = 100;
    private ImageButton tastoAddPhoto;

    // Sull'onCreate, creiamo tutti gli adapter con l'id della ricetta, e riempiamo la UI
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricetta_edit);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        int id = intent.getIntExtra("position", 0);
        ricetta = db.getRicetta(id);

        RecyclerView rvTags = findViewById(R.id.rvTags);
        LinearLayoutManager horizontalTagLayoutManager
                = new LinearLayoutManager(RicettaEdit.this, LinearLayoutManager.HORIZONTAL, false);
        rvTags.setLayoutManager(horizontalTagLayoutManager);
        TagAdapter tagAdapter = new TagAdapter(this, id, true);
        rvTags.setAdapter(tagAdapter);

        RecyclerView recyclerView = findViewById(R.id.rvAnimalsEdit);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(RicettaEdit.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        IngredientiAdapter adapter = new IngredientiAdapter(this, id, true);
        recyclerView.setAdapter(adapter);

        ViewPager viewPager = (ViewPager) findViewById(R.id.photo_edit_pager);
        imageEditAdapter = new ImageEditAdapter(this, ricetta);
        viewPager.setAdapter(imageEditAdapter);
        caloriesEdit = findViewById(R.id.edit_calories);
        peopleEdit = findViewById(R.id.people_edit_counter);

        titolo = (EditText) findViewById(R.id.ricetta_titolo_edit);
        descrizione = (EditText) findViewById(R.id.ricetta_edit_desc);
        tastoAddPhoto = (ImageButton) findViewById(R.id.tastoAddPhoto);

        // Se la ricetta esiste, la modifichiamo
        if (ricetta.getName() != null) {
            titolo.setText(ricetta.getName());
            descrizione.setText(ricetta.getmDescription());
            String caloriesText = getString(R.string.insert_calories);
            if (ricetta.getCalories() != 0)
                caloriesText = ricetta.getCalories().toString() + getString(R.string.calories_abbrv);
            caloriesEdit.setText(caloriesText);
            String peopleText = ricetta.getPeople().toString() + getString(R.string.people);
            peopleEdit.setText(peopleText);
            // Altrimenti, setto gli hint
        } else {
            titolo.setHint(R.string.titolo_hint);
            descrizione.setHint(R.string.descrizione_hint);
            caloriesEdit.setText(R.string.caloriesHint);
            peopleEdit.setText(R.string.peopleHint);
        }// Se non abbiamo immagini, metto un + come logo
        if (db.getImmaginiFromRicetta(ricetta).size() == 0) {
            tastoAddPhoto.setImageResource(R.drawable.ic_images_regular);
            tastoAddPhoto.setColorFilter(Color.argb(255, 0, 0, 0));
        } else {
            tastoAddPhoto.setImageResource(R.drawable.ic_plus_solid);
            tastoAddPhoto.setColorFilter(Color.argb(255, 255, 255, 255));
        }
        RecyclerView rvPassi = (RecyclerView) findViewById(R.id.preparazione_edit);
        PassoAdapter paAdapter = new PassoAdapter(this, id, true);
        rvPassi.setAdapter(paAdapter);
        rvPassi.setLayoutManager(new LinearLayoutManager(this));
        context = this;
        tastoAddPhoto.setOnClickListener(new View.OnClickListener() {
            // Uso l'imagePicker di una libreria
            public void onClick(View view) {
                if (db.getImmaginiFromRicetta(ricetta).size() < 3) {
                    multiSelectionPicker = new BSImagePicker.Builder(getString(R.string.fileprovider))
                            .isMultiSelect() //Set this if you want to use multi selection mode.
                            .setMinimumMultiSelectCount(1) //Default: 1.
                            .setMaximumMultiSelectCount(3) //Default: Integer.MAX_VALUE (i.e. User can select as many images as he/she wants)
                            .setMultiSelectBarBgColor(android.R.color.white) //Default: #FFFFFF. You can also set it to a translucent color.
                            .setMultiSelectTextColor(R.color.primary_text) //Default: #212121(Dark grey). This is the message in the multi-select bottom bar.
                            .setMultiSelectDoneTextColor(R.color.colorAccent) //Default: #388e3c(Green). This is the color of the "Done" TextView.
                            .setOverSelectTextColor(R.color.error_text) //Default: #b71c1c. This is the color of the message shown when user tries to select more than maximum select count.
                            .build();
                    multiSelectionPicker.show(getSupportFragmentManager(), "picker");
                } else {
                    Toast.makeText(context, R.string.maximum_photos_reached, Toast.LENGTH_LONG);
                }
            }
        });
        // Premendo il numero di persone, apro un AlertDialog con un NumberPicker
        peopleEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RicettaEdit.this);
                if (ricetta.getPeople() == null)
                    builder.setTitle(R.string.add_people);
                else
                    builder.setTitle(R.string.edit_people);
                View viewInflated = LayoutInflater.from(view.getContext()).inflate(R.layout.tag_input_number, null);
                final NumberPicker peopleSelector = viewInflated.findViewById(R.id.people_selector);
                peopleSelector.setMinValue(1);
                peopleSelector.setMaxValue(20);
                builder.setView(viewInflated);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ricetta.setPeople(peopleSelector.getValue());
                        String peopleText = ricetta.getPeople().toString() + getString(R.string.people);
                        peopleEdit.setText(peopleText);

                    }
                });
                builder.setNegativeButton(R.string.annulla, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        // Come per le persone, sull'onClick delle calorie apro un AlertDialog
        caloriesEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RicettaEdit.this);
                if (ricetta.getCalories() == null)
                    builder.setTitle(R.string.insert_calories);
                else
                    builder.setTitle(R.string.edit_calories);
                View viewInflated = LayoutInflater.from(view.getContext()).inflate(R.layout.tag_input_number, null);
                final NumberPicker peopleSelector = viewInflated.findViewById(R.id.people_selector);
                peopleSelector.setMinValue(0);
                final int iStepsArray = (MAX_CALORIES - MIN_CALORIES) / 50 + 1; //get the lenght array that will return
                peopleSelector.setMaxValue(iStepsArray);
                String[] arrayValues = new String[iStepsArray + 1]; //Create array with length of iStepsArray

                for (int i = 0; i < iStepsArray; i++) {
                    arrayValues[i] = String.valueOf(MIN_CALORIES + (i * 50));
                }
                arrayValues[iStepsArray] = getString(R.string.dunno);


                peopleSelector.setDisplayedValues(arrayValues);
                builder.setView(viewInflated);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String caloriesText = getString(R.string.insert_calories);
                        if (peopleSelector.getValue() == iStepsArray)
                            ricetta.setCalories(null);
                        else {
                            String sValue = String.valueOf(MIN_CALORIES + (peopleSelector.getValue() * 50));
                            ricetta.setCalories(Integer.parseInt(sValue));
                            caloriesText = ricetta.getCalories().toString() + getString(R.string.calories_abbrv);
                        }
                        caloriesEdit.setText(caloriesText);

                    }
                });
                builder.setNegativeButton(R.string.annulla, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
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
            // Se preme sul trash, cancello la ricetta e tutto ciò che la riguarda
            case R.id.delete_menu:
                db.deleteRicetta(ricetta.getId());
                ArrayList<Tag> mRecentlyDeletedTags = db.getTagsFromRicetta(ricetta);
                ArrayList<Passo> mRecentlyDeletedPassi = db.getPassiFromRicetta(ricetta);
                ArrayList<Immagine> mRecentlyDeletedImmagini = db.getImmaginiFromRicetta(ricetta);
                ArrayList<Ingrediente> mRecentlyDeletedIngredienti = db.getIngredientiFromRicetta(ricetta);
                for (int i = 0; i < mRecentlyDeletedTags.size(); i++) {
                    db.deleteTag(mRecentlyDeletedTags.get(i));
                }
                for (int i = 0; i < mRecentlyDeletedPassi.size(); i++) {
                    db.deletePasso(mRecentlyDeletedPassi.get(i).getId());
                }
                for (int i = 0; i < mRecentlyDeletedImmagini.size(); i++) {
                    db.deleteImmagine(mRecentlyDeletedImmagini.get(i).getId());
                }
                for (int i = 0; i < mRecentlyDeletedIngredienti.size(); i++) {
                    db.deleteIngrediente(mRecentlyDeletedIngredienti.get(i).getId());
                }
                finish();
                return true;
            case android.R.id.home:
                // Se torna indietro, salvo tutto
                if (checkRequiredData()) {
                    ricetta.setmName(titolo.getText().toString());
                    ricetta.setmDescription(descrizione.getText().toString());
                    db.updateRicetta(ricetta);
                    finish();
                }

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // Questa funzione verifica che ci siano tutti i dati richiesti. Se non ci sono, AlertDialog
    public boolean checkRequiredData() {
        if (titolo.getText().length() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.name_required)
                    .setPositiveButton(R.string.gotit, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
            return false;
        } else if (descrizione.getText().length() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.desc_required)
                    .setPositiveButton(R.string.gotit, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
            return false;
        } else if (db.getImmaginiFromRicetta(ricetta).size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.photo_required)
                    .setPositiveButton(R.string.gotit, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
            return false;
        } else if (ricetta.getPeople() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.people_required)
                    .setPositiveButton(R.string.gotit, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
            return false;
        } else if (db.getPassiFromRicetta(ricetta).size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.passo_required)
                    .setPositiveButton(R.string.gotit, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
            return false;
        } else if (db.getIngredientiFromRicetta(ricetta).size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.ingrediente_required)
                    .setPositiveButton(R.string.gotit, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
            return false;

        } else {
            return true;
        }
    }

    // Quando l'utente esce, salvo tutto
    @Override
    public void onBackPressed() {
        if (checkRequiredData()) {
            ricetta.setmName(titolo.getText().toString());
            ricetta.setmDescription(descrizione.getText().toString());
            db.updateRicetta(ricetta);
            super.onBackPressed();
        }
    }

    // Le due seguenti sono funzioni della libreria esterna, chiamate quando vengono selezionate immagini
    @Override
    public void onSingleImageSelected(Uri uri, String tag) {
        db.createImmagine(new Immagine(db.getMaxImmagine() + 1, uri, ricetta.getId()));
        imageEditAdapter.notifyDataSetChanged();
        tastoAddPhoto.setImageResource(R.drawable.ic_plus_solid);
        tastoAddPhoto.setColorFilter(Color.argb(255, 255, 255, 255));
    }

    @Override
    public void onMultiImageSelected(List<Uri> uriList, String tag) {
        for (int i = 0; i < uriList.size(); i++) {
            db.createImmagine(new Immagine(db.getMaxImmagine() + 1, uriList.get(i), ricetta.getId()));
            imageEditAdapter.notifyDataSetChanged();
        }
        tastoAddPhoto.setImageResource(R.drawable.ic_plus_solid);
        tastoAddPhoto.setColorFilter(Color.argb(255, 255, 255, 255));
    }

    @Override
    public void onDestroy() {
        if (db != null)
            db.closeDB();
        super.onDestroy();
    }
}
