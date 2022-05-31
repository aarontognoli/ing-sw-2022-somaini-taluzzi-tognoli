package it.polimi.ingsw.cards.characters.JokerCharacter;

import it.polimi.ingsw.enums.Color;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class JokerCharacterArgument implements Serializable {

    final private List<Color> colorRemoveJoker;
    final private List<Color> colorRemoveBoard;

    public JokerCharacterArgument(
            List<Color> colorRemoveJoker,
            List<Color> colorRemoveBoard) {
        this.colorRemoveJoker = colorRemoveJoker;
        this.colorRemoveBoard = colorRemoveBoard;
    }

    public List<Color> getColorRemoveBoard() {
        return colorRemoveBoard;
    }

    public List<Color> getColorRemoveJoker() {
        return colorRemoveJoker;
    }
}
