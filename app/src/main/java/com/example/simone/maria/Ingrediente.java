package com.example.simone.maria;

import java.io.Serializable;
import java.util.ArrayList;

public class Ingrediente implements Serializable {
    private String mName;
    private int imageId=R.drawable.pera;
    private int grams;

    public int getGrams() {
        return grams;
    }


    public int getDrawableId(){
        return imageId;
    }

    public Ingrediente(String name, int grammi){
        mName=name;
        grams=grammi;
    }
    public String getName(){
        return mName;
    }

    private static int lastIngredienteID = 0;

    public static ArrayList<Ingrediente> createRicettaList(int numRicette){
        ArrayList<Ingrediente> ingredienti = new ArrayList<Ingrediente>();
        for(int i=1;i<=numRicette;i++) {
            ingredienti.add(new Ingrediente("Nome: " + ++lastIngredienteID,200));
        }
        return ingredienti;
    }
}

