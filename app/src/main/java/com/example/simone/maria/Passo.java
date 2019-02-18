package com.example.simone.maria;

import java.io.Serializable;

public class Passo  implements Serializable {
    private Integer id;
    private int done;
    private Integer number;
    private int ricetta_id;
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

    void setDone(int done) {
        this.done = done;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    int getRicetta_id() {
        return ricetta_id;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

}
