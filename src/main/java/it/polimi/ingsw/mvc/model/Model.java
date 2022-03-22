package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.bag.Bag;
import it.polimi.ingsw.cards.DeckId;
import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.cloud.Cloud;
import it.polimi.ingsw.enums.*;
import it.polimi.ingsw.pawn.*;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;
import it.polimi.ingsw.player.Player;

import java.util.*;

import static java.lang.Math.floorMod;

public class Model {

    static final int TOTAL_ISLANDS_NUMBER = 12;
    // TODO: Use a circular pointer list
    List<Island> islands;

    // Professor with board that has them
    List<Professor> professors;

    // Player data and their board
    int totalPlayerCount;
    List<Player> players;
    Player currentPlayer;

    GameMode gameMode;

    // Bag where I can draw new students
    Bag bag;

    // Clouds in the middle
    List<Cloud> clouds;

    MotherNature motherNature;

    // Initialize game with starting rules
    public Model() {
        professors = new ArrayList<>();
        players = new ArrayList<>();
        clouds = new ArrayList<>();

        // Initialize islands
        islands = new ArrayList<Island>();
        for (int i = 0; i < TOTAL_ISLANDS_NUMBER; i++) {
            islands.add(new Island());
        }
    }


    //PUBLIC METHODS

    public void playAssistant(AssistantCard assistantCard) {
        currentPlayer.setCurrentAssistantCard(assistantCard);
    }

    public void drawStudentsIntoEntrance(int cloudIndex) {
        List<Student> studentsFromCloud = clouds.get(cloudIndex).getStudents();
        currentPlayer.getBoard().addStudentsToEntrance(studentsFromCloud);
    }

    public void endTurn() {
        //TODO
    }

    public Island getMotherNatureIsland() {
        return motherNature.getPosition();
    }

    public void setPlayersCount(int playersCount) {
        totalPlayerCount = playersCount;
    }

    public void playerJoin(String nickname) throws Exception {
        if (players.size() == totalPlayerCount) {
            throw new Exception("Player pool full.");
        }

        players.add(new Player(nickname));
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    // Called during game preparation
    public void placeMotherNature(int islandIndex) throws IndexOutOfBoundsException, Exception {
        if (motherNature != null) {
            throw new Exception("Mother Nature already chosen");
        }

        motherNature = new MotherNature(islands.get(islandIndex));
    }

    public void chooseDeck(String playerNickname, DeckId deckId) throws Exception, IndexOutOfBoundsException {

        for (Player p : players) {
            if (p.getNickname().equals(playerNickname)) {
                // Waiting for deck implementation
                // Select correct deck

                //p.setDeck(deck);

                // TODO: Everyone had chosen a deck and mother nature
                //  Was placed
                // prepareMatch();

                return;
            }
        }

        throw new Exception("Player not found.");
    }

    public void moveMotherNature(int steps) {
        int indexPreviousMotherNatureIsland = islands.indexOf(motherNature.getPosition());
        Island destination = islands.get(indexPreviousMotherNatureIsland + steps);
        motherNature.move(destination);
    }

    public void moveStudentToIsland(Color studentColor, int islandIndex) throws Exception {
        // Move a student (of this color) from the current player's entrance
        // into the correct island
        Student selectedStudent = getStudentInEntrance(studentColor);
        addStudentToIsland(selectedStudent, islands.get(islandIndex));
    }

    public void moveStudentToDiningRoom(Color studentColor) throws Exception{
        // Move a student (of this color) from the current player's entrance
        // into their dining room
        Student selectedStudent = getStudentInEntrance(studentColor);
        addStudentToDiningRoom(selectedStudent, currentPlayer.getBoard());

        // (Expert Only) If we reached one of the critical placeholder in the dining room,
        // Assign a new Coin to current player
        if (gameMode.equals(GameMode.EXPERT_MODE)) {
            List<List<Student>> currentDiningRoom = currentPlayer.getBoard().getDiningRoom();
            int indexOfSelectedStudent = currentDiningRoom.get(selectedStudent.getColor().ordinal()).indexOf(selectedStudent);
            if (indexOfSelectedStudent == 2 || indexOfSelectedStudent == 5 || indexOfSelectedStudent == 8) {
                rewardCoin();
            }
        }

    }

    public void playCharacterCard(int cardIndex) {
        // Waiting for CharacterCard implementation
        // (Expert only) Current player plays a character card

        // TODO: Check they have enough coins and decrement them

        // TODO: Activate card effect
    }
}
