package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.mvc.view.lobby.CLI.CLILobbyView;
import it.polimi.ingsw.mvc.view.CLIStringHandler.LobbyCLIStringHandler.CLILobbyNameHandler;
import it.polimi.ingsw.server.Lobby;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LobbyNamesListMessage extends ServerLobbyMessage {

    final private List<LobbyState> lobbies;

    public LobbyNamesListMessage(Map<String, Lobby> lobbyMap) {
        lobbies = new ArrayList<>();
        for (Map.Entry<String, Lobby> entry : lobbyMap.entrySet()) {
            String lobbyName = entry.getKey();
            Lobby lobby = entry.getValue();

            lobbies.add(new LobbyState(lobbyName, lobby.getCurrentPlayersCount(), lobby.getMaxPlayersCount()));
        }
    }

    public List<LobbyState> getLobbies() {
        return lobbies;
    }

    @Override
    public void updateCLI(CLILobbyView cliLobbyView) {
        StringBuilder stringBuilder = new StringBuilder();

        for (LobbyState lobbyState : lobbies) {
            stringBuilder.append("%s - %d/%d\n".formatted(
                    lobbyState.name(),
                    lobbyState.currentPlayersCount(),
                    lobbyState.maxPlayersCount()));
        }

        cliLobbyView.setFrontEnd(stringBuilder.toString());
        cliLobbyView.setCurrentQueryMessage("Choose a lobby or write 'new <new_lobby_name>' to create a new one.\n" +
                "Write 'reload' to reload the lobbies.");
        cliLobbyView.setCliStringHandler(new CLILobbyNameHandler());
    }
}
