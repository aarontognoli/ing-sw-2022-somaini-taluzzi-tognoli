package it.polimi.ingsw.mvc;

import it.polimi.ingsw.bag.Bag;
import it.polimi.ingsw.cards.AssistantCard;
import it.polimi.ingsw.cloud.Cloud;
import it.polimi.ingsw.enums.*;
import it.polimi.ingsw.pawn.*;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Model {
    //Look for a "ring" data type
    private List<Island> islands;
    private List<Professor> professors;
    private List<Player> players;
    private GameMode gameMode;
    private Bag bag;
    private List<Cloud> clouds;

    public Model() {
        islands = new ArrayList<Island>();
        professors = new ArrayList<Professor>();
        players = new ArrayList<Player>();

    }

    public Model(Model copy) {
        //TODO copy constructor
    }

    //PRIVATE METHODS

    private Player getProfessorOwner(Color c) {
        return null;
    }

    private Student removeStudentFromWaitingRoom(Student student, Player player) {
        return null;
    }

    private void addStudentToIsland(Student student, Island island) {

    }

    private void addStudentToDiningRoom(Student student, Player player) {

    }

    private void fillClouds() {

    }

    private void prepareMatch(int playersNumber) {

    }

    private Student drawStudentFromBag() {
        return null;
    }

    private Player getInfluence(Island island) {
        return null;
    }

    private void placeTower(Player player) {
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

    public void placeMotherNature(Island startingIsland) {

    }

    public void moveMotherNature(int steps) {
    }

    public void moveStudentToIsland(Student student, Island island) {
    }

    public void moveStudentToDiningRoom(Student student) {
    }
}
