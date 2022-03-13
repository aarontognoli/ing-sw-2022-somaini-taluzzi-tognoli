package it.polimi.ingsw.pawn;

import it.polimi.ingsw.enums.Color;


public class Student {
    // ID to define
    // TODO
    // Figure out if ID is useful or not
    Integer id;
    Color color;

    public Student(Color color, Integer id) {
        this.color = color;
        this.id = id;
    }

    public Color getColor() {
        return color;
    }

    public Integer getID() {
        return id;
    }

}
