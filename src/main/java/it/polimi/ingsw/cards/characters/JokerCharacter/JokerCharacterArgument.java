package it.polimi.ingsw.cards.characters.JokerCharacter;

import java.util.List;

public class JokerCharacterArgument {

    private List<Integer> studentsIDToRemoveFromJoker;
    private List<Integer> studentsIDToRemoveFromPlayerBoard;

    public JokerCharacterArgument(
            List<Integer> studentsIDToRemoveFromJoker,
            List<Integer> studentsIDToRemoveFromPlayerBoard) {
        this.studentsIDToRemoveFromJoker = studentsIDToRemoveFromJoker;
        this.studentsIDToRemoveFromJoker = studentsIDToRemoveFromJoker;
    }

    public List<Integer> getStudentsIDToRemoveFromPlayerBoard() {
        return studentsIDToRemoveFromPlayerBoard;
    }

    public List<Integer> getStudentsIDToRemoveFromJoker() {
        return studentsIDToRemoveFromJoker;
    }
}
