package com.example.simone.maria;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;


import java.io.Serializable;
import java.util.ArrayList;

public class Ricetta implements Parcelable {


    private int id;
    private String mName;
    private int imageId=R.drawable.pera;
    private ArrayList<Ingrediente> ingredienti;

    private ArrayList<Passo> passi;

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

    public ArrayList<Passo> getPassi() {
        return passi;
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

    public void setPassi(ArrayList<Passo> passi) {
        this.passi = passi;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public Ricetta(String name, int id, String description, ArrayList<Ingrediente> ingred, ArrayList<Passo> pas){
        mName=name;
        imageId=id;
        mDescription=description;
        ingredienti=ingred;
        passi=pas;
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

    public static ArrayList<Ricetta> createRicettaList(int numRicette){
        ArrayList<Ricetta> ricette = new ArrayList<Ricetta>();
        ArrayList<Ingrediente> ingredienti=new ArrayList<Ingrediente>();
        ArrayList<Passo> passi = new ArrayList<Passo>();
        ingredienti.add(new Ingrediente("Farina",200));
        ingredienti.add(new Ingrediente("Ketch",300));
        ingredienti.add(new Ingrediente("Farina",200));
        ingredienti.add(new Ingrediente("Ketch",300));
        ingredienti.add(new Ingrediente("Farina",200));
        ingredienti.add(new Ingrediente("Ketch",300));
        passi.add(new Passo(1,"Mescola un quel"));
        passi.add(new Passo(2,"Cuoci un quel"));
        passi.add(new Passo(3,"Mangiala e daje"));

        for(int i=1;i<=numRicette;i++) {
            ricette.add(new Ricetta("Nome: " + ++lastRicettaID,R.drawable.pera,"Grande piatto dello chef simmy",ingredienti,passi));
        }
        return ricette;
        }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mDescription);
        dest.writeInt(this.imageId);
        dest.writeList(ingredienti);
        dest.writeList(passi);
    }

    protected Ricetta(Parcel in) {
        this.mName = in.readString();
        this.mDescription = in.readString();
        this.imageId = in.readInt();
        this.ingredienti=in.readArrayList(null);
        this.passi=in.readArrayList(null);
    }

    public static final Parcelable.Creator<Ricetta> CREATOR = new Parcelable.Creator<Ricetta>() {
        @Override
        public Ricetta createFromParcel(Parcel source) {
            return new Ricetta(source);
        }

        @Override
        public Ricetta[] newArray(int size) {
            return new Ricetta[size];
        }
    };
    }


