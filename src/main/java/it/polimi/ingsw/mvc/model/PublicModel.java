package it.polimi.ingsw.mvc.model;

import java.util.List;

import it.polimi.ingsw.cards.Deck;
import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.DeckName;
import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;
import it.polimi.ingsw.player.Player;
import it.polimi.ingsw.pawn.MotherNature;

class PlayerAlreadyChosenDeckException extends Exception {
    public PlayerAlreadyChosenDeckException(String playerName) {
        super(playerName + " has already chosen a deck.");
    }
}

class TooMuchStepsException extends Exception {
    public TooMuchStepsException(int maxSteps, int chosenSteps) {
        super("Trying to move mother nature for " + chosenSteps + " steps, maximum is " + maxSteps);
    }
}

public class PublicModel {
    final Model fatherModel;

    PublicModel(Model fatherModel) {
        this.fatherModel = fatherModel;
    }

    public void playAssistant(AssistantCard assistantCard) {
        fatherModel.currentPlayer.setCurrentAssistantCard(assistantCard);
    }

    public void drawStudentsIntoEntrance(int cloudIndex) {
        List<Student> studentsFromCloud = fatherModel.clouds.get(cloudIndex).getStudents();
        fatherModel.currentPlayer.getBoard().addStudentsToEntrance(studentsFromCloud);
    }

    public void endTurn() {
        // TODO
    }

    public Island getMotherNatureIsland() {
        return fatherModel.motherNature.getPosition();
    }

    public void setPlayersCount(int playersCount) {
        fatherModel.totalPlayerCount = playersCount;
    }

    public void playerJoin(String nickname) throws Exception {
        if (fatherModel.players.size() == fatherModel.totalPlayerCount) {
            throw new Exception("Player pool full.");
        }

        fatherModel.players.add(new Player(nickname));
    }

    public void setGameMode(GameMode gameMode) {
        fatherModel.gameMode = gameMode;
    }

    // Called during game preparation
    public void placeMotherNature(int islandIndex) throws Exception {
        if (fatherModel.motherNature != null) {
            throw new Exception("Mother Nature already chosen");
        }

        fatherModel.motherNature = new MotherNature(fatherModel.islands.get(islandIndex));
    }

    public void chooseDeck(int playerIndex, int deckNameOrdinal) throws Exception {
        Player targetPlayer = fatherModel.players.get(playerIndex);

        if (targetPlayer.getDeck() != null) {
            throw new PlayerAlreadyChosenDeckException(targetPlayer.getNickname());
        }

        targetPlayer.setDeck(new Deck(DeckName.values()[deckNameOrdinal]));
    }

    public void moveMotherNature(int steps) throws TooMuchStepsException {
        // TODO: PostManCharacter effect (needs PostManCharacter implementation)
        int maxSteps = getCurrentPlayer().getCurrentAssistantCard().getMaxMotherNatureMovementValue();

        if (steps > maxSteps) {
            throw new TooMuchStepsException(maxSteps, steps);
        }

        int indexPreviousMotherNatureIsland = fatherModel.islands.indexOf(fatherModel.motherNature.getPosition());

        int finalIslandIndex = (indexPreviousMotherNatureIsland + steps) % fatherModel.islands.size();

        Island destination = fatherModel.islands.get(finalIslandIndex);
        fatherModel.motherNature.move(destination);
    }

    public void moveStudentToIsland(Color studentColor, int islandIndex) throws Exception {
        // Move a student (of this color) from the current player's entrance
        // into the correct island
        Student selectedStudent = fatherModel.privateModel.getStudentInEntrance(studentColor);
        fatherModel.privateModel.addStudentToIsland(selectedStudent, fatherModel.islands.get(islandIndex));
    }

    public void moveStudentToDiningRoom(Color studentColor) throws Exception {
        // Move a student (of this color) from the current player's entrance
        // into their dining room
        Student selectedStudent = fatherModel.privateModel.getStudentInEntrance(studentColor);
        fatherModel.privateModel.addStudentToDiningRoom(selectedStudent, fatherModel.currentPlayer.getBoard());

        // (Expert Only) If we reached one of the critical placeholder in the dining
        // room,
        // Assign a new Coin to current player
        if (fatherModel.gameMode.equals(GameMode.EXPERT_MODE)) {
            List<List<Student>> currentDiningRoom = fatherModel.currentPlayer.getBoard().getDiningRoom();
            int indexOfSelectedStudent = currentDiningRoom.get(selectedStudent.getColor().ordinal())
                    .indexOf(selectedStudent);
            if (indexOfSelectedStudent == 2 || indexOfSelectedStudent == 5 || indexOfSelectedStudent == 8) {
                fatherModel.privateModel.rewardCoin();
            }
        }

    }

    public void playCharacterCard(int cardIndex) {
        // Waiting for CharacterCard implementation
        // (Expert only) Current player plays a character card

        // TODO: Check they have enough coins and decrement them

        // TODO: Activate card effect
    }

    // Update island owner based on influence
    public void updateIslandOwner(Island island) {
        Board playerBoard = fatherModel.privateModel.getInfluence(island);

        // TODO: Move towers, and eventually merge island, if the owner changes
    }

    public Player getCurrentPlayer() {
        return fatherModel.currentPlayer;
    }

}
