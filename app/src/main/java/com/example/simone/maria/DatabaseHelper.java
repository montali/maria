package com.example.simone.maria;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ricettaManager";

    // Table Names
    private static final String TABLE_RICETTA = "ricetta";
    private static final String TABLE_TAG = "tag";
    private static final String TABLE_RICETTA_TAG = "ricetta_tag";
    private static final String TABLE_IMMAGINE = "immagine";
    private static final String TABLE_PASSO = "passo";
    private static final String TABLE_INGREDIENTE = "ingrediente";


    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_RICETTA_ID = "ricetta_id";

    // RICETTA Table - column nmaes
    private static final String KEY_RICETTA = "nome";
    private static final String KEY_DESCRIPTION = "descrizione";

    // TAG Table - column names
    private static final String KEY_TAG_NAME = "tag_name";

    // RICETTA_TAG Table - column names
    private static final String KEY_TAG_ID = "tag_id";

    // IMMAGINE Table - column names
    private static final String KEY_IMAGE_ID = "image_id";

    // PASSO Table - column names
    private static final String KEY_PASSO_NUM = "passo_num";
    private static final String KEY_PASSO_DONE = "passo_done";
    private static final String KEY_PASSO_DESC = "passo_desc";

    // INGREDIENTE Table - column names
    private static final String KEY_INGREDIENTE = "ingrediente";
    private static final String KEY_QUANTITA = "quantita";

    
    
    // Table Create Statements
    // Todo table create statement

    private static final String CREATE_TABLE_RICETTA = "CREATE TABLE " + TABLE_RICETTA + "(" + KEY_ID
            + " INTEGER PRIMARY KEY," + KEY_RICETTA + " TEXT, " + KEY_DESCRIPTION + " TEXT)";

    // Tag table create statement
    private static final String CREATE_TABLE_TAG = "CREATE TABLE " + TABLE_TAG
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TAG_NAME + " TEXT" + ")";


    // todo_tag table create statement
    private static final String CREATE_TABLE_RICETTA_TAG = "CREATE TABLE "
            + TABLE_RICETTA_TAG + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_RICETTA_ID + " INTEGER," + KEY_TAG_ID + " INTEGER," + ")";


    private static final String CREATE_TABLE_IMMAGINE = "CREATE TABLE " + TABLE_IMMAGINE + "(" + KEY_ID
            + " INTEGER PRIMARY KEY, " + KEY_IMAGE_ID + " TEXT," + KEY_RICETTA_ID + " INTEGER)";

    private static final String CREATE_TABLE_PASSO = "CREATE TABLE " + TABLE_PASSO + "(" + KEY_ID + "INTEGER PRIMARY KEY," +
            KEY_PASSO_DESC + " TEXT," + KEY_PASSO_NUM + " INTEGER," + KEY_PASSO_DONE + " BIT)";


    private static final String CREATE_TABLE_INGREDIENTE = "CREATE TABLE " + TABLE_INGREDIENTE + "("
            + KEY_ID + " INTEGER PRIMARY KEY," +  KEY_INGREDIENTE + " TEXT," + KEY_QUANTITA + " INTEGER," +
            KEY_RICETTA_ID + " INTEGER)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_RICETTA);
        db.execSQL(CREATE_TABLE_TAG);
        db.execSQL(CREATE_TABLE_RICETTA_TAG);
        db.execSQL(CREATE_TABLE_IMMAGINE);
        db.execSQL(CREATE_TABLE_PASSO);
        db.execSQL(CREATE_TABLE_INGREDIENTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RICETTA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RICETTA_TAG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMMAGINE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTE);


        // create new tables
        onCreate(db);
    }

    public long createRicetta(Ricetta ricetta, long[] tag_ids){
        SQLiteDatabase db =this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RICETTA, ricetta.getName());
        values.put(KEY_DESCRIPTION,ricetta.getmDescription()):

        long ricetta_id = db.insert(TABLE_RICETTA,null,values);

        for (long tag_id : tag_ids){
            createRicettaTag(ricetta_id,tag_id);
        }
        return ricetta_id;
    }

    public Ricetta getRicetta(long ricetta_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM" + TABLE_RICETTA + " WHERE " + KEY_ID + "=" + ricetta_id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery,null);
        if (c!=null)
            c.moveToFirst();
        Ricetta ricetta = new Ricetta(c.getInt(c.getColumnIndex(KEY_ID)),c.getString(c.getColumnIndex(KEY_RICETTA)),c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
        return ricetta;
    }
    public List<Ricetta> getAllRicette() {
        List<Ricetta> ricette = new ArrayList<Ricetta>();
        String selectQuery = "SELECT * FROM " + TABLE_RICETTA;
        Log.e(LOG,selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c= db.rawQuery(selectQuery,null);
        if(c.moveToFirst()){
            Ricetta ricetta = new Ricetta(c.getInt(c.getColumnIndex(KEY_ID)),c.getString(c.getColumnIndex(KEY_RICETTA)),c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
            ricette.add(ricetta);
        }while(c.moveToNext());
        return ricette;
    }

    public List<Ricetta> getAllRicetteByTag(String tag_name) {
        List<Ricetta> ricette = new ArrayList<Ricetta>();

        String selectQuery = "SELECT  * FROM " + TABLE_RICETTA + " td, "
                + TABLE_TAG + " tg, " + TABLE_RICETTA_TAG + " tt WHERE tg."
                + KEY_TAG_NAME + " = '" + tag_name + "'" + " AND tg." + KEY_ID
                + " = " + "tt." + KEY_TAG_ID + " AND td." + KEY_ID + " = "
                + "tt." + KEY_RICETTA_ID;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Ricetta ricetta = new Ricetta(c.getInt(c.getColumnIndex(KEY_ID)),c.getString(c.getColumnIndex(KEY_RICETTA)),c.getString(c.getColumnIndex(KEY_DESCRIPTION)));

                // adding to todo list
                ricette.add(ricetta);
            } while (c.moveToNext());
        }

        return ricette;
    }
    public int updateRicetta(Ricetta ricetta) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RICETTA, ricetta.getName());
        values.put(KEY_DESCRIPTION, ricetta.getmDescription());

        // updating row
        return db.update(TABLE_RICETTA, values, KEY_ID + " = ?",
                new String[] { String.valueOf(ricetta.getId()) });
    }


    public void deleteRicetta(long ricetta_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RICETTA, KEY_ID + " = ?",
                new String[] { String.valueOf(ricetta_id) });
    }





    public long createTag(Tag tag) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TAG_NAME, tag.getTagName());

        // insert row
        long tag_id = db.insert(TABLE_TAG, null, values);

        return tag_id;
    }
    public List<Tag> getAllTags(){
        List<Tag> tags = new ArrayList<Tag>();
        String selectQuery = "SELECT * FROM " + TABLE_TAG;
        Log.e(LOG,selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        if (c.moveToFirst()){
            do{
                Tag t = new Tag (c.getInt(c.getColumnIndex(KEY_ID)),c.getString(c.getColumnIndex(KEY_TAG_NAME)));
                tags.add(t);
            }while (c.moveToNext());
        }
        return tags;

    }
// Sono arrivatyo al punto 9 non incluso, manca un treno di roba
public int updateTag(Tag tag) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_TAG_NAME, tag.getTagName());

    // updating row
    return db.update(TABLE_TAG, values, KEY_ID + " = ?",
            new String[] { String.valueOf(tag.getId()) });
}
    public void deleteTag(Tag tag) {
        SQLiteDatabase db = this.getWritableDatabase();

        // before deleting tag
        // check if todos under this tag should also be deleted

        // now delete the tag
        db.delete(TABLE_TAG, KEY_ID + " = ?",
                new String[] { String.valueOf(tag.getId()) });
    }

    public long createRicettaTag (long ricetta_id,long tag_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_RICETTA_ID,ricetta_id);
        values.put(KEY_TAG_ID,tag_id);
        long id = db.insert(TABLE_RICETTA_TAG,null,values);
        return id;

    }

    public int updateRicettaTag(long id, long tag_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TAG_ID, tag_id);

        // updating row
        return db.update(TABLE_RICETTA, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    public long createPasso(Passo passo, long ricetta_id)

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }


}