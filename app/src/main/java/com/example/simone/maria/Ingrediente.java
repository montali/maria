package com.example.simone.maria;

import java.io.Serializable;

public class Ingrediente implements Serializable {
    private String mName;
    private Integer id;
    private Integer grams;
    private int ricetta_id;

    Ingrediente(Integer id, String mName, Integer grams, int ricetta_id) {
        this.id = id;
        this.mName = mName;
        this.grams = grams;
        this.ricetta_id = ricetta_id;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    String getmName() {
        return mName;
    }

    void setmName(String mName) {
        this.mName = mName;
    }

    int getRicetta_id() {
        return ricetta_id;
    }

    void setRicetta_id(int ricetta_id) {
        this.ricetta_id = ricetta_id;
    }

    Integer getGrams() {
        return grams;
    }

    public String getName(){
        return mName;
    }

    void setGrams(Integer grams) {
        this.grams = grams;
    }

}

