package it.polimi.ingsw.cards.characters.WineCharacter;

import it.polimi.ingsw.enums.Color;

public class WineCharacterArgument {
    final private int targetIslandIndex;
    final private Color targetStudentColor;

    public WineCharacterArgument(int targetIslandIndex, Color targetStudentColor) {
        this.targetIslandIndex = targetIslandIndex;
        this.targetStudentColor = targetStudentColor;
    }

    public int getTargetIslandIndex() {
        return targetIslandIndex;
    }

    public Color getTargetStudentColor() {
        return targetStudentColor;
    }
}
