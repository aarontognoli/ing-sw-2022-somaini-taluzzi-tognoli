package it.polimi.ingsw.mvc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static java.lang.Math.floorMod;

import it.polimi.ingsw.bag.Bag;
import it.polimi.ingsw.cloud.Cloud;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.exceptions.NotFoundException;
import it.polimi.ingsw.pawn.Professor;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.pawn.Tower;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;
import it.polimi.ingsw.player.DiningRoomFullException;
import it.polimi.ingsw.player.Player;

public class PrivateModel {

    final Model fatherModel;

    public PrivateModel(Model fatherModel) {
        this.fatherModel = fatherModel;
    }

    void prepareMatch() {
        fatherModel.bag = new Bag(2);
        // TODO: Assign students to the islands

        // (Expert only) TODO: choose 3 random character cards
    }

    Board getProfessorOwner(Color c) throws Exception {
        return fatherModel.professors.get(c.ordinal()).getPosition();

    }

    Student removeStudentFromEntrance(Student student, Board player) throws NotFoundException {
        List<Student> entrance = player.getEntrance();
        for (Student s : entrance) {
            if (s.getColor().equals(student.getColor())) {
                entrance.remove(s);
                return s;
            }
        }

        throw new NotFoundException("Student not found");
    }

    void addStudentToIsland(Student student, Island island) {
        island.getStudents().add(student);
    }

    void addStudentToDiningRoom(Student student, Board player) throws DiningRoomFullException {
        player.addStudentsToDiningRoom(student);
    }

    void fillClouds() {
        int studentsToDraw;
        List<Student> studentsToAdd;
        for (Cloud c : fatherModel.clouds) {
            studentsToAdd = new ArrayList<>();
            if (fatherModel.totalPlayerCount == 3)
                studentsToDraw = 4;
            else
                studentsToDraw = 3;

            for (int i = 0; i < studentsToDraw; i++)
                studentsToAdd.add(drawStudentFromBag());
            c.putStudents(studentsToAdd);
        }
    }

    Student drawStudentFromBag() {
        // TODO: implement later, waiting for bag implementation
        return null;
    }

    // Returns null if there is no player with more influence than others
    Board getInfluence(Island island) {
        Player maxInfluencePlayer = null;
        int maxPlayerInfluence = 0;
        Boolean tie = false;
        int currentPlayerInfluence;
        for (int i = 0; i < fatherModel.totalPlayerCount; i++) {
            currentPlayerInfluence = 0;
            for (Tower t : island.getTowers()) {
                if (t.getColor().ordinal() == i) {
                    currentPlayerInfluence++;
                }

            }
            for (Student s : island.getStudents()) {
                if (fatherModel.professors.get(s.getColor().ordinal()).getPosition()
                        .equals(fatherModel.players.get(i).getBoard())) {
                    currentPlayerInfluence++;
                }
            }
            if (currentPlayerInfluence > maxPlayerInfluence || maxInfluencePlayer == null) {
                maxPlayerInfluence = currentPlayerInfluence;
                maxInfluencePlayer = fatherModel.players.get(i);
                tie = false;
            } else if (currentPlayerInfluence == maxPlayerInfluence) {
                tie = true;
            }
        }

        if (tie)
            return null;
        return maxInfluencePlayer.getBoard();
    }

    void placeTower(Board board) throws Exception {
        // towers can only be placed on the island containing MotherNature

        fatherModel.publicModel.getMotherNatureIsland().addTower(board.removeTower());
    }

    void removeAllTowers(Island island) {

        List<Tower> towers;
        towers = island.removeAllTowers();
        if (!towers.isEmpty()) {
            fatherModel.players.get(towers.get(0).getColor().ordinal()).getBoard().getTowers().addAll(towers);
        }
    }

