package it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo;

import it.polimi.ingsw.cards.characters.HerbalistCharacter.HerbalistCharacter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CardInfoNoEntry extends CardInfoIsland {
    @Override
    public void showContent() {
        int noEntryNumber = ((HerbalistCharacter) super.thisCc).getEntryTilesInCard();
        for (int i = 0; i < noEntryNumber; i++) {
            ImageView noEntry = new ImageView(new Image("/imgs/Misc/noEntry.png"));
            super.Content.getChildren().add(noEntry);
            noEntry.setScaleX(0.5);
            noEntry.setScaleY(0.5);
            noEntry.setLayoutX(60 * i);
            noEntry.setLayoutY(0);

        }

    }
}
