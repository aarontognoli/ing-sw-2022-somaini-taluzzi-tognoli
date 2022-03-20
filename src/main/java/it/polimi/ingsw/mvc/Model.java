package it.polimi.ingsw.mvc;

import it.polimi.ingsw.bag.Bag;
import it.polimi.ingsw.cards.AssistantCard;
import it.polimi.ingsw.cards.AssistantCardID;
import it.polimi.ingsw.cards.Deck;
import it.polimi.ingsw.cards.DeckId;
import it.polimi.ingsw.cloud.Cloud;
import it.polimi.ingsw.enums.*;
import it.polimi.ingsw.pawn.*;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;
import it.polimi.ingsw.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Model {
    // TODO: Use a circular pointer list
    private List<Island> islands;

    // Professor with board that has them
    private List<Professor> professors;

    // Player data and their board
    private int totalPlayerCount;
    private List<Player> players;
    private Player currentPlayer;

    private GameMode gameMode;

    // Bag where I can draw new students
    private Bag bag;

    // Clouds in the middle
    private List<Cloud> clouds;

    private MotherNature motherNature;

    // Initialize game with starting rules
    public Model() {
        professors = new ArrayList<>();
        players = new ArrayList<>();
        clouds = new ArrayList<>();

        // Populate islands
        islands = new ArrayList<Island>();
        for (int i = 0; i < 12; i++) {
            islands.add(new Island());
        }
    }

    ///DOING PRIVATE METHODS
    private void prepareMatch() {
        bag = new Bag(2);
        // TODO: Assign students to the islands

        // (Expert only) TODO: choose 3 random character cards
    }

    public Model(Model copy) {
        // TODO copy constructor
    }

    // * PRIVATE METHODS

    private Board getProfessorOwner(Color c) throws Exception {
        return professors.get(c.ordinal()).getPosition();

    }

    private Student removeStudentFromWaitingRoom(Student student, Board player) throws Exception {
        List<Student> entrance = player.getEntrance();
        for (Student s : entrance) {
            if (s.getColor().equals(student.getColor())) {
                entrance.remove(s);
                return s;
            }
        }

        throw new Exception("Student not found");
    }

    private void addStudentToIsland(Student student, Island island) {
        island.getStudents().add(student);
    }

    private void addStudentToDiningRoom(Student student, Board player) {
        player.getDiningRoom().get(student.getColor().ordinal()).add(student);
    }

    private void fillClouds() {
        int studentsToDraw;
        List<Student> studentsToAdd;
        for (Cloud c : clouds) {
            studentsToAdd = new ArrayList<>();
            if (totalPlayerCount == 3)
                studentsToDraw = 4;
            else
                studentsToDraw = 3;

            for (int i = 0; i < studentsToDraw; i++)
                studentsToAdd.add(drawStudentFromBag());
            c.putStudents(studentsToAdd);
        }
    }

    private Student drawStudentFromBag() {
        //TODO: implement later, waiting for bag implementation
        return null;
    }

    private Board getInfluence(Island island) {

        return null;
    }

    private void placeTower(Board player) throws Exception {
        //towers can only be placed on the island containing MotherNature
        //TODO: is it better to create a dedicated removeTower() Method in Board Class?
        getMotherNatureIsland().addTower(player.getTowers().remove(player.getTowers().size() - 1));

    }

    private void removeTower(Island island) {
        List<Tower> torri;
        torri = island.removeAllTowers();
        for (Tower t : torri) {
            //TODO: check rules for 3 and 4 players
            //player 0 WHITE, 1 BLACK, 3 GREY
            players.get(t.getColor().ordinal()).getBoard().getTowers().add(t);
        }
    }

    private void mergeIslands(Island island) {

    }

    private void placeProfessorInBoard(Professor professor) {
        professor.move(currentPlayer.getBoard());
    }

    private void checkVictoryConditions() {

    }

    private void rewardCoin() throws Exception {
        // Reward a new coin to the current player
        currentPlayer.getBoard().rewardCoin();
    }

    //PUBLIC METHODS

    public void playAssistant(AssistantCardID assistantCardID) {
        // Current player plays this assistant card
    }

    public void drawStudentsIntoEntrance(int cloudIndex) {
        // Current player draws student from this cloud
    }

    public void endTurn() {

    }

    public Island getMotherNatureIsland() {
        return null;
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
    }

    public void moveStudentToIsland(Color studentColor, int islandIndex) {
        // TODO: Move a student (of this color) from the current player's entrance
        // into the correct island
    }

    public void moveStudentToDiningRoom(Color studentColor) {
        // TODO: Move a student (of this color) from the current player's entrance
        // into their dining room

        // (Expert Only) TODO: If we reached one of the critical placeholder in the dining room,
        // Assign a new Coin to current player
    }

    public void playCharacterCard(int cardIndex) {
        // (Expert only) Current player plays a character card

        // TODO: Check they have enough coins and decrement them

        // TODO: Activate card effect
    }
}
