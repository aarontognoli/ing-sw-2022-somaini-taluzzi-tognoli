package it.polimi.ingsw.mvc.view.GUI.controllers;

import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.pawn.Professor;
import it.polimi.ingsw.player.Player;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GameViewController implements Initializable {
    public Pane gamePane;
    public ImageView testPicPane;
    public Pane boards;

    List<BoardController> boardControllerList;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        testPicPane.setImage(new Image("/imgs/test/waiting.png"));
        boardControllerList = new ArrayList<>();

    }

    public void showModel(Model model) {
        testPicPane.setDisable(true);
        testPicPane.setVisible(false);
        gamePane.setDisable(false);
        gamePane.setVisible(true);
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

        updateModel(model);
    }

    private void updateModel(Model model) {


        for (int i = 0; i < model.publicModel.getTotalPlayerCount(); i++) {
            updateBoard(boardControllerList.get(i), model.publicModel.getPlayers().get(i), model.publicModel.getCurrentPlayer(), model.publicModel.getProfessors());
        }
        //todo
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
