package it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo;

import it.polimi.ingsw.mvc.view.GUI.LobbyFrame;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class CardInfoIsland extends CardInfoController {
    @Override
    public void playCard(MouseEvent event) {
        LobbyFrame.lobbyFrame.showInfo("Played card " + index);

    }

    @Override
    public void showArguments() {
        super.Arguments.getChildren().add(new Label("Choose Island"));
    }
}