package it.polimi.ingsw.server;

import it.polimi.ingsw.enums.DeckName;
import it.polimi.ingsw.exceptions.BadLobbyMessageException;
import it.polimi.ingsw.exceptions.ObjectIsNotMessageException;
import it.polimi.ingsw.messages.ConnectionClosedMessage;
import it.polimi.ingsw.messages.ErrorMessage;
import it.polimi.ingsw.messages.game.ClientGameMessage;
import it.polimi.ingsw.messages.lobby.client.SetDeckMessage;
import it.polimi.ingsw.messages.lobby.client.SetNicknameMessage;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.CreateLobbyMessage;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.LobbyManagementMessage;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.LobbySetupMessage;
import it.polimi.ingsw.messages.lobby.server.*;
import it.polimi.ingsw.mvc.view.RemoteView;

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
    private boolean okLobby = false;

    private RemoteView remoteView;

    public SocketClientConnection(Socket socket, Server server) throws IOException {
        this.socket = socket;
        socketIn = new ObjectInputStream(socket.getInputStream());
        socketOut = new ObjectOutputStream(socket.getOutputStream());
        this.server = server;
    }

    private void redirectToRemoteView(Object message) throws ObjectIsNotMessageException {
        if (!(message instanceof ClientGameMessage gameMsg)) {
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
        send(new ConnectionClosedMessage("Connection closed!"));
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error when closing socket!");
        }
        active = false;
    }

    private void close(Lobby whichLobby) {
        closeConnection();
        System.out.println("A player disconnected... End of the game");
        if (whichLobby != null) {
            server.closePlayersConnections(whichLobby);
            server.lobbyMap.remove(server.getNameFromLobby(whichLobby), whichLobby);
        }
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


    private UsernameInUse tryAddUsername(String newUsername, Lobby whichLobby) throws IOException {
        synchronized (whichLobby.nicknamesAndDecks) {
            // If this is not the first player to enter the lobby, wait for the game options
            // to be defined
            if (whichLobby.nicknamesAndDecks.size() != 0) {
                whichLobby.waitForGameOptions(whichLobby);
            }

            for (String n : whichLobby.nicknamesAndDecks.keySet()) {
                if (n.equals(newUsername)) {
                    return new UsernameInUse(true, false);
                }
            }
            whichLobby.nicknamesAndDecks.put(newUsername, null);
            return new UsernameInUse(false, whichLobby.nicknamesAndDecks.size() == 1);
        }
    }

    private boolean tryAddDeckAndCheckIsUsed(String username, DeckName deckName, Lobby whichLobby) {
        synchronized (whichLobby.nicknamesAndDecks) {
            for (DeckName d : whichLobby.nicknamesAndDecks.values()) {
                if (d == null) {
                    continue;
                }
                if (d.equals(deckName)) {
                    return true;
                }
            }
            whichLobby.nicknamesAndDecks.replace(username, deckName);
            return false;
        }
    }

    private boolean checkValidGameOptions(CreateLobbyMessage createLobbyMessage) {
        return createLobbyMessage.getPlayerCount() > 1 &&
                createLobbyMessage.getPlayerCount() <= 4 &&
                createLobbyMessage.getMotherNatureIslandIndex() >= 0 &&
                createLobbyMessage.getMotherNatureIslandIndex() < 12;
    }

    public ServerLobbyMessage generateLobbyNamesList() {
        return new LobbyNamesListMessage(server.lobbyMap);
    }

    public ServerLobbyMessage createNewLobby(CreateLobbyMessage message) {
        boolean areOptionsValid = false;
        synchronized (server.lobbyMap) {
            if (checkValidGameOptions(message)) {
                areOptionsValid = true;
                if (!server.lobbyMap.containsKey(message.getLobbyName())) {
                    server.lobbyMap.put(message.getLobbyName(), new Lobby());
                    okLobby = true;
                    server.lobbyMap.get(message.getLobbyName()).setGameOptions(message);
                    server.lobbyMap.get(message.getLobbyName()).playersConnections.add(this);
                    return new LobbyCreationAckMessage(true, true);
                }
            }
            return new LobbyCreationAckMessage(false, areOptionsValid);
        }
    }

    public ServerLobbyMessage joinExistingLobby(String lobbyName) {
        int currentPlayers, maxPlayers;
        for (String s : server.lobbyMap.keySet()) {
            if (s.equals(lobbyName)) {
                maxPlayers = server.lobbyMap.get(s).getMaxPlayersCount();
                currentPlayers = server.lobbyMap.get(s).getCurrentPlayersCount();
                if (currentPlayers < maxPlayers) {
                    okLobby = true;
                    server.lobbyMap.get(s).playersConnections.add(this);
                    return new LobbyNameAckMessage(true);
                }
            }
        }
        return new LobbyNameAckMessage(false);
    }

    private LobbySetupMessage createJoinLobby() throws IOException, ClassNotFoundException, BadLobbyMessageException {
        Object objectFromNetwork;
        do {
            objectFromNetwork = socketIn.readObject();
            if (objectFromNetwork instanceof LobbyManagementMessage) {
                ServerLobbyMessage messageToSend = ((LobbyManagementMessage) objectFromNetwork).callbackFunction(this);
                send(messageToSend);
            } else {
                throw new BadLobbyMessageException(objectFromNetwork);
            }

        } while (!okLobby);
        return (LobbySetupMessage) objectFromNetwork;
    }

    private String assignUsernameAndDeck(Lobby thisLobby)
            throws BadLobbyMessageException, IOException, ClassNotFoundException {
        String username;
        UsernameInUse usernameInUse;
        do {
            username = waitForUsername().getNickname();
            usernameInUse = tryAddUsername(username, thisLobby);

            if (usernameInUse.isInUse) {
                send(new SetNicknameAckMessage(true));
            }
        } while (usernameInUse.isInUse);

        send(new SetNicknameAckMessage(false));

        DeckName chosenDeck;
        boolean deckInUse;
        do {
            chosenDeck = waitForDeck().getDeckName();
            deckInUse = tryAddDeckAndCheckIsUsed(username, chosenDeck, thisLobby);
            if (deckInUse) {
                send(new SetDeckAckMessage(false, false));
            }
        } while (deckInUse);

        send(new SetDeckAckMessage(true, usernameInUse.isFirstPlayer));
        return username;
    }

    @Override
    public void run() {
        // lobby setup

        Lobby thisLobby = null;

        try {
            LobbySetupMessage receivedLobbyMessage = createJoinLobby();
            // From here no more LobbyManagement, only ClientLobbyMessages
            thisLobby = server.lobbyMap.get(receivedLobbyMessage.getLobbyName());

            String username = assignUsernameAndDeck(thisLobby);

            server.lobby(this, username, thisLobby);

            Object objectFromNetwork;
            // From now on, we are only expecting to be receiving GameMessages
            while (isActive()) {

                objectFromNetwork = socketIn.readObject();
                redirectToRemoteView(objectFromNetwork);

            }
        } catch (IOException | ClassNotFoundException | ObjectIsNotMessageException | BadLobbyMessageException e) {
            e.printStackTrace();
            send(new ErrorMessage(e.getMessage()));
            close(thisLobby);
        }
    }

    public void setRemoteView(RemoteView remoteView) {
        this.remoteView = remoteView;
    }

    private record UsernameInUse(boolean isInUse, boolean isFirstPlayer) {
    }
}
