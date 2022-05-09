package it.polimi.ingsw.server;

import it.polimi.ingsw.enums.DeckName;
import it.polimi.ingsw.exceptions.BadLobbyMessageException;
import it.polimi.ingsw.exceptions.ObjectIsNotMessageException;
import it.polimi.ingsw.messages.ErrorMessage;
import it.polimi.ingsw.messages.game.GameMessage;
import it.polimi.ingsw.messages.lobby.client.SetDeckMessage;
import it.polimi.ingsw.messages.lobby.client.SetGameOptionsMessage;
import it.polimi.ingsw.messages.lobby.client.SetNicknameMessage;
import it.polimi.ingsw.messages.lobby.server.SetDeckAckMessage;
import it.polimi.ingsw.messages.lobby.server.SetGameOptionsAckMessage;
import it.polimi.ingsw.messages.lobby.server.SetNicknameAckMessage;
import it.polimi.ingsw.mvc.view.game.RemoteView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClientConnection implements Runnable {

    private final Socket socket;
    private final Server server;
    private final ObjectInputStream socketIn;
    private final ObjectOutputStream socketOut;
    private boolean active = true;

    private RemoteView remoteView;

    public SocketClientConnection(Socket socket, Server server) throws IOException {
        this.socket = socket;
        socketIn = new ObjectInputStream(socket.getInputStream());
        socketOut = new ObjectOutputStream(socket.getOutputStream());
        this.server = server;
    }

    private void redirectToRemoteView(Object message) throws ObjectIsNotMessageException {
        if (!(message instanceof GameMessage gameMsg)) {
            throw new ObjectIsNotMessageException();
        }

        remoteView.redirectMessageToController(gameMsg);
    }

    private synchronized boolean isActive() {
        return active;
    }

    private synchronized void send(Object message) {
        try {
            socketOut.reset();
            socketOut.writeObject(message);
            socketOut.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public synchronized void closeConnection() {
        send("Connection closed!");
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error when closing socket!");
        }
        active = false;
    }

    private void close() {
        closeConnection();
        server.closePlayersConnections();
        System.out.println("Game ended");
    }

    public void asyncSend(Object message) {
        new Thread(() -> send(message)).start();
    }

    private SetNicknameMessage waitForUsername() throws BadLobbyMessageException, IOException, ClassNotFoundException {
        Object objectFromNetwork = socketIn.readObject();

        if (!(objectFromNetwork instanceof SetNicknameMessage message))
            throw new BadLobbyMessageException(objectFromNetwork);

        return message;
    }

    private SetDeckMessage waitForDeck() throws BadLobbyMessageException, IOException, ClassNotFoundException {
        Object objectFromNetwork = socketIn.readObject();

        if (!(objectFromNetwork instanceof SetDeckMessage message))
            throw new BadLobbyMessageException(objectFromNetwork);

        return message;
    }

    private SetGameOptionsMessage waitForGameOptions() throws BadLobbyMessageException, IOException, ClassNotFoundException {
        Object objectFromNetwork = socketIn.readObject();

        if (!(objectFromNetwork instanceof SetGameOptionsMessage message))
            throw new BadLobbyMessageException(objectFromNetwork);

        return message;
    }

    private UsernameInUse tryAddUsername(String newUsername) throws IOException {
        synchronized (server.nicknamesAndDecks) {
            // If this is not the first player to enter the lobby, wait for the game options to be defined
            if (server.nicknamesAndDecks.size() != 0) {
                server.currentLobby.waitForGameOptions(server);
            }

            for (String n : server.nicknamesAndDecks.keySet()) {
                if (n.equals(newUsername)) {
                    return new UsernameInUse(true, false);
                }
            }
            server.nicknamesAndDecks.put(newUsername, null);
            return new UsernameInUse(false, server.nicknamesAndDecks.size() == 1);
        }
    }

    private boolean tryAddDeckAndCheckIsUsed(String username, DeckName deckName) {
        synchronized (server.nicknamesAndDecks) {
            for (DeckName d : server.nicknamesAndDecks.values()) {
                if (d.equals(deckName)) {
                    return true;
                }
            }
            server.nicknamesAndDecks.replace(username, deckName);
            return false;
        }
    }

    private boolean tryAddGameOptionsAndCheckValid(SetGameOptionsMessage gameOptions) {
        if (gameOptions.getPlayerCount() <= 1 ||
                gameOptions.getPlayerCount() > 4 ||
                gameOptions.getMotherNatureIslandIndex() < 0 ||
                gameOptions.getMotherNatureIslandIndex() >= 12
        ) return false;

        server.currentLobby.setGameOptions(gameOptions);

        return true;
    }


    @Override
    public void run() {
        try {
            String username;
            UsernameInUse usernameInUse;
            do {
                username = waitForUsername().getNickname();
                usernameInUse = tryAddUsername(username);

                if (usernameInUse.isInUse) {
                    send(new SetNicknameAckMessage(true));
                }
            } while (usernameInUse.isInUse);

            send(new SetNicknameAckMessage(false));

            DeckName chosenDeck;
            boolean deckInUse;
            do {
                chosenDeck = waitForDeck().getDeckName();
                deckInUse = tryAddDeckAndCheckIsUsed(username, chosenDeck);
                if (deckInUse) {
                    send(new SetDeckAckMessage(false, false));
                }
            } while (deckInUse);

            send(new SetDeckAckMessage(true, usernameInUse.isFirstPlayer));

            if (usernameInUse.isFirstPlayer) {
                SetGameOptionsMessage gameOptions;

                boolean gameOptionsValid;
                do {
                    gameOptions = waitForGameOptions();
                    gameOptionsValid = tryAddGameOptionsAndCheckValid(gameOptions);
                    if (!gameOptionsValid) {
                        send(new SetGameOptionsAckMessage(false));
                    }
                } while (!gameOptionsValid);
                send(new SetGameOptionsAckMessage(true));
            }

            server.lobby(this, username);
        } catch (BadLobbyMessageException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
            send(new ErrorMessage(e.getMessage()));
            close();
            return;
        }

        // From now on, we are only expecting to be receiving GameMessages
        Object objectFromNetwork;
        while (isActive()) {
            try {
                objectFromNetwork = socketIn.readObject();
                redirectToRemoteView(objectFromNetwork);
            } catch (IOException | ClassNotFoundException | ObjectIsNotMessageException e) {
                e.printStackTrace();
                send(new ErrorMessage(e.getMessage()));
                close();
                return;
            }
        }
    }

    public void setRemoteView(RemoteView remoteView) {
        this.remoteView = remoteView;
    }

    private record UsernameInUse(boolean isInUse, boolean isFirstPlayer) {
    }
}
