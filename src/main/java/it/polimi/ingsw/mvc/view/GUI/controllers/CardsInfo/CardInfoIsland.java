package it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo;

import it.polimi.ingsw.messages.game.PlayCharacterCardMessage;
import it.polimi.ingsw.mvc.view.GUI.GUIView;
import it.polimi.ingsw.mvc.view.GUI.LobbyFrame;
import it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo.selectableTokens.IslandToken;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;


public class CardInfoIsland extends CardInfoController {
    List<IslandToken> tokens = new ArrayList<>();

    @Override
    public void playCard(MouseEvent event) {
        IslandToken selected = null;

        for (IslandToken it : tokens) {
            if (it.isSelected()) {
                selected = it;
                break;
            }
        }
        if (selected != null)
            GUIView.thisGUI.sendMessage(new PlayCharacterCardMessage(index, selected.getIndex()));
        else
            LobbyFrame.lobbyFrame.showInfo("Select a Color.");
    }

    @Override
    public void showArguments() {
        super.Arguments.getChildren().add(new Label("Choose an Island"));
        IslandToken it;
        for (int i = 0; i < GUIView.thisGUI.getIslandCountFromModel(); i++) {
            it = new IslandToken(i, false);
            super.Arguments.getChildren().add(it);

            it.setOnMouseClicked(this::onIslandSelected);
            it.setLayoutY(20 + (i / 6) * 50);
            it.setLayoutX(50 * (i % 6));
            tokens.add(it);
        }
    }

    public void onIslandSelected(MouseEvent mouseEvent) {
        for (IslandToken it : tokens) {
            if (it.equals(mouseEvent.getSource())) {
                it.setSelected(true);
            } else
                it.setSelected(false);

            it.update();
        }
    }

}