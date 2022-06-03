package it.polimi.ingsw.mvc.view.GUI.controllers;

import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.exceptions.NoTowerException;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.pawn.Professor;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

public class GameViewController implements Initializable {
    public Pane gamePane;
    public ImageView testPicPane;
    public Pane boards;
    private static final int ISLAND_RADIUS = 270;
    private static final int CLOUD_RADIUS = 100;
    public Pane AssistantCards;
    public Pane islands;
    public Pane CharacterCardInfo;
    public ImageView CardPic;
    public Label Description;
    public Pane CharacterCards;
    public Pane Content;
    public Text Cost;
    public Pane PlayCardButton;
    public Pane Clouds;
    public Label Prompt;
    List<BoardController> boardControllerList;
    List<IslandController> islandControllerList;
    Map<String, String> characterCardsNameDescription;
    List<CharacterCard> characterCards;
    List<CloudController> cloudControllerList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        testPicPane.setImage(new Image("/imgs/test/waiting.png"));
        boardControllerList = new ArrayList<>();
        islandControllerList = new ArrayList<>();
        characterCardsNameDescription = new HashMap<>();
        characterCards = new ArrayList<>();
        cloudControllerList = new ArrayList<>();
        String line;

        try (Scanner scanner = new Scanner(new File("./src/main/resources/utils/CharacterCardsDescriptions.csv"));) {
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                characterCardsNameDescription.put(line.split(";")[0], line.split(";")[1]);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void showModel(Model model) {
        testPicPane.setDisable(true);
        testPicPane.setVisible(false);
        gamePane.setDisable(false);
        gamePane.setVisible(true);
        if (model.publicModel.getGameMode().equals(GameMode.EXPERT_MODE)) {
            characterCards = model.publicModel.getCurrentCharacterCards();
        }
        if (boards.getChildren().size() == 0) {
            BoardController thisController;
            for (double i = 0; i < model.publicModel.getTotalPlayerCount(); i++) {
                thisController = new BoardController();

                thisController.setLayoutX(0);


                boardControllerList.add(thisController);
                boards.getChildren().add(thisController);

                double boardHeight = thisController.getChildren().get(0).boundsInLocalProperty().get().getHeight();
                double border = (boards.getHeight() - boardHeight * model.publicModel.getTotalPlayerCount()) / (model.publicModel.getTotalPlayerCount() + 1);
                thisController.setLayoutY((i + 1) * border + i * boardHeight);
            }
            if (model.publicModel.getGameMode().equals(GameMode.EXPERT_MODE)) {
                CharacterCards.setVisible(true);
                CharacterCards.setDisable(false);
                for (int i = 0; i < characterCards.size(); i++) {
                    ((ImageView) CharacterCards.getChildren().get(i)).setImage(new Image("/imgs/CharacterCards/" + characterCards.get(i).getClass().getSimpleName() + ".jpg"));
                    CharacterCards.getChildren().get(i).setAccessibleText(String.valueOf(i));
                }
            }


        }
        if (Clouds.getChildren().size() == 0) {
            CloudController thisController;
            int totalPlayers = model.publicModel.getTotalPlayerCount();
            int toDivide = totalPlayers + 1;
            int offset = 0;
            if (totalPlayers == 4)
                offset = 19;
            else if (totalPlayers == 2)
                offset = -30;

            for (int i = 0; i < model.publicModel.getCloudsCount(); i++) {
                thisController = new CloudController();
                thisController.setup(totalPlayers, i);
                cloudControllerList.add(thisController);
                Clouds.getChildren().add(thisController);
                thisController.setLayoutY(CLOUD_RADIUS * Math.sin(Math.toRadians(-(double) (i + 2) * 360 / toDivide - offset)) - 25);
                thisController.setLayoutX(CLOUD_RADIUS * Math.cos(Math.toRadians(-(double) (i + 2) * 360 / toDivide - offset)));
            }

        }
        double halfPane = islands.getHeight() / 2;
        double islandHeight;
        IslandController thisController;
        islands.getChildren().clear();
        islandControllerList.clear();
        for (int i = 0; i < model.publicModel.getIslandCount(); i++) {
            thisController = new IslandController();
            islands.getChildren().add(thisController);
            islandControllerList.add(thisController);
            islandHeight = thisController.getChildren().get(0).boundsInLocalProperty().get().getHeight();
            thisController.setLayoutY(halfPane - islandHeight + ISLAND_RADIUS * Math.sin(Math.toRadians((double) i * 360 / model.publicModel.getIslandCount() - 90)));
            thisController.setLayoutX(halfPane + ISLAND_RADIUS * Math.cos(Math.toRadians((double) i * 360 / model.publicModel.getIslandCount() - 90)));
            thisController.setPicAndIndex(i);
        }



        updateModel(model);
    }

    private void updateModel(Model model) {


        for (int i = 0; i < model.publicModel.getTotalPlayerCount(); i++) {
            updateBoard(boardControllerList.get(i), model.publicModel.getPlayers().get(i), model.publicModel.getCurrentPlayer(), model.publicModel.getProfessors());
        }

        for (int i = 0; i < model.publicModel.getIslandCount(); i++) {
            updateIsland(islandControllerList.get(i), model.publicModel.getIslands().get(i), model.publicModel.getMotherNatureIsland());
        }
        for (int i = 0; i < model.publicModel.getCloudsCount(); i++) {
            cloudControllerList.get(i).updateStudents(model.publicModel.getClouds().get(i).getStudentsWithoutEmptying());
        }
    }

    private void updateBoard(BoardController bc, Player p, Player current, List<Professor> professors) {
        if (p.equals(current)) {
            bc.updateIsPlaying(true);
        } else {
            bc.updateIsPlaying(false);
        }

        bc.setup(p.getNickname(), p.getDeck().getDeckName(), p.getTowerColor());
        bc.update(p.getBoard());
        bc.updateProfessors(professors, p.getBoard());
        bc.playAssistantCard(p.getCurrentAssistantCard());
    }

    private void updateIsland(IslandController ic, Island i, Island motherNatureIsland) {
        ic.setStudents(i.getStudents());
        ic.setNoEntryTile(i.hasNoEntryTile());
        try {
            ic.setTower(i.getTowerColor(), i.getTowers().size());
        } catch (NoTowerException e) {
            ic.setTower(null, i.getTowers().size());
        }
        ic.setMotherNature(motherNatureIsland.equals(i));

    }

    public void win() {
        //todo
    }

    public void showWinner(String winner) {
        //todo
    }

    public void waitForTurn(String currentPlayerNickname) {
        //todo
    }

    public void planningPhase() {
        //todo
    }

    public void actionPhase() {
        //todo
    }

    @FXML
    public void closeCharacterCardInfo(MouseEvent mouseEvent) {
        CharacterCardInfo.setVisible(false);
        CharacterCardInfo.setDisable(true);
    }


    public void openInfo(MouseEvent mouseEvent) {
        int cardId = Integer.parseInt(((ImageView) mouseEvent.getSource()).getAccessibleText());
        CharacterCard thisCard = characterCards.get(cardId);
        String cardName = thisCard.getClass().getSimpleName();
        Description.setText(characterCardsNameDescription.get(cardName));
        CardPic.setImage(new Image("/imgs/CharacterCards/" + cardName + ".jpg"));
        Cost.setText(String.valueOf(thisCard.getCoinCost()));
        //todo Set Content if card needs students or other things
        CharacterCardInfo.setVisible(true);
        CharacterCardInfo.setDisable(false);
    }

    public void shineBack(MouseEvent mouseEvent) {
        ((ImageView) mouseEvent.getSource()).setStyle("-fx-effect: dropshadow(three-pass-box, yellow, 50, 0, 0, 0);");
    }

    public void notShineBack(MouseEvent mouseEvent) {
        ((ImageView) mouseEvent.getSource()).setStyle("");
    }
}
