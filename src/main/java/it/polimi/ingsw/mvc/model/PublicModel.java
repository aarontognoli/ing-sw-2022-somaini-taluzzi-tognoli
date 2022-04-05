package it.polimi.ingsw.mvc.model;

import java.util.List;

import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;
import it.polimi.ingsw.player.DiningRoomFullException;
import it.polimi.ingsw.player.Player;

public class PublicModel {
    final Model fatherModel;

    PublicModel(Model fatherModel) {
        this.fatherModel = fatherModel;
    }

    public void playAssistant(AssistantCard assistantCard) {
        fatherModel.currentPlayer.setCurrentAssistantCard(assistantCard);
    }

    public void drawStudentsIntoEntrance(int cloudIndex) throws EntranceFullException {

        List<Student> studentsFromCloud = fatherModel.clouds.get(cloudIndex).getStudents();
        try {
            fatherModel.currentPlayer.getBoard().addStudentsToEntrance(studentsFromCloud);
        } catch (EntranceFullException e) {
            fatherModel.clouds.get(cloudIndex).putStudents(studentsFromCloud);
            throw e;
        }
    }

    public void endTurn() {
        // TODO
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
            int indexAddedStudent = currentDiningRoom.get(selectedStudent.getColor().ordinal()).size();

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
            try {
                fatherModel.privateModel.mergeIslands(island);
                return;
            } catch (NoTowerException e) {
                e.printStackTrace();
                throw new RuntimeException(
                        "We just added a tower. How is that possible that there is no tower now?" + e.getMessage());
            }
        }

        TowerColor islandTowerColor;
        try {
            islandTowerColor = island.getTowerColor();
        } catch (NoTowerException e) {
            e.printStackTrace();
            throw new RuntimeException("We just checked island had some towers. What? " + e.getMessage());
        }

        try {
            if (playerOwnerBoard.getTowerColor().equals(islandTowerColor)) {
                // Ok, this player was already the owner. Do nothing.
                return;
            } else {
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
                        // VICTORY TODO: Notify remote-views of victory

                        return;
                    }
                }
                try {
                    fatherModel.privateModel.mergeIslands(island);
                    return;
                } catch (NoTowerException e) {
                    e.printStackTrace();
                    throw new RuntimeException(
                            "We just added a tower. How is that possible that there is no tower now?" + e.getMessage());
                }
            }
        } catch (NoTowerException e) {
            e.printStackTrace();
            throw new RuntimeException("No towers in player board? Why did they not win? " + e.getMessage());
        }
    }

    public Player getCurrentPlayer() {
        return fatherModel.currentPlayer;
    }

    /**
     * @return null if everyone has at least a tower, otherwise returns the player
     *         who has placed their last tower
     *         This method also checks for 4-players game, it only checks the board
     *         of the teammate who originally got the towers in their board
     */
    public Player checkFinishedTowers() {
        for (int i = 0; i < fatherModel.players.size(); i++) {
            // In 4 players games, just check for player 0 and 2
            if (fatherModel.totalPlayerCount != 4 || i % 2 == 0) {
                Player p = fatherModel.players.get(i);
                if (p.getBoard().getTowers().isEmpty()) {
                    return p;
                }
            }
        }

        return null;
    }

}
