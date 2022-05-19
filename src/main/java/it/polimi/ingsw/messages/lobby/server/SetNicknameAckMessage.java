package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.mvc.view.CLI.CLIView;
import it.polimi.ingsw.mvc.view.CLIStringHandler.LobbyCLIStringHandler.CLIDeckNameHandler;

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
        if (getIsUsed()) {
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
}
