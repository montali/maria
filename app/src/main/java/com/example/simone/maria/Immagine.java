package com.example.simone.maria;

import android.net.Uri;

public class Immagine {
    private int id;
    private Uri photo_uri;
    private int ricetta_id;


    // Getters and setters

    Immagine(int id, Uri photo_uri, int ricetta_id) {
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

    Uri getPhoto_uri() {
        return photo_uri;
    }


    int getRicetta_id() {
        return ricetta_id;
    }

}
