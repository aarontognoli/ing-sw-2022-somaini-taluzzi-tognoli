package it.polimi.ingsw.cards.characters.BardCharacter;

import it.polimi.ingsw.enums.Color;

import java.io.Serializable;
import java.util.List;

public class BardCharacterArgument implements Serializable {
    private List<Color> studentsColorToExchangeEntrance;
    private List<Color> studentsColorToExchangeDiningRoom;

    public BardCharacterArgument(
            List<Color> studentsColorToExchangeEntrance,
            List<Color> studentsColorToExchangeDiningRoom) {
        this.studentsColorToExchangeEntrance = studentsColorToExchangeEntrance;
        this.studentsColorToExchangeDiningRoom = studentsColorToExchangeDiningRoom;
    }

    public List<Color> getStudentsColorToExchangeEntrance() {
        return studentsColorToExchangeEntrance;
    }

    public List<Color> getStudentsColorToExchangeDiningRoom() {
        return studentsColorToExchangeDiningRoom;
    }
}
