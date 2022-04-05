package it.polimi.ingsw.mvc.model;

import java.util.*;

import static java.lang.Math.floorMod;

import it.polimi.ingsw.bag.Bag;
import it.polimi.ingsw.bag.BagEmptyException;
import it.polimi.ingsw.cards.characters.BardCharacter.BardCharacter;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.cards.characters.FlagCharacter.FlagCharacter;
import it.polimi.ingsw.cards.characters.HerbalistCharacter.HerbalistCharacter;
import it.polimi.ingsw.cards.characters.JokerCharacter.JokerCharacter;
import it.polimi.ingsw.cards.characters.PostManCharacter.PostManCharacter;
import it.polimi.ingsw.cards.characters.WineCharacter.WineCharacter;
import it.polimi.ingsw.cloud.Cloud;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.exceptions.BoardNotInGameException;
import it.polimi.ingsw.exceptions.NoTowerException;
import it.polimi.ingsw.exceptions.NotFoundException;
import it.polimi.ingsw.exceptions.TowerDifferentColorException;
import it.polimi.ingsw.pawn.MotherNature;
import it.polimi.ingsw.pawn.Professor;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.pawn.Tower;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;
import it.polimi.ingsw.player.DiningRoomFullException;
import it.polimi.ingsw.player.Player;

public class PrivateModel {

    static final int INITIAL_STUDENT_ENTRANCE_3 = 9;
    static final int INITIAL_STUDENT_ENTRANCE_2_4 = 7;

    final Model fatherModel;

    public PrivateModel(Model fatherModel) {
        this.fatherModel = fatherModel;
    }

    void prepareMatch(int motherNatureIslandIndex) throws BagEmptyException {
        fatherModel.bag = new Bag(2);

        for (int i = 0; i < 5; i++) {
            int index1 = (motherNatureIslandIndex + i + 1) % 12;
            int index2 = (motherNatureIslandIndex + i + 6 + 1) % 12;
            fatherModel.islands.get(index1).addStudent(drawStudentFromBag());
            fatherModel.islands.get(index2).addStudent(drawStudentFromBag());
        }

        fatherModel.bag = new Bag(24);

        int studentsToAddInEntranceCount = fatherModel.totalPlayerCount == 3 ? INITIAL_STUDENT_ENTRANCE_3
                : INITIAL_STUDENT_ENTRANCE_2_4;

        for (Player player : fatherModel.players) {
            List<Student> studentsToAddInEntrance = new ArrayList<>(studentsToAddInEntranceCount);
            for (int i = 0; i < studentsToAddInEntranceCount; i++) {
                studentsToAddInEntrance.add(drawStudentFromBag());
            }

            player.getBoard().addStudentsToEntrance(studentsToAddInEntrance);
        }

        // (Expert only) choose 3 random character cards
        if (fatherModel.gameMode.equals(GameMode.EXPERT_MODE)) {
            // give 1 coin to each player
            for (Player player : fatherModel.players) {
                player.getBoard().rewardCoin();
            }

            fatherModel.currentGameCards = new ArrayList<>(3);

            // TODO: add all character cards
            // following the alphabetical order: Bard, Flag, Herbalist, Joker, Postman, Wine
            Random random = new Random();
            int previousIndex1 = -1;
            int previousIndex2 = -1;
            for (int i = 0; i < 3; i++) {
                int index = random.nextInt(6);
                while (index == previousIndex1 || index == previousIndex2) {
                    index = random.nextInt(6);
                }
                previousIndex2 = previousIndex1;
                previousIndex1 = index;

                switch (index) {
                    case 0 -> fatherModel.currentGameCards.add(new BardCharacter(fatherModel));
                    case 1 -> fatherModel.currentGameCards.add(new FlagCharacter(fatherModel));
                    case 2 -> fatherModel.currentGameCards.add(new HerbalistCharacter(fatherModel));
                    case 3 -> {
                        List<Student> studentsForJoker = new ArrayList<>(JokerCharacter.INITIAL_STUDENT_SIZE);
                        for (int j = 0; j < JokerCharacter.INITIAL_STUDENT_SIZE; j++) {
                            studentsForJoker.add(drawStudentFromBag());
                        }
                        fatherModel.currentGameCards.add(new JokerCharacter(fatherModel, studentsForJoker));
                    }
                    case 4 -> fatherModel.currentGameCards.add(new PostManCharacter(fatherModel));
                    case 5 -> {
                        List<Student> studentsForWine = new ArrayList<>(WineCharacter.INITIAL_STUDENT_SIZE);
                        for (int j = 0; j < WineCharacter.INITIAL_STUDENT_SIZE; j++) {
                            studentsForWine.add(drawStudentFromBag());
                        }
                        fatherModel.currentGameCards.add(new WineCharacter(fatherModel, studentsForWine));
                    }
                }
            }
        }
    }

