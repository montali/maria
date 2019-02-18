package com.example.simone.maria;

public class Tag {
    Integer id;
    private String tagName;
    private int ricettaId;

    public Tag(Integer id, String tagName, int ricettaId) {
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

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getRicettaId() {
        return ricettaId;
    }

    public void setRicettaId(int ricettaId) {
        this.ricettaId = ricettaId;
    }
}

