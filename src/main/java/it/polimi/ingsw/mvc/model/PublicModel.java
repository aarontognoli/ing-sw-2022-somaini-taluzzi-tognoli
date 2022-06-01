package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.bag.BagEmptyException;
import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.enums.GamePhase;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.mvc.PlayerActions;
import it.polimi.ingsw.pawn.Professor;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;
import it.polimi.ingsw.player.DiningRoomFullException;
import it.polimi.ingsw.player.Player;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class PublicModel implements PlayerActions, Serializable {
    final Model fatherModel;

    PublicModel(Model fatherModel) {
        this.fatherModel = fatherModel;
    }

    public List<Professor> getProfessors() {
        return new ArrayList<>(fatherModel.professors);
    }

    public void playAssistant(AssistantCard assistantCard) throws NotFoundException, AssistantCardAlreadyPlayedException {
        if (fatherModel.currentPlayer.getCurrentAssistantCard() != null) {
            throw new AssistantCardAlreadyPlayedException();
        }

        // If we have more than one card in the deck, check that we are not playing a card already played by someone else
        if (fatherModel.currentPlayer.getDeck().getHand().size() > 1) {
            for (Player p : fatherModel.players) {
                if (p.getCurrentAssistantCard() != null && p.getCurrentAssistantCard().equals(assistantCard)) {
                    throw new AssistantCardAlreadyPlayedException();
                }
            }
        }

        fatherModel.currentPlayer.setCurrentAssistantCard(assistantCard);
    }

    public void drawStudentsIntoEntrance(int cloudIndex) throws EntranceFullException, CloudEmptyException {

        List<Student> studentsFromCloud = fatherModel.clouds.get(cloudIndex).getStudentsAndRemove();
        if (studentsFromCloud == null)
            throw new CloudEmptyException();
        try {
            fatherModel.currentPlayer.getBoard().addStudentsToEntrance(studentsFromCloud);
        } catch (EntranceFullException e) {
            fatherModel.clouds.get(cloudIndex).putStudents(studentsFromCloud);
            throw e;
        }
    }

    //player Turn
    public void endTurn() {
        switch (fatherModel.gamePhase) {
            case PIANIFICATION -> {
                boolean everyonePlayedAnAssistantCard = true;
                for (Player p : fatherModel.players) {
                    if (p.getCurrentAssistantCard() == null) {
                        everyonePlayedAnAssistantCard = false;
                        break;
                    }
                }

                if (everyonePlayedAnAssistantCard) {
                    List<Player> playersToBeOrdered = new ArrayList<>(fatherModel.players);
                    fatherModel.actionPlayerOrder = new ArrayDeque<>(playersToBeOrdered.stream().sorted(fatherModel.privateModel::compareAssistantCardOrder).toList());
                    fatherModel.gamePhase = GamePhase.ACTION;
                    fatherModel.privateModel.incrementCurrentPlayerAction();
                    fatherModel.firstPlayer = fatherModel.currentPlayer;
                } else {
                    fatherModel.privateModel.incrementCurrentPlayer();
                }

            }

            case ACTION -> {
                if (fatherModel.actionPlayerOrder.isEmpty()) {
                    endRound();
                } else {
                    fatherModel.privateModel.incrementCurrentPlayerAction();
                }
            }
        }
    }

    //all players played their turn

    void endRound() {

        Player winner = fatherModel.privateModel.checkVictoryConditions();
        if (winner != null) {
            fatherModel.winner = winner;
            return;
        }
        for (Player p : fatherModel.players) {
            p.draftAssistantCard();
        }

        try {
            fatherModel.privateModel.fillClouds();
        } catch (BagEmptyException e) {
        }
        fatherModel.gamePhase = GamePhase.PIANIFICATION;
        fatherModel.currentPlayer = fatherModel.firstPlayer;
    }

    public Island getMotherNatureIsland() {
        return fatherModel.motherNature.getPosition();
    }

    public void moveMotherNature(int steps) throws TooMuchStepsException {
        int maxSteps = getCurrentPlayer().getMaxMotherNatureMovementValue();

        if (steps > maxSteps) {
            throw new TooMuchStepsException(maxSteps, steps);
        }

        int indexPreviousMotherNatureIsland = fatherModel.islands.indexOf(fatherModel.motherNature.getPosition());

        int finalIslandIndex = (indexPreviousMotherNatureIsland + steps) % fatherModel.islands.size();

        Island destination = fatherModel.islands.get(finalIslandIndex);
        fatherModel.motherNature.move(destination);
    }

    /**
     * @param studentColor color of the student we need to mode from entrance to
     *                     island
     * @param islandIndex  index of the target island
     * @throws NotFoundException student of this color not found in the entrance
     */
    public void moveStudentToIsland(Color studentColor, int islandIndex) throws NotFoundException {
        Student selectedStudent = fatherModel.privateModel.removeStudentFromEntrance(studentColor,
                fatherModel.currentPlayer.getBoard());
        fatherModel.privateModel.addStudentToIsland(selectedStudent, fatherModel.islands.get(islandIndex));
    }

    /**
     * @param studentColor color of the student we need to mode from entrance to
     *                     dining room
     * @throws DiningRoomFullException dining room of the corresponding color is
     *                                 full
     * @throws NotFoundException       student of this color not found in the
     *                                 entrance
     */
    public void moveStudentToDiningRoom(Color studentColor) throws DiningRoomFullException, NotFoundException {
        Student selectedStudent = fatherModel.privateModel.removeStudentFromEntrance(studentColor,
                fatherModel.currentPlayer.getBoard());

        fatherModel.privateModel.addStudentToDiningRoom(selectedStudent, fatherModel.currentPlayer.getBoard());

        // (Expert Only) If we reached one of the critical placeholder in the dining
        // room,
        // Assign a new Coin to current player
        if (fatherModel.gameMode.equals(GameMode.EXPERT_MODE)) {
            List<List<Student>> currentDiningRoom = fatherModel.currentPlayer.getBoard().getDiningRoom();
            int indexAddedStudent = currentDiningRoom.get(selectedStudent.getColor().ordinal()).size() - 1;

            if (indexAddedStudent == 2 || indexAddedStudent == 5 || indexAddedStudent == 8) {
                fatherModel.privateModel.rewardCoin();
            }
        }

        fatherModel.privateModel.updateProfessorPosition(studentColor);
    }

    public void playCharacterCard(int cardIndex, Object effectArgument)
            throws InsufficientCoinException, CCArgumentException {
        // (Expert only) Current player plays a character card
        if (fatherModel.gameMode.equals(GameMode.EASY_MODE)) {
            throw new RuntimeException("Playing character card while game mode is not expert");
        }

        CharacterCard targetCard = fatherModel.currentGameCards.get(cardIndex);

        int cardCoinNeeded = targetCard.getCoinCost();

        if (getCurrentPlayer().getBoard().getCoinCount() < cardCoinNeeded) {
            throw new InsufficientCoinException(getCurrentPlayer().getBoard().getCoinCount(), cardCoinNeeded);
        }

        targetCard.activateEffect(effectArgument);
        fatherModel.lastPlayedCharacterCard = targetCard;

        try {
            // We decrement the count after the activation of the effect since we do not
            // want to decrement the coins
            // If the effect has invalid arguments.
            getCurrentPlayer().getBoard().useCoins(cardCoinNeeded);
        } catch (InsufficientCoinException e) {
            throw new RuntimeException("Insufficient coin amount after activating character card\n"
                    + "We checked the amount just before activation (?)");
        }
    }

    // Update island owner based on influence
    public void updateIslandOwner(Island island) {
        Board playerOwnerBoard = fatherModel.privateModel.getInfluence(island);

        if (playerOwnerBoard == null)
            return;

        if (island.getTowers().size() == 0) {
            // Place tower here
            try {
                island.addTower(playerOwnerBoard.removeTower());
            } catch (TowerDifferentColorException e1) {
                e1.printStackTrace();
                throw new RuntimeException("Island had no tower in them but different color of tower was detected (?)\n"
                        + e1.getMessage());
            } catch (NoTowerException e1) {
                e1.printStackTrace();
                throw new RuntimeException(
                        "No tower in player board (?) How did they not win already?\n" + e1.getMessage());
            }
            fatherModel.privateModel.mergeIslands(island);
            if (playerOwnerBoard.getTowers().size() == 0) {
                try {
                    fatherModel.winner = fatherModel.privateModel.getPlayerFromBoard(playerOwnerBoard);
                } catch (BoardNotInGameException e) {
                    throw new RuntimeException("Impossible state of the game");
                }

            }
            return;
        }

        TowerColor islandTowerColor;
        try {
            islandTowerColor = island.getTowerColor();
        } catch (NoTowerException e) {
            e.printStackTrace();
            throw new RuntimeException("We just checked island had some towers. What? " + e.getMessage());
        }

        try {
            if (!playerOwnerBoard.getTowerColor().equals(islandTowerColor)) {
                // Remove all towers from this island
                int towerRemovedCount = island.getTowers().size();
                fatherModel.privateModel.removeAllTowers(island);

                // Place as many towers in that island
                for (int i = 0; i < towerRemovedCount; i++) {
                    try {
                        island.addTower(playerOwnerBoard.removeTower());
                    } catch (TowerDifferentColorException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Player board has no tower. What? " + e.getMessage());
                    }
                    if (playerOwnerBoard.getTowers().size() == 0) {
                        try {
                            fatherModel.winner = fatherModel.privateModel.getPlayerFromBoard(playerOwnerBoard);
                        } catch (BoardNotInGameException e) {
                            throw new RuntimeException("Impossible state of the game");
                        }

                        return;
                    }
                }
                fatherModel.privateModel.mergeIslands(island);
            }
        } catch (NoTowerException e) {
            e.printStackTrace();
            throw new RuntimeException("No towers in player board? Why did they not win? " + e.getMessage());
        }
    }

    public Player getCurrentPlayer() {
        return fatherModel.currentPlayer;
    }

    public List<Player> getPlayers() {
        return fatherModel.players;
    }

    public Player getWinner() {
        return fatherModel.winner;
    }

    public GamePhase getGamePhase() {
        return fatherModel.gamePhase;
    }

    public int getTotalPlayerCount() {
        return fatherModel.totalPlayerCount;
    }

    public void resetChecks() {
        fatherModel.characterCardPlayed = false;
        fatherModel.studentsPlaced = 0;
        fatherModel.motherNatureMoved = false;
    }

    public GameMode getGameMode() {
        return fatherModel.gameMode;
    }

    public boolean isCharacterCardPlayed() {
        return fatherModel.characterCardPlayed;
    }

    public boolean isMotherNatureMoved() {
        return fatherModel.motherNatureMoved;
    }

    public int getStudentsPlaced() {
        return fatherModel.studentsPlaced;
    }

    public int getIslandCount() {
        return fatherModel.islands.size();
    }

    public int getCloudsCount() {
        return fatherModel.clouds.size();
    }

    public void setCharacterCardPlayed(boolean characterCardPlayed) {
        fatherModel.characterCardPlayed = characterCardPlayed;
    }

    public List<CharacterCard> getCurrentGameCards() {
        return fatherModel.currentGameCards;
    }

    public void setMotherNatureMoved(boolean motherNatureMoved) {
        fatherModel.motherNatureMoved = motherNatureMoved;
    }

    public void setStudentsPlaced(int studentsPlaced) {
        fatherModel.studentsPlaced = studentsPlaced;
    }

    public boolean enoughStudentsPlaced() {
        int maxStudentsToMove = 3;
        // if entrance is empty the player must keep playing
        if (getCurrentPlayer().getBoard().getEntrance().isEmpty())
            return true;

        if (getTotalPlayerCount() == 3) {
            maxStudentsToMove = 4;
        }
        return getStudentsPlaced() >= maxStudentsToMove;
    }

    public List<Island> getIslands() {
        return fatherModel.islands;
    }
}
