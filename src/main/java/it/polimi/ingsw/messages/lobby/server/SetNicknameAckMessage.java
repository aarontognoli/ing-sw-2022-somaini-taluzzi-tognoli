package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.messages.ErrorMessage;
import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.CLIStringHandler.LobbyCLIStringHandler.CLIDeckNameHandler;
import it.polimi.ingsw.mvc.view.GUI.GUIView;

public class SetNicknameAckMessage extends ServerLobbyMessage {
    final private boolean isUsed;


    public SetNicknameAckMessage(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public boolean isUsed() {
        return isUsed;
    }

    @Override
    public void updateCLI(CLIView cliLobbyView) {
        if (isUsed()) {
            cliLobbyView.setFrontEnd("%s is already in use.".formatted(cliLobbyView.getMyUsername()));
        } else {
            cliLobbyView.setFrontEnd("Hello %s! Username accepted.".formatted(cliLobbyView.getMyUsername()));
            cliLobbyView.setCurrentQueryMessage("""
                    Choose a deck name:
                        'desert king',
                        'mountain sage',
                        'cloud witch',
                        'forest mage'.""");
            cliLobbyView.setCliStringHandler(new CLIDeckNameHandler());
        }
    }

    @Override
    public void updateGUI(GUIView guiLobbyView) {
        if (isUsed())
            guiLobbyView.showError(new ErrorMessage("%s is already in use.".formatted(guiLobbyView.getMyUsername())));
        else {
            //guiLobbyView.showSetDeckFrame();
        }
    }
}
