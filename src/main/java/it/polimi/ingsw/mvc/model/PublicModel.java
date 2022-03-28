package it.polimi.ingsw.mvc.model;

import java.util.List;

import it.polimi.ingsw.cards.Deck;
import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.cards.characters.CCArgumentException;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.DeckName;
import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.DiningRoomFullException;
import it.polimi.ingsw.player.Player;
import it.polimi.ingsw.pawn.MotherNature;

public class PublicModel {
    final Model fatherModel;

    PublicModel(Model fatherModel) {
        this.fatherModel = fatherModel;
    }

    public void playAssistant(AssistantCard assistantCard) {
        fatherModel.currentPlayer.setCurrentAssistantCard(assistantCard);
    }

    public void drawStudentsIntoEntrance(int cloudIndex) {
        List<Student> studentsFromCloud = fatherModel.clouds.get(cloudIndex).getStudents();
        fatherModel.currentPlayer.getBoard().addStudentsToEntrance(studentsFromCloud);
    }

    public void endTurn() {
        // TODO
    }

    public Island getMotherNatureIsland() {
        return fatherModel.motherNature.getPosition();
    }

    public void setPlayersCount(int playersCount) {
        fatherModel.totalPlayerCount = playersCount;
    }

    public void playerJoin(String nickname) throws Exception {
        if (fatherModel.players.size() == fatherModel.totalPlayerCount) {
            throw new Exception("Player pool full.");
        }

        TowerColor towerColor;
        if (fatherModel.totalPlayerCount == 4) {
            // 4 player rules: player 0 and 2 have WHITE towers, player 1 and 3 have BLACK
            // towers.
            towerColor = TowerColor.values()[fatherModel.players.size() % 2];
        } else {
            // 3 player rules: player 0 gets WHITE, player 1 gets BLACK, player 2 gets GREY
            towerColor = TowerColor.values()[fatherModel.players.size()];
        }

        fatherModel.players.add(new Player(nickname, towerColor));
    }

    public void setGameMode(GameMode gameMode) {
        fatherModel.gameMode = gameMode;
    }

    // Called during game preparation
    public void placeMotherNature(int islandIndex) throws Exception {
        if (fatherModel.motherNature != null) {
            throw new Exception("Mother Nature already chosen");
        }

        fatherModel.motherNature = new MotherNature(fatherModel.islands.get(islandIndex));
    }

    public void chooseDeck(int playerIndex, int deckNameOrdinal) throws Exception {
        Player targetPlayer = fatherModel.players.get(playerIndex);

        if (targetPlayer.getDeck() != null) {
            throw new PlayerAlreadyChosenDeckException(targetPlayer.getNickname());
        }

        targetPlayer.setDeck(new Deck(DeckName.values()[deckNameOrdinal]));
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
     * @param studentColor color of the student we need to mode from entrance to island
     * @param islandIndex  index of the target island
     * @throws NotFoundException student of this color not found in the entrance
     */
    public void moveStudentToIsland(Color studentColor, int islandIndex) throws NotFoundException {
        Student selectedStudent = fatherModel.privateModel.getStudentInEntrance(studentColor);
        fatherModel.privateModel.addStudentToIsland(selectedStudent, fatherModel.islands.get(islandIndex));
    }


    /**
     * @param studentColor color of the student we need to mode from entrance to dining room
     * @throws DiningRoomFullException dining room of the corresponding color is full
     * @throws NotFoundException       student of this color not found in the entrance
     */
    public void moveStudentToDiningRoom(Color studentColor) throws DiningRoomFullException, NotFoundException {
        // Move a student (of this color) from the current player's entrance
        // into their dining room
        Student selectedStudent = fatherModel.privateModel.getStudentInEntrance(studentColor);

        fatherModel.privateModel.addStudentToDiningRoom(selectedStudent, fatherModel.currentPlayer.getBoard());

        fatherModel.privateModel.removeStudentFromEntrance(selectedStudent, getCurrentPlayer().getBoard());

        // (Expert Only) If we reached one of the critical placeholder in the dining room,
        // Assign a new Coin to current player
        if (fatherModel.gameMode.equals(GameMode.EXPERT_MODE)) {
            List<List<Student>> currentDiningRoom = fatherModel.currentPlayer.getBoard().getDiningRoom();
            int indexAddedStudent = currentDiningRoom.get(selectedStudent.getColor().ordinal()).size();

            if (indexAddedStudent == 2 || indexAddedStudent == 5 || indexAddedStudent == 8) {
                fatherModel.privateModel.rewardCoin();
            }
        }

    }

    public void playCharacterCard(int cardIndex, Object effectArgument) throws InsufficientCoinException, CCArgumentException, NoMoreNoEntryTilesException {
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
            // We decrement the count after the activation of the effect since we do not want to decrement the coins
            // If the effect has invalid arguments.
            getCurrentPlayer().getBoard().useCoins(cardCoinNeeded);
        } catch (InsufficientCoinException e) {
            throw new RuntimeException("Insufficient coin amount after activating character card\n"
                    + "We checked the amount just before activation (?)");
        }
    }

    // Update island owner based on influence
    public void updateIslandOwner(Island island) {
        // TODO: Get influence correctly
        // Board playerBoard = fatherModel.privateModel.getInfluence(island);

        // TODO: Move towers, and eventually merge island, if the owner changes
    }

    public Player getCurrentPlayer() {
        return fatherModel.currentPlayer;
    }

}
