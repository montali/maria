package com.example.simone.maria;

import android.net.Uri;

public class Immagine {
    private int id;
    private Uri photo_uri;
    private int ricetta_id;


    // Getters and setters

    public Immagine(int id, Uri photo_uri, int ricetta_id) {
        this.id = id;
        this.photo_uri = photo_uri;
        this.ricetta_id = ricetta_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Uri getPhoto_uri() {
        return photo_uri;
    }

    public void setPhoto_uri(Uri photo_uri) {
        this.photo_uri = photo_uri;
    }

    public int getRicetta_id() {
        return ricetta_id;
    }

    // Constructors

    public void setRicetta_id(int ricetta_id) {
        this.ricetta_id = ricetta_id;
    }
}
