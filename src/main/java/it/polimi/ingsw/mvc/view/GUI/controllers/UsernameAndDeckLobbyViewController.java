package it.polimi.ingsw.mvc.view.GUI.controllers;

import it.polimi.ingsw.enums.DeckName;
import it.polimi.ingsw.mvc.view.GUI.GUIView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UsernameAndDeckLobbyViewController implements Initializable {
    @FXML
    public Pane deckPane;
    @FXML
    public ComboBox deckComboBox;
    @FXML
    public Button deckButton;
    @FXML
    public Pane userPane;
    @FXML
    public TextField userText;
    @FXML
    public Button userButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deckComboBox.getItems().clear();
        for (DeckName dn : DeckName.values()) {
            deckComboBox.getItems().add(dn);
        }
        deckComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void userButtonOnAction(ActionEvent e) {
        GUIView.thisGUI.setUsername(userText.getText());
    }

    @FXML
    private void deckButtonOnAction(ActionEvent e) {
        GUIView.thisGUI.setDeckName((DeckName) deckComboBox.getSelectionModel().getSelectedItem());
    }

    public void unlockDeckPane() {
        userPane.setDisable(true);
        deckPane.setDisable(false);
    }

    public void hideView() {
        ((Stage) deckButton.getScene().getWindow()).hide();
    }
}
