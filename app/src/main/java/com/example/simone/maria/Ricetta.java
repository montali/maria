package com.example.simone.maria;

import java.util.ArrayList;

public class Ricetta {


    private int id;
    private String mName;
    private int imageId=R.drawable.pera;
    private ArrayList<Ingrediente> ingredienti;


    private String mDescription;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getmDescription() {
        return mDescription;
    }

    public int getDrawableId(){
        return imageId;
    }


    public ArrayList<Ingrediente> getIngredienti() {
        return ingredienti;
    }


    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setIngredienti(ArrayList<Ingrediente> ingredienti) {
        this.ingredienti = ingredienti;
    }


    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public Ricetta(String name, int id, String description, ArrayList<Ingrediente> ingred) {
        mName=name;
        imageId=id;
        mDescription=description;
        ingredienti=ingred;
    }
    public Ricetta (int id, String name, String description){
        mName=name;
        mDescription=description;
        this.id=id;
    }
    public String getName(){
        return mName;
    }

    private static int lastRicettaID = 0;
    }


