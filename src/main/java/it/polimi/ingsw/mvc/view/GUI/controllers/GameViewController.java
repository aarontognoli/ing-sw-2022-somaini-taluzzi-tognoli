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
import it.polimi.ingsw.pawn.Professor;
import it.polimi.ingsw.pawn.Student;
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
    public Pane AssistantCardsOuter;
    public Pane ActionOuter;
    public Pane ActionStudents;
    public Pane ActionInner;
    List<BoardController> boardControllerList;
    List<IslandController> islandControllerList;
    Map<String, String> characterCardsNameDescription;
    List<CharacterCard> characterCards;
    List<CloudController> cloudControllerList;
    List<Node> interactableParts;
    List<Student> entranceBackup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        testPicPane.setImage(new Image("/imgs/test/waiting.png"));
        testPicPane.setVisible(true);
        testPicPane.setDisable(false);
        boardControllerList = new ArrayList<>();
        islandControllerList = new ArrayList<>();
        characterCardsNameDescription = new HashMap<>();
        characterCards = new ArrayList<>();
        cloudControllerList = new ArrayList<>();
        interactableParts = new ArrayList<>();
        entranceBackup = new ArrayList<>();
        String line;


        interactableParts.add(AssistantCardsOuter);
        interactableParts.add(PlayCardButton);
        interactableParts.add(ActionOuter);
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


    private void disableInteractableParts() {
        for (Node n : interactableParts) {
            n.setDisable(true);
            n.setVisible(false);
        }
        for (Node n : islands.getChildren()) {
            removeIslandHandlers(n);
            n.setStyle("");
        }
        for (Node n : Clouds.getChildren()) {
            removeCloudHandlers(n);
            n.setStyle("");
        }
    }

    private void removeCloudHandlers(Node n) {
        if (n.getOnMouseEntered() != null)
            n.removeEventHandler(MouseEvent.MOUSE_ENTERED, n.getOnMouseEntered());
        if (n.getOnMouseExited() != null)
            n.removeEventHandler(MouseEvent.MOUSE_EXITED, n.getOnMouseExited());
        if (n.getOnMouseClicked() != null)
            n.removeEventHandler(MouseEvent.MOUSE_CLICKED, n.getOnMouseClicked());


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

    public void planningPhase(Deck thisDeck) {
        disableInteractableParts();
        Prompt.setText(TextOutputConstants.planningPhase());

        thisDeck.getHand().remove(0);
        for (AssistantCard ac : AssistantCard.values()) {
            if (thisDeck.getHand().contains(ac)) {
                ((ImageView) AssistantCards.getChildren().get(ac.ordinal())).setImage(new Image("/imgs/AssistantCards/" + ac + ".png"));
                AssistantCards.getChildren().get(ac.ordinal()).setDisable(false);

            } else {
                ((ImageView) AssistantCards.getChildren().get(ac.ordinal())).setImage(new Image("/imgs/Decks/" + thisDeck.getDeckName() + ".png"));
                AssistantCards.getChildren().get(ac.ordinal()).setDisable(true);
            }
            AssistantCards.getChildren().get(ac.ordinal()).setVisible(true);
        }
        AssistantCardsOuter.setVisible(true);
        AssistantCardsOuter.setDisable(false);
    }

    public void actionPhase(Board board, GameMode gm, boolean alreadyPlayedCharacterCard, boolean enoughStudentsMoved, boolean motherNatureMoved) {
        disableInteractableParts();
        Prompt.setText(TextOutputConstants.actionPhase(gm, alreadyPlayedCharacterCard, enoughStudentsMoved, motherNatureMoved));
        if (!alreadyPlayedCharacterCard) {
            PlayCardButton.setDisable(false);
            PlayCardButton.setVisible(true);
        }
        if (!enoughStudentsMoved) {
            int i;
            Color thisColor;
            for (i = 0; i < board.getEntrance().size(); i++) {
                thisColor = board.getEntrance().get(i).getColor();
                ((ImageView) ActionStudents.getChildren().get(i)).setImage(new Image("/imgs/Students/" + thisColor.toString() + ".png"));
                ActionStudents.getChildren().get(i).setVisible(true);
                ActionStudents.getChildren().get(i).setDisable(false);
            }
            for (; i < ActionStudents.getChildren().size(); i++) {
                ActionStudents.getChildren().get(i).setVisible(false);
                ActionStudents.getChildren().get(i).setDisable(true);
            }
            entranceBackup = board.getEntrance();
            for (Node n : islands.getChildren()) {
                n.addEventHandler(DragEvent.DRAG_ENTERED, this::dragEntered);
                n.addEventHandler(DragEvent.DRAG_EXITED, this::dragExited);
                n.addEventHandler(DragEvent.DRAG_DROPPED, this::placeStudentInIsland);
                n.addEventHandler(DragEvent.DRAG_OVER, this::acceptDrag);
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
                islands.getChildren().get(index).setStyle("-fx-effect: dropshadow(three-pass-box, green, 50, 0, 0, 0);");
                islands.getChildren().get(index).addEventHandler(MouseEvent.MOUSE_CLICKED, this::moveMotherNature);
            }

        } else {
            for (Node n : Clouds.getChildren()) {
                int index = Clouds.getChildren().indexOf(n);
                if (cloudControllerList.get(index).getSize() != 0) {
                    n.setStyle("-fx-effect: dropshadow(three-pass-box, green, 50, 0, 0, 0);");
                    n.addEventHandler(MouseEvent.MOUSE_CLICKED, this::selectCloud);
                    n.addEventHandler(MouseEvent.MOUSE_ENTERED, this::shineBack);
                    n.addEventHandler(MouseEvent.MOUSE_EXITED, this::greenBack);
                }
            }

        }

    }

    private void removeIslandHandlers(Node n) {
        if (n.getOnDragEntered() != null)
            n.removeEventHandler(DragEvent.DRAG_ENTERED, n.getOnDragEntered());
        if (n.getOnDragExited() != null)
            n.removeEventHandler(DragEvent.DRAG_EXITED, n.getOnDragExited());
        if (n.getOnDragOver() != null)
            n.removeEventHandler(DragEvent.DRAG_OVER, n.getOnDragOver());
        if (n.getOnDragDropped() != null)
            n.removeEventHandler(DragEvent.DRAG_DROPPED, n.getOnDragDropped());
        if (n.getOnMouseClicked() != null)
            n.removeEventHandler(MouseEvent.MOUSE_CLICKED, n.getOnMouseClicked());
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
        ((Node) mouseEvent.getSource()).setStyle("-fx-effect: dropshadow(three-pass-box, yellow, 50, 0, 0, 0);");

    }

    public void notShineBack(MouseEvent mouseEvent) {
        ((ImageView) mouseEvent.getSource()).setStyle("");
    }

    public void greenBack(MouseEvent mouseEvent) {
        ((Node) mouseEvent.getSource()).setStyle("-fx-effect: dropshadow(three-pass-box, green, 50, 0, 0, 0);");
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
            int index = AssistantCards.getChildren().indexOf(selected);
            GUIView.thisGUI.sendMessage(new PlayAssistantMessage(AssistantCard.values()[index]));
            return;
        }

    }

    public void startDrag(MouseEvent mouseEvent) {
        ImageView selected = (ImageView) mouseEvent.getSource();
        Dragboard db = selected.startDragAndDrop(TransferMode.COPY);
        int index = ActionStudents.getChildren().indexOf(selected);
        ClipboardContent color = new ClipboardContent();
        color.putString(entranceBackup.get(index).getColor().toString());
        db.setContent(color);
        db.setDragView(selected.getImage());
        mouseEvent.consume();
    }

    public void dragEntered(DragEvent dragEvent) {
        if (dragEvent.getGestureSource() != dragEvent.getSource() &&
                dragEvent.getDragboard().hasString()) {

            ((Node) dragEvent.getSource()).setStyle("-fx-effect: dropshadow(three-pass-box, yellow, 50, 0, 0, 0);");


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
        int index = islands.getChildren().indexOf(dragEvent.getSource());
        if (db.hasString()) {
            //Prompt.setText(Color.valueOf(db.getString()) + " Student placed in Island number "+index);
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
        int index = islands.getChildren().indexOf(mouseEvent.getSource());
        int mnIndex = GUIView.thisGUI.getMotherNatureIslandIndex();
        int islandsCount = GUIView.thisGUI.getIslandCountFromModel();
        int steps = Math.floorMod(index - mnIndex, islandsCount);
        GUIView.thisGUI.sendMessage(new MoveMotherNatureMessage(steps));
    }

    public void selectCloud(MouseEvent mouseEvent) {
        int index = Clouds.getChildren().indexOf(mouseEvent.getSource());
        GUIView.thisGUI.sendMessage(new DrawStudentIntoEntranceMessage(index));
    }
}
