package it.polimi.ingsw.mvc.view.GUI.controllers;

import it.polimi.ingsw.mvc.model.Model;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class GameViewController implements Initializable {
    public Pane gamePane;
    public ImageView testPicPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        testPicPane.setImage(new Image("/imgs/test/waiting.png"));
    }

    public void showModel(Model model) {
        testPicPane.setDisable(true);
        testPicPane.setVisible(false);
        gamePane.setDisable(false);
        gamePane.setVisible(true);
        updateModel(model);
    }

    private void updateModel(Model model) {

        //todo
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
}
