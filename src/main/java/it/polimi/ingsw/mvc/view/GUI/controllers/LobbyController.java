package it.polimi.ingsw.mvc.view.GUI.controllers;

import it.polimi.ingsw.messages.lobby.server.LobbyState;
import it.polimi.ingsw.mvc.view.GUI.GUIView;
import it.polimi.ingsw.mvc.view.GUI.LobbyFrame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LobbyController implements Initializable {
    @FXML
    public Button reloadButton;
    @FXML
    public TableColumn nameColumn;
    @FXML
    public TableColumn playersColumn;
    @FXML
    public TableColumn gamemodeColumn;
    @FXML
    private Button createButton;

    @FXML
    private Button joinButton;

    @FXML
    private TableView table;


    public void reloadLobbies(List<LobbyState> lobbies) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("lobbyName"));
        playersColumn.setCellValueFactory(new PropertyValueFactory<>("players"));
        gamemodeColumn.setCellValueFactory(new PropertyValueFactory<>("gameMode"));
        table.getItems().clear();

        for (LobbyState ls : lobbies) {
            table.getItems().add(new LobbyFrame.LobbyTable(ls.name(), ls.currentPlayersCount() + "/" + ls.maxPlayersCount(), ls.gameMode().toString()));

        }

        table.refresh();

    }

    @FXML
    private void reloadButtonOnAction(ActionEvent e) {
        GUIView.thisGUI.reloadLobbies();
    }

    @FXML
    private void createButtonOnAction(ActionEvent e) {
        GUIView.thisGUI.loadCreateFrame();
    }

    @FXML
    private void joinButtonOnAction(ActionEvent e) {
        String lobbyName;
        try {
            LobbyFrame.LobbyTable selected = (LobbyFrame.LobbyTable) table.getSelectionModel().getSelectedItem();
            lobbyName = selected.getLobbyName();
        } catch (Exception exc) {
            lobbyName = null;
        }

        GUIView.thisGUI.joinLobby(lobbyName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
