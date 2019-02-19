package com.example.simone.maria;

import android.net.Uri;

class Immagine {
    private final Integer id;
    private final Uri photo_uri;
    private final int ricetta_id;


    // Getters and setters

    Immagine(Integer id, Uri photo_uri, int ricetta_id) {
        this.id = id;
        this.photo_uri = photo_uri;
        this.ricetta_id = ricetta_id;
    }

    public Integer getId() {
        return id;
    }

    Uri getPhoto_uri() {
        return photo_uri;
    }


    int getRicetta_id() {
        return ricetta_id;
    }

}
