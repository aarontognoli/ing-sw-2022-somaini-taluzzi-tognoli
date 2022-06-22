package it.polimi.ingsw.mvc.view.GUI.controllers;

import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo.CardInfoController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CharacterCardsController extends Pane implements Initializable {
    int index;
    @FXML
    private ImageView Pic;
    @FXML
    private ImageView Played;
    private GameViewController gvc;

    public CharacterCard getThisCC() {
        return thisCC;
    }

    private CharacterCard thisCC;
    private String description;

    public CharacterCardsController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/CharacterCard.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public int getIndex() {
        return index;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String d) {
        description = d;
    }

    public String getName() {
        return thisCC.getClass().getSimpleName();
    }


    public int getCoinCost() {
        return thisCC.getCoinCost();
    }

    public CardInfoController getCardInfoController() {
        return thisCC.getCharacterCardInfoController();
    }

    public void setup(CharacterCard cc, GameViewController gvc, int index) {
        thisCC = cc;
        this.index = index;
        Pic.setImage(new Image("/imgs/CharacterCards/" + getName() + ".jpg"));
        this.gvc = gvc;
        this.setOnMouseClicked(gvc::openInfo);
        this.setOnMouseEntered(gvc::shineBack);
        this.setOnMouseExited(gvc::notShineBack);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setPlayed() {
        Played.setVisible(true);
    }

    public void setNotPlayed() {
        Played.setVisible(false);
    }
}
