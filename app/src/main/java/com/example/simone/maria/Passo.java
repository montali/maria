package com.example.simone.maria;

import java.io.Serializable;

class Passo implements Serializable {
    private Integer id;
    private final int done;
    private final Integer number;
    private final int ricetta_id;
    private String description;

    Passo(Integer id, Integer num, int ricetta, String desc, int done) {
        this.id = id;
        number = num;
        ricetta_id = ricetta;
        description = desc;
        this.done = done;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Integer getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }

    int isDone() {
        return done;
    }

    int getRicetta_id() {
        return ricetta_id;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

}
