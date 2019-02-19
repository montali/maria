package com.example.simone.maria;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;

class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Nome database
    private static final String DATABASE_NAME = "ricettaManager";

    // Nomi tabelle
    private static final String TABLE_RICETTA = "ricetta";
    private static final String TABLE_RICETTA_FTS = "ricetta_fts";
    private static final String TABLE_TAG = "tag";
    private static final String TABLE_TAG_FTS = "tag_fts";
    private static final String TABLE_IMMAGINE = "immagine";
    private static final String TABLE_PASSO = "passo";
    private static final String TABLE_INGREDIENTE = "ingrediente";
    private static final String TABLE_INGREDIENTE_FTS = "ingrediente_fts";


    // Nomi colonne comuni
    private static final String KEY_ID = "id";
    private static final String KEY_RICETTA_ID = "ricetta_id";

    // RICETTA Table - nomi colonne
    private static final String KEY_RICETTA = "nome";
    private static final String KEY_DESCRIPTION = "descrizione";
    private static final String KEY_PEOPLE = "persone";
    private static final String KEY_CALORIES = "calorie";

    // TAG Table - nomi colonne
    private static final String KEY_TAG_NAME = "tag_name";


    // IMMAGINE Table - nomi colonne
    private static final String KEY_IMAGE_URI = "image_id";

    // PASSO Table - nomi colonne
    private static final String KEY_PASSO_NUM = "passo_num";
    private static final String KEY_PASSO_DONE = "passo_done";
    private static final String KEY_PASSO_DESC = "passo_desc";

    // INGREDIENTE Table - nomi colonne
    private static final String KEY_INGREDIENTE = "ingrediente";
    private static final String KEY_QUANTITA = "quantita";


    // Table Create Statements

    private static final String CREATE_TABLE_RICETTA = "CREATE TABLE " + TABLE_RICETTA + "(" + KEY_ID
            + " INTEGER PRIMARY KEY," + KEY_RICETTA + " TEXT, " + KEY_DESCRIPTION + " TEXT," + KEY_PEOPLE + " INTEGER," + KEY_CALORIES + " INTEGER)";
    private static final String CREATE_TABLE_RICETTA_FTS = "CREATE VIRTUAL TABLE " + TABLE_RICETTA_FTS + " USING fts3 ("
            + KEY_RICETTA + ")";



    // Tag table create statement
    private static final String CREATE_TABLE_TAG = "CREATE TABLE " + TABLE_TAG
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TAG_NAME + " TEXT," + KEY_RICETTA_ID + " INTEGER" + ")";

    private static final String CREATE_TABLE_TAG_FTS = "CREATE VIRTUAL TABLE " + TABLE_TAG_FTS + " USING fts3 ("
            + KEY_TAG_NAME + ")";

    // Immagine table
    private static final String CREATE_TABLE_IMMAGINE = "CREATE TABLE " + TABLE_IMMAGINE + "(" + KEY_ID
            + " INTEGER PRIMARY KEY, " + KEY_IMAGE_URI + " INTEGER," + KEY_RICETTA_ID + " INTEGER)";

    private static final String CREATE_TABLE_PASSO = "CREATE TABLE " + TABLE_PASSO + "(" + KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_PASSO_DESC + " TEXT," + KEY_PASSO_NUM + " INTEGER," + KEY_RICETTA_ID + " INTEGER," + KEY_PASSO_DONE + " INTEGER)";

    // Ingrediente table
    private static final String CREATE_TABLE_INGREDIENTE = "CREATE TABLE " + TABLE_INGREDIENTE + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_INGREDIENTE + " TEXT," + KEY_QUANTITA + " INTEGER," +
            KEY_RICETTA_ID + " INTEGER)";
    private static final String CREATE_TABLE_INGREDIENTE_FTS = "CREATE VIRTUAL TABLE " + TABLE_INGREDIENTE_FTS + " USING fts3 ("
            + KEY_INGREDIENTE + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_RICETTA);
        db.execSQL(CREATE_TABLE_RICETTA_FTS);
        db.execSQL(CREATE_TABLE_TAG);
        db.execSQL(CREATE_TABLE_TAG_FTS);
        db.execSQL(CREATE_TABLE_IMMAGINE);
        db.execSQL(CREATE_TABLE_PASSO);
        db.execSQL(CREATE_TABLE_INGREDIENTE);
        db.execSQL(CREATE_TABLE_INGREDIENTE_FTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RICETTA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RICETTA_FTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG_FTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMMAGINE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTE_FTS);



        // create new tables
        onCreate(db);
    }

    int createRicetta(Ricetta ricetta) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, ricetta.getId());

        values.put(KEY_RICETTA, ricetta.getName());
        values.put(KEY_DESCRIPTION, ricetta.getmDescription());
        values.put(KEY_PEOPLE, ricetta.getPeople());
        values.put(KEY_CALORIES, ricetta.getCalories());

        int ricetta_id = (int) db.insert(TABLE_RICETTA, null, values);
        ContentValues valueFTS = new ContentValues();
        valueFTS.put(KEY_RICETTA, ricetta.getName());
        valueFTS.put("docid", ricetta.getId());
        db.insert(TABLE_RICETTA_FTS, null, valueFTS);
        return ricetta_id;
    }

    int getMaxRicetta() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT MAX(" + KEY_ID + ") AS MASSIMO FROM " + TABLE_RICETTA;
        Cursor c = db.rawQuery(selectQuery, null);
        int max;
        if (c != null) {
            c.moveToFirst();
            max = c.getInt(c.getColumnIndex("MASSIMO"));
        } else
            max = 0;
        if (c != null)
            c.close();
        return max;

    }

    int getMaxTag() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT MAX(" + KEY_ID + ") AS MASSIMO FROM " + TABLE_TAG;
        Cursor c = db.rawQuery(selectQuery, null);
        int max;
        if (c != null) {
            c.moveToFirst();
            max = c.getInt(c.getColumnIndex("MASSIMO"));
            c.close();
        } else
            max = 0;
        return max;
    }

    int getMaxImmagine() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT MAX(" + KEY_ID + ") AS MASSIMO FROM " + TABLE_IMMAGINE;
        Cursor c = db.rawQuery(selectQuery, null);
        int max;
        if (c != null) {
            c.moveToFirst();
            max = c.getInt(c.getColumnIndex("MASSIMO"));
            c.close();
        } else
            max = 0;
        return max;
    }

    int getMaxPasso() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT MAX(" + KEY_ID + ") AS MASSIMO FROM " + TABLE_PASSO;
        Cursor c = db.rawQuery(selectQuery, null);
        int max;
        if (c != null) {
            c.moveToFirst();
            max = c.getInt(c.getColumnIndex("MASSIMO"));
            c.close();
        } else
            max = 0;
        return max;
    }

    int getMaxIngrediente() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT MAX(" + KEY_ID + ") AS MASSIMO FROM " + TABLE_INGREDIENTE;
        Cursor c = db.rawQuery(selectQuery, null);
        int max;
        if (c != null) {
            c.moveToFirst();
            max = c.getInt(c.getColumnIndex("MASSIMO"));
            c.close();
        } else
            max = 0;
        return max;
    }

    Ricetta getRicetta(long ricetta_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Ricetta ricetta = null;
        String selectQuery = "SELECT * FROM " + TABLE_RICETTA + " WHERE " + KEY_ID + "=" + ricetta_id;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null && c.moveToFirst()) {
            ricetta = new Ricetta(c.getString(c.getColumnIndex(KEY_RICETTA)), c.getInt(c.getColumnIndex(KEY_ID)), c.getString(c.getColumnIndex(KEY_DESCRIPTION)), c.getInt(c.getColumnIndex(KEY_PEOPLE)), c.getInt(c.getColumnIndex(KEY_CALORIES)));
            c.close();
        }
        return ricetta;
    }

    ArrayList<Ricetta> getAllRicette() {
        ArrayList<Ricetta> ricette = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_RICETTA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Ricetta ricetta = new Ricetta(c.getString(c.getColumnIndex(KEY_RICETTA)), c.getInt(c.getColumnIndex(KEY_ID)), c.getString(c.getColumnIndex(KEY_DESCRIPTION)), c.getInt(c.getColumnIndex(KEY_PEOPLE)), c.getInt(c.getColumnIndex(KEY_CALORIES)));
            ricette.add(ricetta);
            }
            while (c.moveToNext());
            c.close();
        }
        return ricette;
    }


    void updateRicetta(Ricetta ricetta) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RICETTA, ricetta.getName());
        values.put(KEY_DESCRIPTION, ricetta.getmDescription());
        values.put(KEY_PEOPLE, ricetta.getPeople());
        values.put(KEY_CALORIES, ricetta.getCalories());

        ContentValues valueFTS = new ContentValues();
        valueFTS.put(KEY_RICETTA, ricetta.getName());
        db.update(TABLE_RICETTA_FTS, valueFTS, "docid = ?", new String[]{String.valueOf(ricetta.getId())});
    }


    void deleteRicetta(long ricetta_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RICETTA_FTS, "docid = ?", new String[]{String.valueOf(ricetta_id)});
        db.delete(TABLE_RICETTA, KEY_ID + " = ?",
                new String[]{String.valueOf(ricetta_id)});
    }


    void createTag(Tag tag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, tag.getId());
        values.put(KEY_TAG_NAME, tag.getTagName());
        values.put(KEY_RICETTA_ID, tag.getRicettaId());
        // insert row
        db.insert(TABLE_TAG, null, values);
        ContentValues valueFTS = new ContentValues();
        valueFTS.put(KEY_TAG_NAME, tag.getTagName());
        db.insert(TABLE_TAG_FTS, null, valueFTS);
    }


    ArrayList<Tag> getTagsFromRicetta(Ricetta ricetta) {
        ArrayList<Tag> tags = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_TAG + " WHERE " + KEY_RICETTA_ID + "=" + ricetta.getId();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Tag t = new Tag(c.getInt(c.getColumnIndex(KEY_ID)), c.getString(c.getColumnIndex(KEY_TAG_NAME)), c.getInt(c.getColumnIndex(KEY_RICETTA_ID)));
                tags.add(t);
            } while (c.moveToNext());
            c.close();
        }
        return tags;
    }

    void updateTag(Tag tag) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TAG_NAME, tag.getTagName());

        // updating row
        ContentValues valueFTS = new ContentValues();
        valueFTS.put(KEY_TAG_NAME, tag.getTagName());
        db.update(TABLE_TAG_FTS, valueFTS, "docid = ?", new String[]{String.valueOf(tag.getId())});
    }

    void deleteTag(Tag tag) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TAG_FTS, "docid = ?", new String[]{String.valueOf(tag.getId())});
        db.delete(TABLE_TAG, KEY_ID + " = ?",
                new String[]{String.valueOf(tag.getId())});
    }


    void createPasso(Passo passo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, passo.getId());
        values.put(KEY_PASSO_DESC, passo.getDescription());
        values.put(KEY_PASSO_NUM, passo.getNumber());
        values.put(KEY_RICETTA_ID, passo.getRicetta_id());
        values.put(KEY_PASSO_DONE, passo.isDone());
        db.insert(TABLE_PASSO, null, values);
    }


    ArrayList<Passo> getPassiFromRicetta(Ricetta ricetta) {
        ArrayList<Passo> passi = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_PASSO + " WHERE " + KEY_RICETTA_ID + "=" + ricetta.getId();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Passo passo = new Passo(c.getInt(c.getColumnIndex(KEY_ID)), c.getInt(c.getColumnIndex(KEY_PASSO_NUM)), c.getInt(c.getColumnIndex(KEY_RICETTA_ID)), c.getString(c.getColumnIndex(KEY_PASSO_DESC)), c.getInt(c.getColumnIndex(KEY_PASSO_DONE)));
                passi.add(passo);
            } while (c.moveToNext());
            c.close();
        }
        return passi;
    }


    void updatePasso(Passo passo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, passo.getId());
        values.put(KEY_PASSO_DESC, passo.getDescription());
        values.put(KEY_PASSO_NUM, passo.getNumber());
        values.put(KEY_RICETTA_ID, passo.getRicetta_id());
        values.put(KEY_PASSO_DONE, passo.isDone());
        // updating row
        db.update(TABLE_PASSO, values, KEY_ID + " = ?",
                new String[]{String.valueOf(passo.getId())});
    }


    void deletePasso(long passo_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PASSO, KEY_ID + " = ?",
                new String[]{String.valueOf(passo_id)});
    }


    void createIngrediente(Ingrediente ingrediente) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, ingrediente.getId());
        values.put(KEY_INGREDIENTE, ingrediente.getmName());
        values.put(KEY_QUANTITA, ingrediente.getGrams());
        values.put(KEY_RICETTA_ID, ingrediente.getRicetta_id());
        db.insert(TABLE_INGREDIENTE, null, values);
        ContentValues valueFTS = new ContentValues();
        valueFTS.put(KEY_INGREDIENTE, ingrediente.getmName());
        db.insert(TABLE_INGREDIENTE_FTS, null, valueFTS);
    }


    ArrayList<Ingrediente> getIngredientiFromRicetta(Ricetta ricetta) {
        ArrayList<Ingrediente> ingredienti = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_INGREDIENTE + " WHERE " + KEY_RICETTA_ID + "=" + ricetta.getId();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Ingrediente ingrediente = new Ingrediente(c.getInt(c.getColumnIndex(KEY_ID)), c.getString(c.getColumnIndex(KEY_INGREDIENTE)), c.getInt(c.getColumnIndex(KEY_QUANTITA)), c.getInt(c.getColumnIndex(KEY_RICETTA_ID)));
                ingredienti.add(ingrediente);
            } while (c.moveToNext());
            c.close();
        }
        return ingredienti;
    }


    void updateIngrediente(Ingrediente ingrediente) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, ingrediente.getId());
        values.put(KEY_INGREDIENTE, ingrediente.getmName());
        values.put(KEY_QUANTITA, ingrediente.getGrams());
        values.put(KEY_RICETTA_ID, ingrediente.getRicetta_id());

        // updating row

        ContentValues valueFTS = new ContentValues();
        valueFTS.put(KEY_INGREDIENTE, ingrediente.getName());
        db.update(TABLE_INGREDIENTE_FTS, valueFTS, "docid = ?", new String[]{String.valueOf(ingrediente.getId())});

        db.update(TABLE_INGREDIENTE, values, KEY_ID + " = ?",
                new String[]{String.valueOf(ingrediente.getId())});
    }


    void deleteIngrediente(long ingrediente_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INGREDIENTE_FTS, "docid = ?", new String[]{String.valueOf(ingrediente_id)});
        db.delete(TABLE_INGREDIENTE, KEY_ID + " = ?",
                new String[]{String.valueOf(ingrediente_id)});
    }


    void createImmagine(Immagine img) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, img.getId());
        values.put(KEY_IMAGE_URI, img.getPhoto_uri().toString());
        values.put(KEY_RICETTA_ID, img.getRicetta_id());
        db.insert(TABLE_IMMAGINE, null, values);
    }

    ArrayList<Immagine> getImmaginiFromRicetta(Ricetta ricetta) {
        ArrayList<Immagine> images = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_IMMAGINE + " WHERE " + KEY_RICETTA_ID + "=" + ricetta.getId();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Immagine img = new Immagine(c.getInt(c.getColumnIndex(KEY_ID)), Uri.parse(c.getString(c.getColumnIndex(KEY_IMAGE_URI))), c.getInt(c.getColumnIndex(KEY_RICETTA_ID)));
                images.add(img);
            } while (c.moveToNext());
            c.close();
        }
        return images;
    }


    void deleteImmagine(long immagine_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_IMMAGINE, KEY_ID + " = ?",
                new String[]{String.valueOf(immagine_id)});
    }

    ArrayList<Ricetta> searchRicette(String text) {
        ArrayList<Ricetta> ricette = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query, query1, query2, query3;
        if (text.length() != 0) {
            query1 = "SELECT DISTINCT " + TABLE_RICETTA + "." + KEY_ID + "," + TABLE_RICETTA + "." + KEY_RICETTA + "," + TABLE_RICETTA + "." + KEY_DESCRIPTION + "," + TABLE_RICETTA + "." + KEY_PEOPLE + "," + KEY_CALORIES + " FROM " + TABLE_RICETTA + "," + TABLE_RICETTA_FTS + " WHERE " + TABLE_RICETTA + "." + KEY_ID + "=" + TABLE_RICETTA_FTS + "." + "docid AND " + TABLE_RICETTA_FTS + " MATCH '" + text + "*'";
            query2 = "SELECT DISTINCT " + TABLE_RICETTA + "." + KEY_ID + "," + TABLE_RICETTA + "." + KEY_RICETTA + "," + TABLE_RICETTA + "." + KEY_DESCRIPTION + "," + TABLE_RICETTA + "." + KEY_PEOPLE + "," + KEY_CALORIES + " FROM " + TABLE_RICETTA + "," + TABLE_TAG + "," + TABLE_TAG_FTS + " WHERE " + TABLE_TAG + "." + KEY_ID + "=" + TABLE_TAG_FTS + ".docid AND " + TABLE_TAG + "." + KEY_RICETTA_ID + "=" + TABLE_RICETTA + "." + KEY_ID + " AND " + TABLE_TAG_FTS + " MATCH '" + text + "*'";
            query3 = "SELECT DISTINCT " + TABLE_RICETTA + "." + KEY_ID + "," + TABLE_RICETTA + "." + KEY_RICETTA + "," + TABLE_RICETTA + "." + KEY_DESCRIPTION + "," + TABLE_RICETTA + "." + KEY_PEOPLE + "," + KEY_CALORIES + " FROM " + TABLE_RICETTA + "," + TABLE_INGREDIENTE_FTS + "," + TABLE_INGREDIENTE + " WHERE " + TABLE_INGREDIENTE + "." + KEY_ID + "=" + TABLE_INGREDIENTE_FTS + ".docid AND " + TABLE_INGREDIENTE + "." + KEY_RICETTA_ID + "=" + TABLE_RICETTA + "." + KEY_ID + " AND " + TABLE_INGREDIENTE_FTS + " MATCH '" + text + "*'";
            query = query1 + " UNION " + query2 + " UNION " + query3 + ";";
        } else
            query = "SELECT * FROM " + TABLE_RICETTA;
        Cursor c = db.rawQuery(query, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Ricetta ricetta = new Ricetta(c.getString(c.getColumnIndex(KEY_RICETTA)), c.getInt(c.getColumnIndex(KEY_ID)), c.getString(c.getColumnIndex(KEY_DESCRIPTION)), c.getInt(c.getColumnIndex(KEY_PEOPLE)), c.getInt(c.getColumnIndex(KEY_CALORIES)));
                ricette.add(ricetta);
            } while (c.moveToNext());
            c.close();
        }

        db.execSQL("DROP VIEW IF EXISTS T1");
        db.execSQL("DROP VIEW IF EXISTS T2");
        db.execSQL("DROP VIEW IF EXISTS T3");
        return ricette;
    }

    void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}