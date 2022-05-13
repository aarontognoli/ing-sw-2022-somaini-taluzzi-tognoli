package it.polimi.ingsw.mvc.view.lobby;

import it.polimi.ingsw.messages.lobby.client.lobbysetup.RequestLobbyNamesListMessage;
import it.polimi.ingsw.messages.lobby.server.LobbyNamesListMessage;
import it.polimi.ingsw.messages.lobby.server.LobbyState;
import it.polimi.ingsw.messages.lobby.server.ServerLobbyMessage;
import it.polimi.ingsw.mvc.view.lobby.CLIStringHandler.BaseCLIStringHandler;
import it.polimi.ingsw.mvc.view.lobby.CLIStringHandler.CLILobbyNameHandler;
import it.polimi.ingsw.notifier.Notifier;

import java.util.Scanner;

/**
 * CLI client View for the lobby
 */
public class CLILobbyView extends LobbyView {

    private String frontEnd;
    private String currentQueryMessage;
    private Thread readInputThread;

    private BaseCLIStringHandler cliStringHandler;

    public CLILobbyView(Notifier<ServerLobbyMessage> modelNotifier) {
        super(modelNotifier);
    }

    public void show() {
        System.out.println(frontEnd);
        System.out.println(currentQueryMessage);
    }

    @Override
    public void run() {
        // Query lobbies from server, and set initial message
        frontEnd = "Loading lobbies...";
        currentQueryMessage = "";
        cliStringHandler = new CLILobbyNameHandler();
        show();

        notifySubscribers(new RequestLobbyNamesListMessage());

        asyncReadStdin();
    }

    @Override
    public void subscribeNotification(ServerLobbyMessage newMessage) {
        // TODO: Try to handle message in ServerLobbyMessage passing CLILobbyView as argument?
        if (newMessage instanceof LobbyNamesListMessage lobbiesMessage) {
            handleLobbyMessage(lobbiesMessage);
        } else {
            throw new RuntimeException("Not implemented yet for message of type " + newMessage.getClass().getName());
        }

        show();
    }

    private void handleLobbyMessage(LobbyNamesListMessage lobbiesMessage) {
        StringBuilder stringBuilder = new StringBuilder();

        for (LobbyState lobbyState : lobbiesMessage.getLobbies()) {
            stringBuilder.append("%s - %d/%d\n".formatted(
                    lobbyState.name(),
                    lobbyState.currentPlayersCount(),
                    lobbyState.maxPlayersCount()));
        }

        frontEnd = stringBuilder.toString();
        currentQueryMessage = "Choose a lobby or write 'new <new_lobby_name>' to create a new one";
        cliStringHandler = new CLILobbyNameHandler();
    }

    private void asyncReadStdin() {
        readInputThread = new Thread(() -> {
            final Scanner stdin = new Scanner(System.in);

            while (true) {
                String newLine = stdin.nextLine();

                notifySubscribers(cliStringHandler.generateMessageFromInput(newLine));
            }
        });

        readInputThread.start();
    }
}
