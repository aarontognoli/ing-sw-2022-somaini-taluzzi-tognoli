package it.polimi.ingsw.mvc.model;

import java.util.List;

import it.polimi.ingsw.cards.Deck;
import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;
import it.polimi.ingsw.player.Player;
import it.polimi.ingsw.pawn.MotherNature;

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

        TowerColor towerColor;
        if (fatherModel.totalPlayerCount == 4) {
            // 4 player rules: player 0 and 2 have WHITE towers, player 1 and 3 have BLACK
            // towers.
            towerColor = TowerColor.values()[fatherModel.players.size() % 2];
        } else {
            // 3 player rules: player 0 gets WHITE, player 1 gets BLACK, player 2 gets GREY
            towerColor = TowerColor.values()[fatherModel.players.size()];
        }

        fatherModel.players.add(new Player(nickname, towerColor));
    }

    public void setGameMode(GameMode gameMode) {
        fatherModel.gameMode = gameMode;
    }

    // Called during game preparation
    public void placeMotherNature(int islandIndex) throws IndexOutOfBoundsException, Exception {
        if (fatherModel.motherNature != null) {
            throw new Exception("Mother Nature already chosen");
        }

        fatherModel.motherNature = new MotherNature(fatherModel.islands.get(islandIndex));
    }

    public void chooseDeck(String playerNickname, Deck deck) throws Exception, IndexOutOfBoundsException {

        for (Player p : fatherModel.players) {
            if (p.getNickname().equals(playerNickname)) {
                // Waiting for deck implementation
                // Select correct deck

                // p.setDeck(deck);

                // TODO: Everyone had chosen a deck and mother nature
                // Was placed
                // prepareMatch();

                return;
            }
        }

        throw new Exception("Player not found.");
    }

    public void moveMotherNature(int steps) {
        int indexPreviousMotherNatureIsland = fatherModel.islands.indexOf(fatherModel.motherNature.getPosition());
        Island destination = fatherModel.islands.get(indexPreviousMotherNatureIsland + steps);
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
        // TODO: Get influence correctly
        // Board playerBoard = fatherModel.privateModel.getInfluence(island);

        // TODO: Move towers, and eventually merge island, if the owner changes
    }

    public Player getCurrentPlayer() {
        return fatherModel.currentPlayer;
    }

}