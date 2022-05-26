package it.polimi.ingsw.mvc.view.GUI.controllers;

import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.mvc.view.GUI.GUIView;
import it.polimi.ingsw.mvc.view.GUI.LobbyFrame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateLobbyController implements Initializable {
    @FXML
    public ToggleGroup gamemode;
    @FXML
    public Button goBack;
    @FXML
    public Button confirmCreation;

    @FXML
    private ComboBox motherNatureStartingPosition;
    @FXML
    private ComboBox playersNumber;
    @FXML
    private TextField lobbyName;

    private boolean back = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playersNumber.getItems().clear();
        for (int i = 2; i < 5; i++) {
            playersNumber.getItems().add(i);
        }
        playersNumber.getSelectionModel().selectFirst();

        motherNatureStartingPosition.getItems().clear();
        for (int i = 1; i < 13; i++) {
            motherNatureStartingPosition.getItems().add(i);
        }
        motherNatureStartingPosition.getSelectionModel().selectFirst();
    }

    public boolean getBack() {
        return back;
    }

    @FXML
    private void goBackButtonOnAction(ActionEvent e) {
        ((Stage) goBack.getScene().getWindow()).close();
    }

    @FXML
    private void confirmCreationButtonOnAction(ActionEvent e) {
        if (lobbyName.getText().equals("")) {
            LobbyFrame.lobbyFrame.showError("Insert a lobby name");
        } else {
            GameMode gm;
            int playersNumber;
            int motherNatureStartingIsland;

            RadioButton selected = (RadioButton) gamemode.getSelectedToggle();
            gm = GameMode.valueOf(selected.getAccessibleText());

            playersNumber = (int) this.playersNumber.getSelectionModel().getSelectedItem();
            motherNatureStartingIsland = (int) this.motherNatureStartingPosition.getSelectionModel().getSelectedItem() - 1;
            GUIView.thisGUI.createLobby(lobbyName.getText(), playersNumber, gm, motherNatureStartingIsland);
        }
    }
}
