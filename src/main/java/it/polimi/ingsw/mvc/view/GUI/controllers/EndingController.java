package it.polimi.ingsw.mvc.view.GUI.controllers;

import it.polimi.ingsw.mvc.view.GUI.LobbyFrame;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class EndingController {


    public ImageView youWon;
    public Pane youLost;
    public Label whoWon;

    public void win() {
        youLost.setVisible(false);
        youWon.setVisible(true);
    }

    public void loose(String winner) {
        whoWon.setText(winner);
        youLost.setVisible(true);
        youWon.setVisible(false);
    }


    public void closeClick(MouseEvent mouseEvent) {
        LobbyFrame.lobbyFrame.closeApp();
    }

    public void closeType(KeyEvent keyEvent) {
        LobbyFrame.lobbyFrame.closeApp();
    }
}
