package it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo;

import it.polimi.ingsw.messages.game.PlayCharacterCardMessage;
import it.polimi.ingsw.mvc.view.GUI.GUIView;
import javafx.scene.input.MouseEvent;

public class CardInfoNone extends CardInfoController {
    @Override
    public void playCard(MouseEvent event) {
        GUIView.thisGUI.sendMessage(new PlayCharacterCardMessage(index, null));
        close();
    }

    @Override
    public void showArguments() {
        return;
    }
}