    Board getProfessorOwner(Color c) {
        return fatherModel.professors.get(c.ordinal()).getPosition();
    }

    void addStudentToIsland(Student student, Island island) {
        island.getStudents().add(student);
    }

    void addStudentToDiningRoom(Student student, Board player) throws DiningRoomFullException {
        player.addStudentsToDiningRoom(student);
    }

    void fillClouds() throws BagEmptyException {
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

    Student drawStudentFromBag() throws BagEmptyException {
        return fatherModel.bag.draw();
    }

    Board getInfluence(Island island) {
        if (island.hasNoEntryTile()) {
            island.removeNoEntryTile();
            for (CharacterCard card : fatherModel.currentGameCards) {
                if (card.getClass() == HerbalistCharacter.class) {
                    ((HerbalistCharacter) card).addNoEntryTile();
                    return null;
                }
            }
            throw new RuntimeException("Impossible state of the game");
        } else {
            return fatherModel.influenceCalculator.getInfluence(island);
        }
    }

    void placeTower(Board board) throws Exception {
        // towers can only be placed on the island containing MotherNature
        Tower tempTower = board.removeTower();
        try {

            fatherModel.publicModel.getMotherNatureIsland().addTower(tempTower);
        } catch (TowerDifferentColorException e) {
            board.getTowers().add(tempTower);
            throw e;
        }

    }

    void removeAllTowers(Island island) {

        List<Tower> towers;
        towers = island.removeAllTowers();
        if (!towers.isEmpty()) {
            fatherModel.players.get(towers.get(0).getColor().ordinal()).getBoard().getTowers().addAll(towers);
        }
    }

    void mergeIslands(Island island) throws NoTowerException {
        int currentIslandIndex = fatherModel.islands.indexOf(island);
        int prev = floorMod(currentIslandIndex - 1, fatherModel.islands.size());
        int next = floorMod(currentIslandIndex + 1, fatherModel.islands.size());
        boolean prevDone = false, nextDone = false, noTowers = false;
        try {
            if (fatherModel.islands.get(prev).getTowerColor() == fatherModel.islands.get(currentIslandIndex)
                    .getTowerColor()) {
                fatherModel.islands.set(currentIslandIndex,
                        new Island(fatherModel.islands.get(currentIslandIndex), fatherModel.islands.get(prev)));
                prevDone = true;

            }
        } catch (NoTowerException e) {
            noTowers = true;
        } catch (TowerDifferentColorException e) {
            // This can't happen
            throw new RuntimeException("Can't enter here");
        }
        try {
            if (fatherModel.islands.get(next).getTowerColor() == fatherModel.islands.get(currentIslandIndex)
                    .getTowerColor()) {
                fatherModel.islands.set(currentIslandIndex,
                        new Island(fatherModel.islands.get(currentIslandIndex), fatherModel.islands.get(next)));
                nextDone = true;
            }
        } catch (NoTowerException e) {
            if (noTowers)
                throw e;
        } catch (TowerDifferentColorException e) {
            // This can't happen
            throw new RuntimeException("Can't enter here");
        }
        // the index changes if i remove islands
        Island tempIslandToGetIndex = fatherModel.islands.get(currentIslandIndex);
        if (prevDone) {
            fatherModel.islands.remove(
                    floorMod(fatherModel.islands.indexOf(tempIslandToGetIndex) - 1, fatherModel.islands.size()));
        }
        if (nextDone) {
            fatherModel.islands.remove(
                    floorMod(fatherModel.islands.indexOf(tempIslandToGetIndex) + 1, fatherModel.islands.size()));
        }
        fatherModel.motherNature.move(tempIslandToGetIndex);

    }

    void placeProfessorInBoard(Professor professor) {
        professor.move(fatherModel.currentPlayer.getBoard());
    }

    // the method will be called in the right moments
    Player checkVictoryConditions() {

        Player winner = fatherModel.publicModel.checkFinishedTowers();

        if (winner != null)
            return winner;

        // Check if someone has an empty deck
        boolean emptyDeckSomewhere = false;
        for (Player p : fatherModel.players) {
            if (p.getDeck().isEmpty()) {
                emptyDeckSomewhere = true;
                break;
            }
        }

        // when some islands are merged
        if ((fatherModel.islands.size() <= 3) || emptyDeckSomewhere || fatherModel.bag.isEmpty()) {
            winner = checkTowersForVictory();
            if (winner == null)
                return checkProfessorsForVictory();
            else
                return winner;
        }

        return null;
    }

    // support methods for more readable code
    private Player checkTowersForVictory() {
        int minTowersCount = Integer.MAX_VALUE;
        int playerWinningIndex = -1;

        for (int i = 0; i < fatherModel.totalPlayerCount; i++) {
            if (fatherModel.totalPlayerCount != 4 || i % 2 == 0) {
                Player p = fatherModel.players.get(i);

                int currentPlayerTowerCount = p.getBoard().getTowers().size();

                if (currentPlayerTowerCount < minTowersCount) {
                    playerWinningIndex = i;
                    minTowersCount = currentPlayerTowerCount;
                } else if (currentPlayerTowerCount == minTowersCount) {
                    playerWinningIndex = -1;
                }
            }
        }

        if (playerWinningIndex == -1) {
            return null;
        }

        return fatherModel.players.get(playerWinningIndex);
    }

    private Player checkProfessorsForVictory() {
        // 5 professors, tie only for 3 players. The player whose index is lesser wins,
        // in case of tie.
        List<Integer> professorsCountForEachPlayer = new ArrayList<>(
                Collections.nCopies(fatherModel.totalPlayerCount, 0));

        for (Professor p : fatherModel.professors) {
            try {
                int playerIndex = fatherModel.players.indexOf(getPlayerFromBoard(p.getPosition()));

                /*
                 * In 4 players game: We count the professors only for player 0 and 2
                 * When a professor is owned by player 1, we update the count for player 0
                 * (their teammate)
                 * When a professor is owned by player 3, we update the count for player 2
                 * (their teammate)
                 */
                if (fatherModel.totalPlayerCount == 4 && playerIndex % 2 == 1) {
                    playerIndex--;
                }

                professorsCountForEachPlayer.set(playerIndex, professorsCountForEachPlayer.get(playerIndex) + 1);
            } catch (BoardNotInGameException e) {
                // if this happens the code is severely bugged
                if (p.getPosition() != null) {
                    e.printStackTrace();
                    throw new RuntimeException("Board not found in Model. (?)");
                }
            }
        }

        int max = -1;
        Player winner = null;
        for (int i = 0; i < fatherModel.totalPlayerCount; i++) {
            if (professorsCountForEachPlayer.get(i) > max) {
                max = professorsCountForEachPlayer.get(i);
                winner = fatherModel.players.get(i);
            }
        }

        return winner;
    }

    Player getPlayerFromBoard(Board board) throws BoardNotInGameException {
        for (Player p : fatherModel.players) {
            if (p.getBoard().equals(board)) {
                return p;
            }
        }
        throw new BoardNotInGameException("Board not existing");
    }

    Student removeStudentFromEntrance(Color c, Board playerBoard) throws NotFoundException {
        List<Student> entrance = playerBoard.getEntrance();
        for (int i = 0; i < entrance.size(); i++) {
            Student s = entrance.get(i);
            if (s.getColor().equals(c)) {
                return entrance.remove(i);
            }
        }

        throw new NotFoundException("Student not found");
    }

    void rewardCoin() {
        // Reward a new coin to the current player
        fatherModel.currentPlayer.getBoard().rewardCoin();
    }

    void placeMotherNature(int islandIndex) throws Exception {
        if (fatherModel.motherNature != null) {
            throw new Exception("Mother Nature already chosen");
        }

        fatherModel.motherNature = new MotherNature(fatherModel.islands.get(islandIndex));
    }

    void updateProfessorPosition(Color professColor) {
        int maxStudentOfColor = -1;
        Board maxBoard = null;

        for (Player player : fatherModel.players) {
            Board currentBoard = player.getBoard();

            int studentsCountThisColor = currentBoard.getDiningRoom().get(professColor.ordinal()).size();

            if (fatherModel.professorMoverRule.isMaxStudentsCount(studentsCountThisColor, maxStudentOfColor,
                    player.getNickname())) {
                maxStudentOfColor = studentsCountThisColor;
                maxBoard = currentBoard;
            }
        }

        if (maxBoard == null) {
            // This should never happen (?)
            throw new RuntimeException("No player in model (?)");
        }

        fatherModel.professors.get(professColor.ordinal()).move(maxBoard);
    }
}
