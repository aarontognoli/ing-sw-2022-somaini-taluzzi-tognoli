package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.bag.Bag;
import it.polimi.ingsw.bag.BagEmptyException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.cards.characters.CharacterFactory;
import it.polimi.ingsw.cards.characters.HerbalistCharacter.HerbalistCharacter;
import it.polimi.ingsw.cloud.Cloud;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.pawn.MotherNature;
import it.polimi.ingsw.pawn.Professor;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.pawn.Tower;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;
import it.polimi.ingsw.player.DiningRoomFullException;
import it.polimi.ingsw.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.Math.floorMod;

public class PrivateModel {

    static final int INITIAL_STUDENT_ENTRANCE_3 = 9;
    public static final int INITIAL_STUDENT_ENTRANCE_2_4 = 7;
    static final int INITIAL_CHARACTERS_COUNT = 3;

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

            try {
                player.getBoard().addStudentsToEntrance(studentsToAddInEntrance);
            } catch (EntranceFullException e) {
                e.printStackTrace();
                throw new RuntimeException("Should not overflow entrance during initialization");
            }
        }

        // (Expert only) choose 3 random character cards, and assign initial coin to
        // everyone
        if (fatherModel.gameMode.equals(GameMode.EXPERT_MODE)) {
            // give 1 coin to each player
            for (Player player : fatherModel.players) {
                player.getBoard().rewardCoin();
            }

            fatherModel.currentGameCards = new ArrayList<>(3);

            CharacterFactory characterFactory = new CharacterFactory();
            for (int i = 0; i < INITIAL_CHARACTERS_COUNT; i++) {
                fatherModel.currentGameCards.add(characterFactory.getRandomCharacter(fatherModel));
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

    HerbalistCharacter findHerbalist() throws NotFoundException {
        for (CharacterCard card : fatherModel.currentGameCards) {
            if (card.getClass() == HerbalistCharacter.class) {
                return (HerbalistCharacter) card;
            }
        }

        throw new NotFoundException("Herbalist not found in character cards");
    }

    Board getInfluence(Island island) {
        if (island.hasNoEntryTile()) {
            island.removeNoEntryTile();
            try {
                HerbalistCharacter card = findHerbalist();
                card.moveEntryTileBackToCard();
                return null;
            } catch (NotFoundException e) {
                throw new RuntimeException("Herbalist not found after finding no-entry tile. What?");
            }
        } else {
            return fatherModel.influenceCalculator.getInfluence(island);
        }
    }

    void removeAllTowers(Island island) {

        List<Tower> towers;
        towers = island.removeAllTowers();

        // Move these towers to the correct player board
        if (!towers.isEmpty()) {

            TowerColor towerColor = towers.get(0).getColor();
            int playerIndex;
            if (fatherModel.totalPlayerCount == 4) {
                playerIndex = towerColor.ordinal() * 2;
            } else {
                playerIndex = towerColor.ordinal();
            }

            fatherModel.players.get(playerIndex).getBoard().getTowers().addAll(towers);
        }
    }

    /**
     * Try to merge currentIsland to otherIsland, and if we can do the merge, set otherIsland to null
     *
     * @param currentIslandColor Color of the island we want the nearby ones to be merged
     * @param currentIndex       Index of the island we want the nearby ones to be merged
     * @param otherIndex         Index of the nearby island we want to merge and set to null
     * @implNote We pass currentIslandColor to better handle exception in the mergeIslands method
     */
    private void mergeAndSetNull(TowerColor currentIslandColor, int currentIndex, int otherIndex) {
        Island currentIsland = fatherModel.islands.get(currentIndex);
        try {
            TowerColor otherIslandColor = fatherModel.islands.get(otherIndex).getTowerColor();
            if (otherIslandColor == currentIslandColor) {
                fatherModel.islands.set(currentIndex,
                        new Island(currentIsland, fatherModel.islands.get(otherIndex))
                );
                fatherModel.islands.set(otherIndex, null);
            }
        } catch (NoTowerException e) {
            // Other island has no tower, do nothing.
        } catch (TowerDifferentColorException e) {
            throw new RuntimeException(
                    "Merge failed saying they have different color, but we just checked (?) "
                            + e.getMessage());
        }
    }

    /**
     * Merge island with nearby ones if they have same tower color
     *
     * @param island island where we just added a tower
     * @implNote When we merge a nearby island, set them as null. At the end, we filter out null elements
     */
    void mergeIslands(Island island) {
        int currentIslandIndex = fatherModel.islands.indexOf(island);

        TowerColor islandTowerColor;
        try {
            islandTowerColor = island.getTowerColor();
        } catch (NoTowerException e) {
            throw new RuntimeException("Who called mergeIslands when target island has no towers?! " + e.getMessage());
        }

        int prev = floorMod(currentIslandIndex - 1, fatherModel.islands.size());
        mergeAndSetNull(islandTowerColor, currentIslandIndex, prev);
        int next = floorMod(currentIslandIndex + 1, fatherModel.islands.size());
        mergeAndSetNull(islandTowerColor, currentIslandIndex, next);

        fatherModel.motherNature.move(fatherModel.islands.get(currentIslandIndex));

        // Filter out null
        fatherModel.islands = fatherModel.islands
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
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

    void incrementCurrentPlayer() {
        int currentPlayerIndex = fatherModel.players.indexOf(fatherModel.currentPlayer);

        int nextPlayerIndex = (currentPlayerIndex + 1) % fatherModel.players.size();
        fatherModel.currentPlayer = fatherModel.players.get(nextPlayerIndex);
    }

    void incrementCurrentPlayerAction() {
        fatherModel.currentPlayer = fatherModel.actionPlayerOrder.pop();
    }
}
