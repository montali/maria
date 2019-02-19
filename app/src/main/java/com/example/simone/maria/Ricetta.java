package com.example.simone.maria;


class Ricetta {


    private final int id;
    private String mName;
    private Integer people;
    private Integer calories;

    public Ricetta(String name, int id, String description, Integer people) {
        mName = name;
        mDescription = description;
        this.id = id;
        this.people = people;
        this.calories = null;
    }

    public Ricetta(String name, int id, String description, Integer people, Integer calories) {
        mName = name;
        mDescription = description;
        this.id = id;
        this.people = people;
        this.calories = calories;
    }

    public Ricetta(Ricetta ricetta) {
        this.mName = ricetta.getName();
        this.mDescription = ricetta.getmDescription();
        this.id = ricetta.getId();
        this.people = ricetta.getPeople();
        this.calories = ricetta.getCalories();
    }

    public Integer getPeople() {
        return people;
    }

    private String mDescription;

    public int getId() {
        return id;
    }

    String getmDescription() {
        return mDescription;
    }

    void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    void setmName(String mName) {
        this.mName = mName;
    }

    public void setPeople(Integer people) {
        this.people = people;
    }

    Integer getCalories() {
        if (calories != null)
            return calories;
        else
            return 0;
    }

    void setCalories(Integer calories) {
        this.calories = calories;
    }

    public String getName(){
        return mName;
    }

    }


