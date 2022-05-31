package it.polimi.ingsw.mvc.view.GUI;

import it.polimi.ingsw.messages.ErrorMessage;
import it.polimi.ingsw.messages.lobby.server.LobbyState;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.GUI.controllers.CreateLobbyController;
import it.polimi.ingsw.mvc.view.GUI.controllers.GameViewController;
import it.polimi.ingsw.mvc.view.GUI.controllers.LobbyController;
import it.polimi.ingsw.mvc.view.GUI.controllers.UsernameAndDeckLobbyViewController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.util.List;

public class LobbyFrame extends Application {


    public static LobbyFrame lobbyFrame;
    private LobbyController lc;
    private CreateLobbyController clc;
    private UsernameAndDeckLobbyViewController userAndDeckController;
    private GameViewController gvc;


    private Stage mainStage;


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
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage createLobbyStage = new Stage();
                createLobbyStage.setTitle("Eryantis");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/CreateLobbyView.fxml"));

                try {
                    createLobbyStage.setScene(new Scene((AnchorPane) loader.load()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                clc = loader.getController();
                mainStage.hide();
                createLobbyStage.initStyle(StageStyle.UNDECORATED);
                createLobbyStage.setResizable(false);
                createLobbyStage.showAndWait();
                if (clc.getBack())
                    mainStage.show();
                createLobbyStage.close();
            }
        });
    }

    public void loadUsernameAndDeckFrame() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (clc != null)
                    clc.closeAndContinue();
                Stage userAndDeckStage = new Stage();
                userAndDeckStage.setTitle("Eryantis");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UsernameAndDeckLobbyView.fxml"));

                try {
                    userAndDeckStage.setScene(new Scene((AnchorPane) loader.load()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                userAndDeckController = loader.getController();

                userAndDeckStage.initStyle(StageStyle.UNDECORATED);
                userAndDeckStage.setResizable(false);
                userAndDeckStage.show();
                mainStage.close();

            }
        });
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

    public void unlockDeckPane() {
        try {
            userAndDeckController.unlockDeckPane();
        } catch (Exception e) {
            throw new RuntimeException("userAndDeckController can't be null if method called correctly");
        }
    }

    public void showGameView() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                Stage gameStage = new Stage();
                gameStage.setTitle("Eryantis");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GameView.fxml"));

                try {
                    gameStage.setScene(new Scene((AnchorPane) loader.load()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                gvc = loader.getController();
                gameStage.setResizable(false);
                gameStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent windowEvent) {
                        Platform.exit();
                        System.exit(0);
                    }
                });
                gameStage.show();
                userAndDeckController.closeView();

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

    public void updateModel(Model model) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gvc.showModel(model);
            }
        });
    }

    public void win() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gvc.win();
            }
        });
    }

    public void showWinner(String winner) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gvc.showWinner(winner);
            }
        });
    }

    public void waitForTurn(String currentPlayerNickname) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gvc.waitForTurn(currentPlayerNickname);
            }
        });
    }

    public void planningPhase() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gvc.planningPhase();
            }
        });
    }

    public void actionPhase() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gvc.actionPhase();
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
