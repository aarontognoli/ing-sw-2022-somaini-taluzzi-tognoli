package it.polimi.ingsw.pawn;

import it.polimi.ingsw.enums.Color;

import java.util.Objects;


public class Student {
    private final Integer id;
    private final Color color;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id.equals(student.id) && color == student.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, color);
    }
}
