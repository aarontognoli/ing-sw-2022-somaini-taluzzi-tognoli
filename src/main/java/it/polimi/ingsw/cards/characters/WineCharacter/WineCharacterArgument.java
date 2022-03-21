package it.polimi.ingsw.cards.characters.WineCharacter;

import it.polimi.ingsw.places.Island;

public class WineCharacterArgument {
    private Island targetIsland;
    private int targetStudentId;

    public WineCharacterArgument(Island targetIsland, int targetStudentId) {
        this.targetIsland = targetIsland;
        this.targetStudentId = targetStudentId;
    }

    public Island getTargetIsland() {
        return targetIsland;
    }

    public int getTargetStudentId() {
        return targetStudentId;
    }
}
