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

import java.util.*;
import java.util.concurrent.ExecutionException;

import static java.lang.Math.floorMod;

public class Model {

    static private final int TOTAL_ISLANDS_NUMBER = 12;
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
        for (int i = 0; i < TOTAL_ISLANDS_NUMBER; i++) {
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
       ///DOING PRIVATE METHODS
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

    //Returns null if there is no player with more influence than others
    private Board getInfluence(Island island) {
        Player maxInfluencePlayer = null;
        int maxPlayerInfluence = 0;
        Boolean tie = false;
        int currentPlayerInfluence;
        for (int i = 0; i < totalPlayerCount; i++) {
            currentPlayerInfluence = 0;
            for (Tower t : island.getTowers()) {
                if (t.getColor().ordinal() == i) {
                    currentPlayerInfluence++;
                }

            }
            for (Student s : island.getStudents()) {
                if (professors.get(s.getColor().ordinal()).getPosition().equals(players.get(i).getBoard())) {
                    currentPlayerInfluence++;
                }
            }
            if (currentPlayerInfluence > maxPlayerInfluence || maxInfluencePlayer == null) {
                maxPlayerInfluence = currentPlayerInfluence;
                maxInfluencePlayer = players.get(i);
                tie = false;
            } else if (currentPlayerInfluence == maxPlayerInfluence) {
                tie = true;
            }
        }

        if (tie)
            return null;
        return maxInfluencePlayer.getBoard();
    }

    private void placeTower(Board player) throws Exception {
        //towers can only be placed on the island containing MotherNature
        //TODO: is it better to create a dedicated removeTower() Method in Board Class?
        getMotherNatureIsland().addTower(player.getTowers().remove(player.getTowers().size() - 1));

    }

    private void removeAllTowers(Island island) {

        List<Tower> towers;
        towers = island.removeAllTowers();
        /*for (Tower t : towers) {
            //TODO: check rules for 3 and 4 players
            //player 0 WHITE, 1 BLACK, 3 GREY
            players.get(t.getColor().ordinal()).getBoard().getTowers().add(t);
        }*/
        if (!towers.isEmpty()) {
            players.get(towers.get(0).getColor().ordinal()).getBoard().getTowers().addAll(towers);
        }
    }

    private void mergeIslands(Island island) throws Exception {
        int currentIslandIndex = islands.indexOf(island);
        int prev = floorMod(currentIslandIndex - 1, TOTAL_ISLANDS_NUMBER);
        int next = floorMod(currentIslandIndex + 1, TOTAL_ISLANDS_NUMBER);
        Boolean prevDone = false, nextDone = false;
        if (islands.get(prev).getTowerColor() != islands.get(currentIslandIndex).getTowerColor()) {
            islands.set(currentIslandIndex, new Island(islands.get(currentIslandIndex), islands.get(prev)));
            prevDone = true;
        }
        if (islands.get(next).getTowerColor() != islands.get(currentIslandIndex).getTowerColor()) {
            islands.set(currentIslandIndex, new Island(islands.get(currentIslandIndex), islands.get(next)));
            nextDone = true;
        }
        //the index changes if i remove islands
        Island tempIslandToGetIndex = islands.get(currentIslandIndex);
        if (prevDone) {
            islands.remove(floorMod(islands.indexOf(tempIslandToGetIndex) - 1, TOTAL_ISLANDS_NUMBER));
        }
        if (nextDone) {
            islands.remove(floorMod(islands.indexOf(tempIslandToGetIndex) + 1, TOTAL_ISLANDS_NUMBER));
        }


    }

    private void placeProfessorInBoard(Professor professor) {
        professor.move(currentPlayer.getBoard());
    }

    private void checkVictoryConditions() {
        //TODO
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
