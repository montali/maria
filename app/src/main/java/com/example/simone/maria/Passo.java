package com.example.simone.maria;

import java.io.Serializable;

public class Passo  implements Serializable {
    private Integer id;
    private int done;
    private int number;
    private int ricetta_id;
    private String description;

    public Passo(Integer id, int num, int ricetta, String desc, int done) {
        this.id = id;
        number = num;
        ricetta_id = ricetta;
        description = desc;
        this.done = done;
    }

    public Integer getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }

    public int isDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getRicetta_id() {
        return ricetta_id;
    }

    public void setRicetta_id(int ricetta_id) {
        this.ricetta_id = ricetta_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
