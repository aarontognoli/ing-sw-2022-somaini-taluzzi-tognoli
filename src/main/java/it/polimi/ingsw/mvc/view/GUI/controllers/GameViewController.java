package it.polimi.ingsw.mvc.view.GUI.controllers;

import it.polimi.ingsw.exceptions.NoTowerException;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.pawn.Professor;
import it.polimi.ingsw.places.Island;
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
    private static final int ISLAND_RADIUS = 270;
    public Pane AssistantCards;
    public Pane islands;
    List<BoardController> boardControllerList;
    List<IslandController> islandControllerList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        testPicPane.setImage(new Image("/imgs/test/waiting.png"));
        boardControllerList = new ArrayList<>();
        islandControllerList = new ArrayList<>();

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
        double halfPane = islands.getHeight() / 2;
        IslandController thisController;
        islands.getChildren().clear();
        for (int i = 0; i < model.publicModel.getIslandCount(); i++) {
            thisController = new IslandController();
            islands.getChildren().add(thisController);
            islandControllerList.add(thisController);
            thisController.setLayoutY(halfPane + ISLAND_RADIUS * Math.sin(Math.toRadians((double) i * 360 / model.publicModel.getIslandCount() - 90)));
            thisController.setLayoutX(halfPane + ISLAND_RADIUS * Math.cos(Math.toRadians((double) i * 360 / model.publicModel.getIslandCount() - 90)));
            thisController.setPic(i);
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
}
