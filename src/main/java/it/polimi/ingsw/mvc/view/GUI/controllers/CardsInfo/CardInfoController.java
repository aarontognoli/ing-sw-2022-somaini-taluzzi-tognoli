package it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class CardInfoController extends Pane implements Initializable {
    public ImageView CardPic;
    public Label Description;
    public Pane Content;
    public Pane Arguments;
    public Text Cost;
    public Pane PlayCardButton;
    int index;

    public CardInfoController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/CardInfo.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setup(String name, String description, int cost, int index, boolean canPlayCharacterCard) {
        this.index = index;
        Description.setText(description);
        CardPic.setImage(new Image("/imgs/CharacterCards/" + name + ".jpg"));
        Cost.setText(String.valueOf(cost));
        showArguments();
        showContent();
        PlayCardButton.setOnMouseClicked(this::playCard);
        if (canPlayCharacterCard)
            enablePlayButton();
        else
            disablePlayButton();
    }

    public void closeOnClick(MouseEvent event) {
        this.getParent().setVisible(false);
        this.getParent().setDisable(true);
    }

    public void show() {
        this.getParent().setVisible(true);
        this.getParent().setDisable(false);
    }

    public void disablePlayButton() {
        PlayCardButton.setDisable(true);
        PlayCardButton.setVisible(false);
        Arguments.setDisable(true);

    }

    public void enablePlayButton() {
        PlayCardButton.setDisable(false);
        PlayCardButton.setVisible(true);
        Arguments.setDisable(false);
    }

    public abstract void playCard(MouseEvent event);

    public abstract void showArguments();

    public void showContent() {
        Content.setVisible(false);
        Content.setDisable(true);
    }
}
