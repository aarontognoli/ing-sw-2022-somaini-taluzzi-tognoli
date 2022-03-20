package it.polimi.ingsw.mvc;

import it.polimi.ingsw.bag.Bag;
import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.cards.DeckId;
import it.polimi.ingsw.cloud.Cloud;
import it.polimi.ingsw.enums.*;
import it.polimi.ingsw.pawn.*;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;
import it.polimi.ingsw.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Model {
    // TODO: Use a circular pointer list
    private List<Island> islands;

    // Professor with board that has them
    private List<Professor> professors;

    // Player data and their board
    private int totalPlayerCount;
    private List<Player> players;
    private int indexCurrentPlayer;

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

        // Initialize islands
        islands = new ArrayList<Island>();
        for (int i = 0; i < 12; i++) {
            islands.add(new Island());
        }
    }

    private void prepareMatch() {
        bag = new Bag(2);
        // TODO: Assign students to the islands

        // (Expert only) TODO: choose 3 random character cards
    }

    public Model(Model copy) {
        // TODO copy constructor
    }

    // Method to revise when we will implement distributed MVC
    public void setIndexCurrentPlayer(int indexCurrentPlayer) {
        this.indexCurrentPlayer = indexCurrentPlayer;
    }

    // * PRIVATE METHODS

    private Board getProfessorOwner(Color c) {
        // TODO: get Player that owns professor of color c
        return null;
    }

    private Student removeStudentFromWaitingRoom(Student student, Board player) {
        return null;
    }

    private void addStudentToIsland(Student student, Island island) {

    }

    private void addStudentToDiningRoom(Student student, Board player) {

    }

    private void fillClouds() {

    }

    private Student drawStudentFromBag() {
        return null;
    }

    private Board getInfluence(Island island) {
        return null;
    }

    private void placeTower(Board player) {
        //towers can only be placed on the island containing MotherNature
    }

    private void removeTower(Island island) {

    }

    private void mergeIslands(Island island) {

    }

    private void placeProfessorInBoard(Professor professor) {

    }

    private void checkVictoryConditions() {

    }

    private void rewardCoin() {
        // Reward a new coin to the current player
    }

    //PUBLIC METHODS

    public void playAssistant(AssistantCard assistantCardID) {
        // Current player plays this assistant card
    }

    public void drawStudentsIntoEntrance(int cloudIndex) {
        // Current player draws students from this cloud and puts them in entrance
        Board currentBoard = players.get(indexCurrentPlayer).getBoard();
        currentBoard.addStudentsToEntrance(clouds.get(cloudIndex).getStudents());
    }

    public void endTurn() {

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
