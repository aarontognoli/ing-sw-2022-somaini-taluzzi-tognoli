package it.polimi.ingsw.mvc.view.GUI;

import it.polimi.ingsw.messages.ErrorMessage;
import it.polimi.ingsw.messages.lobby.server.LobbyState;
import it.polimi.ingsw.mvc.view.GUI.controllers.CreateLobbyController;
import it.polimi.ingsw.mvc.view.GUI.controllers.LobbyController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.List;

public class LobbyFrame extends Application {


    public static LobbyFrame lobbyFrame;
    LobbyController lc;


    Stage mainStage;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Eryantis");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LobbyView.fxml"));

        mainStage = stage;
        mainStage.setScene(new Scene((AnchorPane) loader.load()));
        lc = (LobbyController) loader.getController();
        lobbyFrame = this;
        mainStage.setResizable(false);
        mainStage.show();
        GUIView.thisGUI.reloadLobbies();
    }

    public void open() {
        launch();

    }


    public void reloadLobbies(List<LobbyState> lobbies) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lc.reloadLobbies(lobbies);
            }
        });


    }

    public void loadCreateFrame() {
        Stage createLobbyStage = new Stage();
        createLobbyStage.setTitle("Eryantis");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CreateLobbyView.fxml"));

        try {
            createLobbyStage.setScene(new Scene((AnchorPane) loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        CreateLobbyController clc = loader.getController();
        mainStage.hide();
        createLobbyStage.setResizable(false);
        createLobbyStage.showAndWait();
        if (clc.getBack())
            mainStage.show();
        else
            mainStage.close();

    }

    public void showError(String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
                alert.setTitle("Warning!");
                alert.showAndWait();
            }
        });


    }

    public void showError(ErrorMessage em) {
        showError(em.getErrorMessageString());
    }

    public void showInfo(String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
                alert.setTitle("Info!");
                alert.showAndWait();
            }
        });
    }

    public record LobbyTable(String lobbyName, String players, String gameMode) {
        public String getLobbyName() {
            return lobbyName;
        }


        public String getPlayers() {
            return players;
        }


        public String getGameMode() {
            return gameMode;
        }
    }

}
