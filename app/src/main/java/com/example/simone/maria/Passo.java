package com.example.simone.maria;

import java.io.Serializable;

public class Passo  implements Serializable {
    private int number;
    private int ricetta_id;
    private String description;


    public int getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }

    public Passo(int num, String desc){
        number=num;
        description=desc;
    }

}
