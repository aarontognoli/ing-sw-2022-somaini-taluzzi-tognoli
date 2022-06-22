package it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.messages.game.PlayCharacterCardMessage;
import it.polimi.ingsw.mvc.view.GUI.GUIView;
import it.polimi.ingsw.mvc.view.GUI.LobbyFrame;
import it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo.selectableTokens.CardColorsToken;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class CardInfoColor extends CardInfoController {
    List<CardColorsToken> colorSelection = new ArrayList<>();

    @Override
    public void playCard(MouseEvent event) {
        Color selected = null;
        for (CardColorsToken cc : colorSelection) {
            if (cc.isSelected()) {
                selected = cc.getColor();
                break;
            }
        }

        if (selected != null) {
            GUIView.thisGUI.sendMessage(new PlayCharacterCardMessage(index, selected));
            close();
        } else
            LobbyFrame.lobbyFrame.showInfo("Select a Color.");

    }

    @Override
    public void showArguments() {
        CardColorsToken element;
        int i = 0;
        super.Arguments.getChildren().add(new Label("Select a color"));
        for (Color c : Color.values()) {
            element = new CardColorsToken(c);
            colorSelection.add(element);
            super.Arguments.getChildren().add(element);
            element.setOnMouseClicked(this::onColorSelected);
            element.setLayoutY(20);
            element.setLayoutX(60 * i);
            i++;
        }

    }

    public void onColorSelected(MouseEvent mouseEvent) {
        for (CardColorsToken cc : colorSelection) {
            if (cc.equals(mouseEvent.getSource())) {
                cc.setSelected(true);
            } else
                cc.setSelected(false);

            cc.update();
        }
    }

}