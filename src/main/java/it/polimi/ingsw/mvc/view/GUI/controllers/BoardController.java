package it.polimi.ingsw.mvc.view.GUI.controllers;

import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.DeckName;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.pawn.Professor;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.player.Board;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class BoardController extends Pane implements Initializable {
    public Pane entrance;
    public ImageView ASSISTANT_CARD;
    public Pane GREEN_FROGS_DR;
    public Pane RED_DRAGONS_DR;
    public Pane YELLOW_GNOMES_DR;
    public Pane PINK_FAIRIES_DR;
    public Pane BLUE_UNICORNS_DR;
    public ImageView GREEN_FROGS_PROFESSOR;
    public ImageView RED_DRAGONS_PROFESSOR;
    public ImageView YELLOW_GNOMES_PROFESSOR;
    public ImageView PINK_FAIRIES_PROFESSOR;
    public ImageView BLUE_UNICORNS_PROFESSOR;
    public Pane TOWERS;
    public Pane NOW_PLAYING;
    public Label username;
    public ImageView DECK;
    public Label coin;
    public AnchorPane BACK;

    Map<Color, ImageView> professorsMap;
    Map<Color, ObservableList<Node>> diningRoomsMap;

    public BoardController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/PlayerBoard.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        professorsMap = new HashMap<>();
        professorsMap.put(Color.GREEN_FROGS, GREEN_FROGS_PROFESSOR);
        professorsMap.put(Color.RED_DRAGONS, RED_DRAGONS_PROFESSOR);
        professorsMap.put(Color.YELLOW_GNOMES, YELLOW_GNOMES_PROFESSOR);
        professorsMap.put(Color.PINK_FAIRIES, PINK_FAIRIES_PROFESSOR);
        professorsMap.put(Color.BLUE_UNICORNS, BLUE_UNICORNS_PROFESSOR);

        diningRoomsMap = new HashMap<>();
        diningRoomsMap.put(Color.GREEN_FROGS, GREEN_FROGS_DR.getChildren());
        diningRoomsMap.put(Color.RED_DRAGONS, RED_DRAGONS_DR.getChildren());
        diningRoomsMap.put(Color.YELLOW_GNOMES, YELLOW_GNOMES_DR.getChildren());
        diningRoomsMap.put(Color.PINK_FAIRIES, PINK_FAIRIES_DR.getChildren());
        diningRoomsMap.put(Color.BLUE_UNICORNS, BLUE_UNICORNS_DR.getChildren());
        ASSISTANT_CARD.setVisible(false);

    }

    public void setup(String username, DeckName deckName, TowerColor towerColor) {
        this.username.setText(username);
        DECK.setImage(new Image("/imgs/Decks/" + deckName.toString() + ".png"));
        Paint tower = null;
        Paint dash = null;
        switch (towerColor) {

            case WHITE -> {
                tower = Paint.valueOf("white");
                dash = Paint.valueOf("black");
            }
            case BLACK -> {
                tower = Paint.valueOf("black");
                dash = Paint.valueOf("white");
            }
            case GREY -> {
                tower = Paint.valueOf("gray");
                dash = Paint.valueOf("black");
            }
        }

        for (Node t : TOWERS.getChildren()) {
            ((Circle) t).setFill(tower);
            ((Circle) t).setStroke(dash);
        }

    }

    public void update(Board board) {
        updateEntrance(board.getEntrance());
        for (Color c : Color.values()) {
            updateDiningRoom(diningRoomsMap.get(c), board.getDiningRoom().get(c.ordinal()).size());
        }
        updateCoinCount(board.getCoinCount());
        updateTowers(board.getTowers().size());


    }

    public void updateProfessors(List<Professor> professors, Board thisBoard) {
        for (Professor p : professors) {
            if (p.getPosition() != null && p.getPosition().equals(thisBoard)) {
                professorsMap.get(p.getColor()).setVisible(true);
                professorsMap.get(p.getColor()).setDisable(false);
            } else {
                professorsMap.get(p.getColor()).setVisible(false);
                professorsMap.get(p.getColor()).setDisable(true);
            }
        }
    }

    public void updateIsPlaying(boolean isPlaying) {
        if (isPlaying) {
            BACK.setStyle("-fx-background-color: #55B46C;-fx-background-radius: 10;-fx-effect: dropshadow(three-pass-box, #55B46C, 100, 0, 0, 0);-fx-border-color: #ffffff;-fx-border-radius: 10;");
            NOW_PLAYING.setVisible(true);
        } else {
            BACK.setStyle("-fx-background-color: #ffffff;-fx-background-radius: 10;-fx-border-color: #ffffff;-fx-border-radius: 10;");
            NOW_PLAYING.setVisible(false);
        }
    }

    private void updateEntrance(List<Student> entrance) {
        int i;
        ImageView thisStudent;
        Color thisColor;
        for (i = 0; i < entrance.size(); i++) {
            thisColor = entrance.get(i).getColor();
            thisStudent = (ImageView) this.entrance.getChildren().get(i);
            thisStudent.setImage(new Image("/imgs/Students/" + thisColor.toString() + ".png"));
            thisStudent.setDisable(false);
            thisStudent.setVisible(true);
        }
        for (; i < this.entrance.getChildren().size(); i++) {
            thisStudent = (ImageView) this.entrance.getChildren().get(i);
            thisStudent.setDisable(true);
            thisStudent.setVisible(false);
        }

    }

    private void updateDiningRoom(ObservableList<Node> DR, int diningRoomSize) {
        int i;
        for (i = 0; i < diningRoomSize; i++) {
            DR.get(i).setDisable(false);
            DR.get(i).setVisible(true);
        }
        for (; i < DR.size(); i++) {
            DR.get(i).setDisable(true);
            DR.get(i).setVisible(false);
        }

    }

    private void updateTowers(int towerSize) {
        int i;

        for (i = 0; i < towerSize; i++) {
            TOWERS.getChildren().get(i).setDisable(false);
            TOWERS.getChildren().get(i).setVisible(true);
        }
        for (; i < TOWERS.getChildren().size(); i++) {
            TOWERS.getChildren().get(i).setDisable(true);
            TOWERS.getChildren().get(i).setVisible(false);
        }
    }

    private void updateCoinCount(int coinCount) {
        coin.setText(String.valueOf(coinCount));
    }

    public void playAssistantCard(AssistantCard card) {
        if (card != null) {
            ASSISTANT_CARD.setImage(new Image("/imgs/AssistantCards/" + card.toString() + ".png"));
            ASSISTANT_CARD.setVisible(true);
            ASSISTANT_CARD.setDisable(false);
        } else {
            ASSISTANT_CARD.setVisible(false);
            ASSISTANT_CARD.setDisable(true);
        }

    }


}
