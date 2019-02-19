package com.example.simone.maria;

public class Tag {
    Integer id;
    private String tagName;
    private int ricettaId;

    Tag(Integer id, String tagName, int ricettaId) {
        this.id = id;
        this.tagName = tagName;
        this.ricettaId = ricettaId;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    String getTagName() {
        return tagName;
    }

    void setTagName(String tagName) {
        this.tagName = tagName;
    }

    int getRicettaId() {
        return ricettaId;
    }

    void setRicettaId(int ricettaId) {
        this.ricettaId = ricettaId;
    }
}

