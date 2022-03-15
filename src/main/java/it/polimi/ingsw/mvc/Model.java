package it.polimi.ingsw.mvc;

import it.polimi.ingsw.bag.Bag;
import it.polimi.ingsw.cards.AssistantCard;
import it.polimi.ingsw.cards.Deck;
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

    private void prepareMatch() {
        bag = new Bag(2);
        // TODO: Assign students to the islands
    }

    public Model(Model copy) {
        //TODO copy constructor
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

    //PUBLIC METHODS

    public void playAssistant(AssistantCard assistant) {

    }

    public void drawStudentsIntoEntrance() {
    }

    public void drawStudentsIntoCloud(Cloud cloud) {
    }

    public void endTurn() {

    }

    public void getStudentsFromCloud(Cloud cloud) {

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
    public void placeMotherNature(Island startingIsland) throws Exception {
        if (motherNature != null) {
            throw new Exception("Mother Nature already chosen");
        }

        for (Island i : islands) {
            if (i.equals(startingIsland)) {
                motherNature = new MotherNature(i);
                return;
            }
        }

        throw new Exception("Island not found");
    }

    public void chooseDeck(Player player, Deck deck) throws Exception {
        for (Player p : players) {
            if (p.equals(player)) {
                p.setDeck(deck);

                // TODO: Everyone had chosen a deck and mother nature
                //  Was placed
                prepareMatch();

                return;
            }
        }

        throw new Exception("Player not found.");
    }

    public void moveMotherNature(int steps) {
    }

    public void moveStudentToIsland(Student student, Island island) {
    }

    public void moveStudentToDiningRoom(Student student) {
    }
}
