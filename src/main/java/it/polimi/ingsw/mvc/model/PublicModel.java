package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.exceptions.BagEmptyException;
import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.cloud.Cloud;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.enums.GamePhase;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.mvc.PlayerActions;
import it.polimi.ingsw.mvc.view.GUI.LobbyFrame;
import it.polimi.ingsw.pawn.Professor;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;
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

    public List<Cloud> getClouds() {
        return new ArrayList<>(fatherModel.clouds);
    }

    public List<CharacterCard> getCurrentCharacterCards() {
        return new ArrayList<>(fatherModel.currentGameCards);
    }

    public List<Professor> getProfessors() {
        return new ArrayList<>(fatherModel.professors);
    }

    /**
     * @throws AssistantCardAlreadyPlayedException current player has already played
     *                                             an assistant card, or there is more
     *                                             than one card in the deck and the card
     *                                             was already played by someone else
     * @throws NotFoundException                   assistantCard not found in deck
     */
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

    /**
     * @throws EntranceFullException entrance is full
     * @throws CloudEmptyException   cloud is empty
     */
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

    /**
     * Ends the turn of a player and assigns the next player, only if necessary
     */
    public void endTurn() {
        switch (fatherModel.gamePhase) {
            case PLANNING -> {
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

    /**
     * If all players played their turn, checks if there is a winner and
     * ends the round; if there is no winner, starts another round
     */
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
        } catch (BagEmptyException ignored) {
        }
        fatherModel.gamePhase = GamePhase.PLANNING;
        fatherModel.currentPlayer = fatherModel.firstPlayer;
    }

    /**
     * @return island where mother nature is placed
     */
    public Island getMotherNatureIsland() {
        return fatherModel.motherNature.getPosition();
    }

    /**
     * @throws TooMuchStepsException the value of steps is greater than the maximum
     *                               mother nature movement value of the current
     *                               assistant card of the current player
     */
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
     * @throws NotFoundException student of this color not found in the entrance
     */
    public void moveStudentToIsland(Color studentColor, int islandIndex) throws NotFoundException {
        Student selectedStudent = fatherModel.privateModel.removeStudentFromEntrance(studentColor,
                fatherModel.currentPlayer.getBoard());
        fatherModel.privateModel.addStudentToIsland(selectedStudent, fatherModel.islands.get(islandIndex));
    }

    /**
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

    /**
     * @throws InsufficientCoinException the player has not enough coins to play the card
     * @throws CCArgumentException       there is something not right in the argument of the card
     */
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

    /**
     * @param island island where we want to update the owner based on influence
     */
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

    /**
     * @return current player
     */
    public Player getCurrentPlayer() {
        return fatherModel.currentPlayer;
    }

    /**
     * @return list of all players
     */
    public List<Player> getPlayers() {
        return fatherModel.players;
    }

    /**
     * @return player who won the game, can be null
     */
    public Player getWinner() {
        return fatherModel.winner;
    }

    /**
     * @return current game phase (planning or action)
     */
    public GamePhase getGamePhase() {
        return fatherModel.gamePhase;
    }

    /**
     * @return number of players
     */
    public int getTotalPlayerCount() {
        return fatherModel.totalPlayerCount;
    }

    /**
     * Resets the parameters needed to check if we can end a player turn
     */
    public void resetChecks() {
        fatherModel.characterCardPlayed = false;
        fatherModel.studentsPlaced = 0;
        fatherModel.motherNatureMoved = false;
        fatherModel.lastPlayedCharacterCardIndex = -1;
    }

    /**
     * @return game mode (easy or expert)
     */
    public GameMode getGameMode() {
        return fatherModel.gameMode;
    }

    /**
     * @return true if a character card is being played in the current turn,
     * otherwise false
     */
    public boolean isCharacterCardPlayed() {
        return fatherModel.characterCardPlayed;
    }

    public int getPlayedCharacterCardIndex() {
        return fatherModel.lastPlayedCharacterCardIndex;
    }

    /**
     * @return true if mother nature has been moved in the current turn,
     * otherwise false
     */
    public boolean isMotherNatureMoved() {
        return fatherModel.motherNatureMoved;
    }

    /**
     * @return number of students moved in the current turn
     */
    public int getStudentsPlaced() {
        return fatherModel.studentsPlaced;
    }

    /**
     * @return number of islands
     */
    public int getIslandCount() {
        return fatherModel.islands.size();
    }

    /**
     * @return number of clouds
     */
    public int getCloudsCount() {
        return fatherModel.clouds.size();
    }

    /**
     * @param characterCardPlayed boolean value that indicates if a character card
     *                            has been played
     */
    public void setCharacterCardPlayed(boolean characterCardPlayed) {
        fatherModel.characterCardPlayed = characterCardPlayed;
    }

    public void setCharacterCardPlayedIndex(int index) {
        fatherModel.lastPlayedCharacterCardIndex = index;
    }

    /**
     * @return list of character cards of the match
     */
    public List<CharacterCard> getCurrentGameCards() {
        return fatherModel.currentGameCards;
    }

    /**
     * @param motherNatureMoved boolean value that indicates if mother nature
     *                          has been moved
     */
    public void setMotherNatureMoved(boolean motherNatureMoved) {
        fatherModel.motherNatureMoved = motherNatureMoved;
    }

    /**
     * @param studentsPlaced number of students placed in the current turn
     */
    public void setStudentsPlaced(int studentsPlaced) {
        fatherModel.studentsPlaced = studentsPlaced;
    }

    /**
     * @return true if there are enough students placed to end the turn,
     * otherwise false
     */
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

    /**
     * @return list of islands
     */
    public List<Island> getIslands() {
        return fatherModel.islands;
    }

    public record Winners(boolean youAreWinner, String winnersNames) {
    }

    /**
     * @param myUsername The client username, to indicate if you are the winner
     * @return a record that indicates if you are one of the winner, and the names of the winners
     */
    public Winners getWinners(String myUsername) {
        String winner = getWinner().getNickname();
        String secondWinner = "";

        boolean won = winner.equals(myUsername);

        if (getTotalPlayerCount() == 4) {
            int winnerIndex = getPlayers().indexOf(getWinner());
            secondWinner = getPlayers().get(winnerIndex + 1).getNickname();
            if (!won) {
                won = secondWinner.equals(myUsername);
            }
        }

        if (getTotalPlayerCount() == 4) {
            return new Winners(won, "%s\nand\n%s".formatted(winner, secondWinner));
        } else {
            return new Winners(won, winner);
        }
    }
}
