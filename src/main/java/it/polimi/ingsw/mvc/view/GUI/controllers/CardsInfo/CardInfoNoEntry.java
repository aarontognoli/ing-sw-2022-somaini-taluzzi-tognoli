package it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo;

import javafx.scene.control.Label;

public class CardInfoNoEntry extends CardInfoIsland {
    @Override
    public void showContent() {
        super.Content.getChildren().add(new Label("Show no Entry tiles"));
    }
}