    void mergeIslands(Island island) throws Exception {
        int currentIslandIndex = fatherModel.islands.indexOf(island);
        int prev = floorMod(currentIslandIndex - 1, Model.TOTAL_ISLANDS_NUMBER);
        int next = floorMod(currentIslandIndex + 1, Model.TOTAL_ISLANDS_NUMBER);
        Boolean prevDone = false, nextDone = false;
        if (fatherModel.islands.get(prev).getTowerColor() == fatherModel.islands.get(currentIslandIndex)
                .getTowerColor()) {
            fatherModel.islands.set(currentIslandIndex,
                    new Island(fatherModel.islands.get(currentIslandIndex), fatherModel.islands.get(prev)));
            prevDone = true;
        }
        if (fatherModel.islands.get(next).getTowerColor() == fatherModel.islands.get(currentIslandIndex)
                .getTowerColor()) {
            fatherModel.islands.set(currentIslandIndex,
                    new Island(fatherModel.islands.get(currentIslandIndex), fatherModel.islands.get(next)));
            nextDone = true;
        }
        // the index changes if i remove islands
        Island tempIslandToGetIndex = fatherModel.islands.get(currentIslandIndex);
        if (prevDone) {
            fatherModel.islands.remove(
                    floorMod(fatherModel.islands.indexOf(tempIslandToGetIndex) - 1, Model.TOTAL_ISLANDS_NUMBER));
        }
        if (nextDone) {
            fatherModel.islands.remove(
                    floorMod(fatherModel.islands.indexOf(tempIslandToGetIndex) + 1, Model.TOTAL_ISLANDS_NUMBER));
        }

    }

    void placeProfessorInBoard(Professor professor) {
        professor.move(fatherModel.currentPlayer.getBoard());
    }

    // the method will be called in the right moments
    Player checkVictoryConditions() throws Exception {
        Boolean noAssistantCards = false;
        // every time a new tower is placed
        Player winner;
        for (Player p : fatherModel.players) {
            if (p.getBoard().getTowers().isEmpty()) {
                return p;
            }
        }
        for (Player p : fatherModel.players) {
            // TODO: wait for Final Deck implementation check assistants cards number and
            // ser noAssistantCards accordingly
        }
        // when some islands are merged
        if ((fatherModel.islands.size() <= 3) || noAssistantCards)// TODO: wait for bag implementation and ckeck if the
                                                                  // bag is empty)
        {
            winner = checkTowersForVictory();
            if (winner == null)
                return checkProfessorsForVictory();
            else
                return winner;
        }

        return null;
    }

    // support methods for more readable code
    Player checkTowersForVictory() {
        List<Integer> towersCountForEachPlayer = new ArrayList<>(Collections.nCopies(fatherModel.totalPlayerCount, 0));
        int max = 0;
        Player winner = null;
        for (Island i : fatherModel.islands) {
            try {
                towersCountForEachPlayer.set(i.getTowerColor().ordinal(),
                        towersCountForEachPlayer.get(i.getTowerColor().ordinal()) + i.getTowers().size());
            } catch (Exception e) {
                // if there are no towers
                continue;
            }
        }
        for (int i = 0; i < fatherModel.totalPlayerCount; i++) {
            if (towersCountForEachPlayer.get(i) > max) {
                max = towersCountForEachPlayer.get(i);
                winner = fatherModel.players.get(i);
            } else if (towersCountForEachPlayer.get(i) == max) {
                // tie
                winner = null;
            }
        }

        return winner;
    }

    Player checkProfessorsForVictory() throws Exception {
        // 5 professors, a tie isn't possible
        List<Integer> professorsCountForEachPlayer = new ArrayList<>(
                Collections.nCopies(fatherModel.totalPlayerCount, 0));
        int max = 0;
        Player winner = null;
        for (Professor p : fatherModel.professors) {
            professorsCountForEachPlayer.set(fatherModel.players.indexOf(getPlayerFromBoard(p.getPosition())),
                    professorsCountForEachPlayer.get(fatherModel.players.indexOf(getPlayerFromBoard(p.getPosition())))
                            + 1);
        }

        for (int i = 0; i < fatherModel.totalPlayerCount; i++) {
            if (professorsCountForEachPlayer.get(i) > max) {
                max = professorsCountForEachPlayer.get(i);
                winner = fatherModel.players.get(i);
            }
        }

        return winner;
    }

    Player getPlayerFromBoard(Board board) throws Exception {
        for (Player p : fatherModel.players) {
            if (p.getBoard().equals(board)) {
                return p;
            }
        }
        throw new Exception("Board not existing");
    }

    Student getStudentInEntrance(Color c) throws NotFoundException {
        for (Student s : fatherModel.currentPlayer.getBoard().getEntrance()) {
            if (s.getColor().equals(c)) {
                return s;
            }
        }

        throw new NotFoundException("Student not found");
    }

    void rewardCoin() {
        // Reward a new coin to the current player
        fatherModel.currentPlayer.getBoard().rewardCoin();
    }
}
