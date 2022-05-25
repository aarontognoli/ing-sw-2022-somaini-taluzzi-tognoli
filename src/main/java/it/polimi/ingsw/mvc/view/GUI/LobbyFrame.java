package it.polimi.ingsw.mvc.view.GUI;

import it.polimi.ingsw.messages.lobby.server.LobbyState;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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

    public record lobbyTable(String lobbyName, String players, String gameMode) {
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
