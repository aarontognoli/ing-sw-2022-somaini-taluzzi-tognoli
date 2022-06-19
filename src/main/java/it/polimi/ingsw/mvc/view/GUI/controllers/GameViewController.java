package it.polimi.ingsw.mvc.view.GUI.controllers;

import it.polimi.ingsw.cards.Deck;
import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.exceptions.NoTowerException;
import it.polimi.ingsw.messages.game.*;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.GUI.GUIView;
import it.polimi.ingsw.mvc.view.GUI.TextOutputConstants;
import it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo.CardInfoController;
import it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo.CardInfoNone;
import it.polimi.ingsw.pawn.Professor;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;
import it.polimi.ingsw.player.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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

    public Pane CharacterCards;

    public Pane Clouds;
    public Label Prompt;
    public Pane AssistantCardsOuter;
    public Pane ActionOuter;
    public Pane ActionStudents;
    public Pane ActionInner;
    List<BoardController> boardControllerList;
    List<IslandController> islandControllerList;
    Map<String, String> characterCardsNameDescription;
    List<CharacterCardsController> characterCardsControllerList;
    List<CloudController> cloudControllerList;
    List<Node> interactableParts;

    CardInfoController cardInfoController;
    boolean canPlayCharacterCard;

    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        testPicPane.setImage(new Image("/imgs/Misc/waiting.png"));
        testPicPane.setVisible(true);
        testPicPane.setDisable(false);
        boardControllerList = new ArrayList<>();
        islandControllerList = new ArrayList<>();
        characterCardsNameDescription = new HashMap<>();
        characterCardsControllerList = new ArrayList<>();
        cloudControllerList = new ArrayList<>();
        interactableParts = new ArrayList<>();

        cardInfoController = new CardInfoNone();
        canPlayCharacterCard = false;
        String line;


        interactableParts.add(AssistantCardsOuter);
        interactableParts.add(ActionOuter);
        try (Scanner scanner = new Scanner(new File("./src/main/resources/utils/CharacterCardsDescriptions.csv"));) {
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                characterCardsNameDescription.put(line.split(";")[0], line.split(";")[1]);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        CharacterCardInfo.getChildren().add(cardInfoController);

    }

    public void showModel(Model model) {
        testPicPane.setDisable(true);
        testPicPane.setVisible(false);
        gamePane.setDisable(false);
        gamePane.setVisible(true);
        List<CharacterCard> characterCards = new ArrayList<>();
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


        }
        characterCardsControllerList.clear();
        if (model.publicModel.getGameMode().equals(GameMode.EXPERT_MODE)) {
            CharacterCardsController thisCCController;
            CharacterCards.setVisible(true);
            CharacterCards.setDisable(false);
            CharacterCards.getChildren().clear();
            for (int i = 0; i < characterCards.size(); i++) {
                thisCCController = new CharacterCardsController();
                thisCCController.setup(characterCards.get(i), this, i);
                thisCCController.setDescription(characterCardsNameDescription.get(thisCCController.getName()));
                CharacterCards.getChildren().add(thisCCController);
                characterCardsControllerList.add(thisCCController);
                thisCCController.setLayoutY(i * 170);
            }
        }
        if (cloudControllerList.isEmpty()) {
            CloudController thisCloudController;
            int totalPlayers = model.publicModel.getTotalPlayerCount();
            int toDivide = totalPlayers + 1;
            int offset = 0;
            if (totalPlayers == 4)
                offset = 19;
            else if (totalPlayers == 2)
                offset = -30;
            cloudControllerList.clear();
            Clouds.getChildren().clear();
            for (int i = 0; i < model.publicModel.getCloudsCount(); i++) {
                thisCloudController = new CloudController();
                thisCloudController.setup(totalPlayers, i);
                cloudControllerList.add(thisCloudController);
                Clouds.getChildren().add(thisCloudController);
                thisCloudController.setLayoutY(CLOUD_RADIUS * Math.sin(Math.toRadians(-(double) (i + 2) * 360 / toDivide - offset)) - 25);
                thisCloudController.setLayoutX(CLOUD_RADIUS * Math.cos(Math.toRadians(-(double) (i + 2) * 360 / toDivide - offset)));
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

        for (int i = 0; i < characterCardsControllerList.size(); i++) {
            if (i == model.publicModel.getPlayedCharacterCardIndex()) {
                characterCardsControllerList.get(i).setPlayed();
            } else
                characterCardsControllerList.get(i).setNotPlayed();
        }

    }


    private void disableInteractableParts() {

        for (Node n : islands.getChildren()) {
            removeIslandHandlers(n);
            n.setStyle("");
        }
        for (Node n : Clouds.getChildren()) {
            removeCloudHandlers(n);
            n.setStyle("");
        }
        for (Node n : interactableParts) {
            n.setDisable(true);
            n.setVisible(false);
        }
        canPlayCharacterCard = false;
    }

    private void removeCloudHandlers(Node n) {
        n.setOnMouseEntered(null);
        n.setOnMouseExited(null);
        n.setOnMouseClicked(null);
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


    public void planningPhase(Deck thisDeck) {
        disableInteractableParts();
        Prompt.setText(TextOutputConstants.planningPhase());

        AssistantCards.getChildren().clear();
        ImageView thisAssistantCard;

        for (AssistantCard ac : AssistantCard.values()) {

            if (thisDeck.getHand().contains(ac)) {
                thisAssistantCard = new ImageView(new Image("/imgs/AssistantCards/" + ac + ".png"));
                thisAssistantCard.setDisable(false);

            } else {
                thisAssistantCard = new ImageView(new Image("/imgs/Decks/" + thisDeck.getDeckName() + ".png"));
                thisAssistantCard.setDisable(true);
            }
            AssistantCards.getChildren().add(thisAssistantCard);
            thisAssistantCard.setPreserveRatio(true);
            thisAssistantCard.setFitHeight(150);

            thisAssistantCard.setVisible(true);
            thisAssistantCard.setLayoutY(25);
            thisAssistantCard.setLayoutX(20 + 120 * ac.ordinal());
            thisAssistantCard.setOnMouseEntered(this::shineBackEntranceStudent);
            thisAssistantCard.setOnMouseExited(this::notShineBack);
            thisAssistantCard.setOnMouseClicked(this::playAssistant);
            thisAssistantCard.getProperties().put("ACValue", ac);
        }

        AssistantCardsOuter.setVisible(true);
        AssistantCardsOuter.setDisable(false);
        AssistantCards.setTranslateX(310);
        AssistantCards.setTranslateY(30);
        AssistantCards.setScaleX(0.7);
        AssistantCards.setScaleY(0.7);
    }

    public void actionPhase(Board board, GameMode gm, boolean alreadyPlayedCharacterCard, boolean enoughStudentsMoved, boolean motherNatureMoved) {
        disableInteractableParts();
        Prompt.setText(TextOutputConstants.actionPhase(gm, alreadyPlayedCharacterCard, enoughStudentsMoved, motherNatureMoved));
        if (!alreadyPlayedCharacterCard) {
            canPlayCharacterCard = true;
        }
        if (!enoughStudentsMoved) {
            int i;
            ImageView thisImageView;
            Color thisColor;
            ActionStudents.getChildren().clear();
            for (i = 0; i < board.getEntrance().size(); i++) {
                thisColor = board.getEntrance().get(i).getColor();
                thisImageView = new ImageView(new Image("/imgs/Students/" + thisColor.toString() + ".png"));
                thisImageView.setLayoutX(58 + (i + 1) / 2 * 94);
                thisImageView.setLayoutY(27 + i % 2 * 79);
                thisImageView.getProperties().put("color", thisColor);
                thisImageView.setPreserveRatio(true);
                thisImageView.setFitWidth(76);
                thisImageView.setVisible(true);
                thisImageView.setDisable(false);
                thisImageView.setOnDragDetected(this::startDrag);
                thisImageView.setOnMouseEntered(this::shineBackEntranceStudent);
                thisImageView.setOnMouseExited(this::notShineBack);

                ActionStudents.getChildren().add(thisImageView);
            }

            for (Node n : islands.getChildren()) {
                n.setOnDragEntered(this::dragEntered);
                n.setOnDragExited(this::dragExited);
                n.setOnDragOver(this::acceptDrag);
                n.setOnDragDropped(this::placeStudentInIsland);

            }

            ActionOuter.setDisable(false);
            ActionOuter.setVisible(true);
        } else if (!motherNatureMoved) {
            int index;
            int mnIndex = GUIView.thisGUI.getMotherNatureIslandIndex();
            int islandsCount = GUIView.thisGUI.getIslandCountFromModel();
            int mnMaxMov = GUIView.thisGUI.getMotherNatureMaxMovement();
            for (int i = 0; i < mnMaxMov; i++) {
                index = (mnIndex + i + 1) % islandsCount;
                islands.getChildren().get(index).setStyle("-fx-effect: dropshadow(three-pass-box, #E6F3EA, 75, 0.5, 0, 0);");

                islands.getChildren().get(index).setOnMouseClicked(this::moveMotherNature);
            }

        } else {
            for (Node n : Clouds.getChildren()) {
                int index = Clouds.getChildren().indexOf(n);
                if (cloudControllerList.get(index).getSize() != 0) {
                    n.setStyle("-fx-effect: dropshadow(three-pass-box, #E6F3EA, 75, 0.5, 0, 0);");
                    n.setOnMouseEntered(this::shineBack);
                    n.setOnMouseExited(this::greenBack);
                    n.setOnMouseClicked(this::selectCloud);

                }
            }

        }

    }

    private void removeIslandHandlers(Node n) {
        n.setOnMouseClicked(null);
        n.setOnDragEntered(null);
        n.setOnDragExited(null);
        n.setOnDragOver(null);
        n.setOnDragDropped(null);
    }

    @FXML
    public void closeCharacterCardInfo(MouseEvent mouseEvent) {
        CharacterCardInfo.setVisible(false);
        CharacterCardInfo.setDisable(true);
    }


    private void openInfo(String cardName, String description, int coinCost, CardInfoController cic, int index, CharacterCard cc) {
        cardInfoController = cic;
        CharacterCardInfo.getChildren().clear();
        cardInfoController.setup(cardName, description, coinCost, index, canPlayCharacterCard, cc);
        CharacterCardInfo.getChildren().add(cardInfoController);
        cardInfoController.show();
    }

    public void shineBack(MouseEvent mouseEvent) {
        ((Node) mouseEvent.getSource()).setStyle("-fx-effect: dropshadow(three-pass-box, #1E6C93, 50, 0, 0, 0);");
    }

    public void shineBackEntranceStudent(MouseEvent mouseEvent) {
        shineBack(mouseEvent);
        ((Node) mouseEvent.getSource()).toBack();
    }

    public void notShineBack(MouseEvent mouseEvent) {
        ((Node) mouseEvent.getSource()).setStyle("");
    }

    public void greenBack(MouseEvent mouseEvent) {
        ((Node) mouseEvent.getSource()).setStyle("-fx-effect: dropshadow(three-pass-box, #E6F3EA, 75, 0.5, 0, 0);");
    }

    public void makeAssistantsNormal(MouseEvent mouseEvent) {
        AssistantCards.setTranslateX(0);
        AssistantCards.setTranslateY(0);
        AssistantCards.setScaleX(1);
        AssistantCards.setScaleY(1);
    }

    public void makeAssistantsSmall(MouseEvent mouseEvent) {
        AssistantCards.setTranslateX(310);
        AssistantCards.setTranslateY(30);
        AssistantCards.setScaleX(0.7);
        AssistantCards.setScaleY(0.7);
    }

    public void waitForTurn(String currentPlayerNickname) {
        disableInteractableParts();
        Prompt.setText(TextOutputConstants.notMyTurn(currentPlayerNickname));

    }


    public void playAssistant(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {

            ImageView selected = (ImageView) mouseEvent.getSource();
            //Prompt.setText("Played "+selected.getProperties().get("ACValue").toString());
            GUIView.thisGUI.sendMessage(new PlayAssistantMessage((AssistantCard) selected.getProperties().get("ACValue")));
            return;
        }

    }

    public void startDrag(MouseEvent mouseEvent) {
        ImageView selected = (ImageView) mouseEvent.getSource();
        Dragboard db = selected.startDragAndDrop(TransferMode.COPY);
        ClipboardContent color = new ClipboardContent();
        color.putString(selected.getProperties().get("color").toString());
        db.setContent(color);
        db.setDragView(selected.getImage());
        mouseEvent.consume();
    }

    public void dragEntered(DragEvent dragEvent) {
        if (dragEvent.getGestureSource() != dragEvent.getSource() &&
                dragEvent.getDragboard().hasString()) {

            ((Node) dragEvent.getSource()).setStyle("-fx-effect: dropshadow(three-pass-box, #1E6C93, 50, 0, 0, 0);");

            ((Node) dragEvent.getSource()).toBack();


        }
        dragEvent.consume();
    }

    public void dragEnteredLabel(DragEvent dragEvent) {
        if (dragEvent.getGestureSource() != dragEvent.getSource() &&
                dragEvent.getDragboard().hasString()) {

            ((Node) dragEvent.getSource()).setStyle("-fx-effect: dropshadow(three-pass-box, #1E6C93, 50, 0, 0, 0);");


        }
        dragEvent.consume();
    }

    public void dragExited(DragEvent dragEvent) {
        if (dragEvent.getGestureSource() != dragEvent.getSource() &&
                dragEvent.getDragboard().hasString()) {
            ((Node) dragEvent.getSource()).setStyle("");

        }
        dragEvent.consume();
    }


    public void placeStudentInDiningRoom(DragEvent dragEvent) {
        Dragboard db = dragEvent.getDragboard();
        boolean success = false;
        if (db.hasString()) {
            //Prompt.setText(Color.valueOf(db.getString()) + " Student placed in Dining Room");
            GUIView.thisGUI.sendMessage(new MoveStudentToDiningRoomMessage(Color.valueOf(db.getString())));
            success = true;
        }

        dragEvent.setDropCompleted(success);

        dragEvent.consume();
    }

    public void placeStudentInIsland(DragEvent dragEvent) {
        Dragboard db = dragEvent.getDragboard();
        boolean success = false;
        int index = ((IslandController) dragEvent.getSource()).getIndex();
        if (db.hasString()) {
            //Prompt.setText(Color.valueOf(db.getString()) + " Student placed in Island number " + index);
            GUIView.thisGUI.sendMessage(new MoveStudentToIslandMessage(Color.valueOf(db.getString()), index));
            success = true;
        }

        dragEvent.setDropCompleted(success);

        dragEvent.consume();
    }

    public void acceptDrag(DragEvent dragEvent) {
        if (dragEvent.getGestureSource() != dragEvent.getSource() &&
                dragEvent.getDragboard().hasString()) {

            dragEvent.acceptTransferModes(TransferMode.COPY);


        }
        dragEvent.consume();

    }

    public void moveMotherNature(MouseEvent mouseEvent) {
        int index = ((IslandController) mouseEvent.getSource()).getIndex();
        int mnIndex = GUIView.thisGUI.getMotherNatureIslandIndex();
        int islandsCount = GUIView.thisGUI.getIslandCountFromModel();
        int steps = Math.floorMod(index - mnIndex, islandsCount);
        GUIView.thisGUI.sendMessage(new MoveMotherNatureMessage(steps));
    }

    public void selectCloud(MouseEvent mouseEvent) {
        int index = Clouds.getChildren().indexOf(mouseEvent.getSource());
        GUIView.thisGUI.sendMessage(new DrawStudentIntoEntranceMessage(index));
    }

    public void openInfo(MouseEvent mouseEvent) {
        CharacterCardsController caller = (CharacterCardsController) mouseEvent.getSource();
        openInfo(caller.getName(), caller.getDescription(), caller.getCoinCost(), caller.getCardInfoController(), caller.getIndex(), caller.getThisCC());
    }
}
