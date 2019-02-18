package com.example.simone.maria;

import java.util.ArrayList;

public class Ricetta {


    private int id;
    private String mName;
    private int imageId=R.drawable.pera;
    private ArrayList<Ingrediente> ingredienti;
    private Integer people;
    private Integer calories;

    public Ricetta(String name, int id, String description, Integer people) {
        mName = name;
        mDescription = description;
        this.id = id;
        this.people = people;
        this.calories = null;
    }

    public Ricetta(String name, int id, String description, Integer people, Integer calories) {
        mName = name;
        mDescription = description;
        this.id = id;
        this.people = people;
        this.calories = calories;
    }

    public Ricetta(Ricetta ricetta) {
        this.mName = ricetta.getName();
        this.mDescription = ricetta.getmDescription();
        this.id = ricetta.getId();
        this.people = ricetta.getPeople();
        this.calories = ricetta.getCalories();
    }

    public Integer getPeople() {
        return people;
    }

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

    public void setPeople(Integer people) {
        this.people = people;
    }

    public Integer getCalories() {
        if (calories != null)
            return calories;
        else
            return 0;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public String getName(){
        return mName;
    }

    }


