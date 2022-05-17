package it.polimi.ingsw.messages.lobby.server;

import it.polimi.ingsw.mvc.view.CLIStringHandler.LobbyCLIStringHandler.CLILobbyNameHandler;
import it.polimi.ingsw.mvc.view.lobby.CLI.CLILobbyView;
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
        cliLobbyView.setCurrentQueryMessage("""
                Type 'reload' to reload the names of the lobbies
                If you want to join a lobby, just type the lobby name
                If you want to create a new lobby, write:
                                
                new <lobby_name> <number_of_players> <game_mode> <mother_nature_island>
                                
                Where:
                  2 <= number_of_players <= 4
                  game_mode = 'easy' or 'expert'
                  1 <= mother_nature_island <= 12
                """);
        cliLobbyView.setCliStringHandler(new CLILobbyNameHandler());
    }
}
