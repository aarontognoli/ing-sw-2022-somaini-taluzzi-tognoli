package it.polimi.ingsw.mvc.view.lobby.CLI;

import it.polimi.ingsw.mvc.view.lobby.CLI.CLIStringHandler.BaseCLIStringHandler;

public record CLILobbyViewUpdate(String newFrontEnd, String newCurrentQueryMessage,
                                 BaseCLIStringHandler newCliStringHandler) {
}
