package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.mvc.view.CLIStringHandler.LobbyCLIStringHandler.CLIDeckNameHandler;
import it.polimi.ingsw.mvc.view.CLI.CLIView;

public class SetNicknameAckMessage extends ServerLobbyMessage {
    final private boolean isUsed;


    public SetNicknameAckMessage(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public boolean getIsUsed() {
        return isUsed;
    }

    @Override
    public void updateCLI(CLIView cliLobbyView) {
        if(getIsUsed()) {
            cliLobbyView.setFrontEnd("This username is already in use.");
        } else {
            cliLobbyView.setFrontEnd("Good!");
            cliLobbyView.setCurrentQueryMessage("""
                    Choose a deck name:
                        'desert king',
                        'mountain sage',
                        'cloud witch',
                        'forest mage'.""");
            cliLobbyView.setCliStringHandler(new CLIDeckNameHandler());
        }
    }
}
