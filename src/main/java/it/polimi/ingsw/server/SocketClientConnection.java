package it.polimi.ingsw.server;

import it.polimi.ingsw.enums.DeckName;
import it.polimi.ingsw.exceptions.BadLobbyMessageException;
import it.polimi.ingsw.exceptions.ObjectIsNotMessageException;
import it.polimi.ingsw.messages.ErrorMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.game.GameMessage;
import it.polimi.ingsw.messages.lobby.client.SetDeckMessage;
import it.polimi.ingsw.messages.lobby.client.SetGameOptionsMessage;
import it.polimi.ingsw.messages.lobby.client.SetNicknameMessage;
import it.polimi.ingsw.messages.lobby.server.SetDeckAckMessage;
import it.polimi.ingsw.messages.lobby.server.SetNicknameAckMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClientConnection implements ClientConnection, Runnable {

    private final Socket socket;
    private final Server server;
    private ObjectInputStream socketIn;
    private ObjectOutputStream socketOut;
    private boolean active = true;

    public SocketClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    private void redirectToRemoteView(Object message) throws ObjectIsNotMessageException {
        if (!(message instanceof GameMessage)) {
            throw new ObjectIsNotMessageException();
        } else {
            ((GameMessage) message).getRemoteView().redirectMessageToController((Message) message);
        }
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
        System.out.println("A player disconnected... End of the game");
        server.closePlayersConnections();
        System.out.println("Game ended");
    }

    public void asyncSend(Object message) {
        new Thread(() -> send(message)).start();
    }

    private SetNicknameMessage waitForUsername() throws BadLobbyMessageException {
        // TODO: read from socket
        return null;
    }

    private SetDeckMessage waitForDeck() throws BadLobbyMessageException {
        // TODO: read from socket
        return null;
    }

    private SetGameOptionsMessage waitForGameOptions() throws BadLobbyMessageException {
        // TODO: read from socket
        return null;
    }

    private UsernameInUse tryAddUsername(String newUsername) {
        synchronized (server.nicknamesAndDecks) {
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

        // TODO: Prepare lobby/model with these options

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
                SetGameOptionsMessage gameOptions = null;

            }

            server.lobby(this, username);
        } catch (BadLobbyMessageException e) {
            send(new ErrorMessage(e.getMessage()));
        }

        // TODO: Listen for other messages from the network and such
    }

    private record UsernameInUse(boolean isInUse, boolean isFirstPlayer) {
    }
}
