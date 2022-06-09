package it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.messages.game.PlayCharacterCardMessage;
import it.polimi.ingsw.mvc.view.GUI.GUIView;
import it.polimi.ingsw.mvc.view.GUI.LobbyFrame;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class CardInfoColor extends CardInfoController {
    List<CardColors> colorSelection = new ArrayList<>();

    @Override
    public void playCard(MouseEvent event) {
        Color selected = null;
        for (CardColors cc : colorSelection) {
            if (cc.getSelected()) {
                selected = cc.getColor();
                break;
            }
        }

        if (selected != null)
            GUIView.thisGUI.sendMessage(new PlayCharacterCardMessage(index, selected));
        else
            LobbyFrame.lobbyFrame.showInfo("Select a Color.");

    }

    @Override
    public void showArguments() {
        CardColors element;
        int i = 0;
        super.Arguments.getChildren().add(new Label("Select a color"));
        for (Color c : Color.values()) {
            element = new CardColors(c);
            colorSelection.add(element);
            super.Arguments.getChildren().add(element);
            element.setOnMouseClicked(this::onColorSelected);
            element.setOnMouseEntered(CardColors::shineBack);
            element.setOnMouseExited(CardColors::notShineBack);
            element.setLayoutY(20);
            element.setLayoutX(60 * i);
            i++;
        }

    }

    private void onColorSelected(MouseEvent mouseEvent) {
        for (CardColors cc : colorSelection) {
            if (cc.equals(mouseEvent.getSource())) {
                cc.setSelected(true);
            } else
                cc.setSelected(false);

            cc.update();
        }
    }

    private class CardColors extends Pane {
        ImageView color;
        boolean selected;
        Color c;
        Circle circle;

        public CardColors(Color c) {

            //this.setBackground(Background.fill(javafx.scene.paint.Color.RED));
            color = new ImageView(new Image("/imgs/Professors/" + c.toString() + ".png"));
            selected = false;
            this.c = c;
            this.getChildren().add(color);
            circle = new Circle();
            this.getChildren().add(circle);
            circle.setFill(null);
            circle.setStroke(javafx.scene.paint.Color.GREEN);
            circle.setStrokeWidth(5);
            circle.setCenterX(27.5);
            circle.setCenterY(24);
            circle.setRadius(24);
            circle.setVisible(false);
        }

        static void shineBack(MouseEvent mouseEvent) {
            ((Node) mouseEvent.getSource()).setStyle("-fx-effect: dropshadow(three-pass-box, yellow, 20, 0, 0, 0);");
            ((Node) mouseEvent.getSource()).toBack();

        }

        static void notShineBack(MouseEvent mouseEvent) {
            ((Node) mouseEvent.getSource()).setStyle("");

        }

        public boolean getSelected() {
            return this.selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public Color getColor() {
            return c;
        }

        public void update() {
            if (selected) {
                circle.setVisible(true);
            } else
                circle.setVisible(false);
        }
    }
}