package it.polimi.ingsw.match;


import it.polimi.ingsw.enums.DeckName;
import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.messages.ErrorMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.lobby.client.SetDeckMessage;
import it.polimi.ingsw.messages.lobby.client.SetNicknameMessage;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.CreateLobbyMessage;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.JoinLobbyMessage;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.RequestLobbyNamesListMessage;
import it.polimi.ingsw.messages.lobby.server.*;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.server.Server;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class NetworkArchitectureTest {
    @Test
    void NetworkTest() {
        Server server = runServerAsync();
        assertNotNull(server);
        ClientStub client1 = runClientAsync();
        ClientStub client2 = runClientAsync();
        assertTrue(client1.isActive());
        assertTrue(client2.isActive());
        client1.sendMessage(new SetNicknameMessage("Prova"));

        assertEquals(ErrorMessage.class, client1.waitToReceiveMessage().getClass());

        assertDoesNotThrow(server::closeServer);

    }

    @Test
    void LobbyCreationTest() {
        Message received1, received2, received3;
        Server server = runServerAsync();
        assertNotNull(server);
        ClientStub client1 = runClientAsync();
        ClientStub client2 = runClientAsync();
        ClientStub client3 = runClientAsync();
        assertTrue(client1.isActive());
        assertTrue(client2.isActive());

        //c1 creates lobby Prova
        client1.sendMessage(new CreateLobbyMessage("Prova", 2, GameMode.EXPERT_MODE, 1));
        received1 = client1.waitToReceiveMessage();
        assertEquals(LobbyCreationAckMessage.class, received1.getClass());
        assertTrue(((LobbyCreationAckMessage) received1).isNameValid());
        assertTrue(((LobbyCreationAckMessage) received1).areOptionsValid());

        //c2 tries to create same lobby
        client2.sendMessage(new CreateLobbyMessage("Prova", 0, GameMode.EXPERT_MODE, 1));
        received2 = client2.waitToReceiveMessage();
        assertEquals(LobbyCreationAckMessage.class, received2.getClass());
        assertFalse(((LobbyCreationAckMessage) received2).isNameValid());
        assertFalse(((LobbyCreationAckMessage) received2).areOptionsValid());

        client2.sendMessage(new JoinLobbyMessage("Prova"));
        received2 = client2.waitToReceiveMessage();
        assertEquals(LobbyNameAckMessage.class, received2.getClass());
        assertTrue(((LobbyNameAckMessage) received2).isValid());


        //c3 tries to join lobby prova, which is full
        client3.sendMessage(new RequestLobbyNamesListMessage());
        received3 = client3.waitToReceiveMessage();
        assertEquals(LobbyNamesListMessage.class, received3.getClass());
        assertEquals(2, ((LobbyNamesListMessage) received3).getLobbies().get(0).currentPlayersCount());
        client3.sendMessage(new JoinLobbyMessage("Prova"));
        received3 = client3.waitToReceiveMessage();
        assertEquals(LobbyNameAckMessage.class, received3.getClass());
        assertFalse(((LobbyNameAckMessage) received3).isValid());
        //client1 inserts username and deck
        client1.sendMessage(new SetNicknameMessage("one"));
        received1 = client1.waitToReceiveMessage();
        assertEquals(SetNicknameAckMessage.class, received1.getClass());
        assertFalse(((SetNicknameAckMessage) received1).isUsed());
        client1.sendMessage(new SetDeckMessage(DeckName.DESERT_KING));
        received1 = client1.waitToReceiveMessage();
        assertEquals(SetDeckAckMessage.class, received1.getClass());
        assertTrue(((SetDeckAckMessage) received1).isDeckValid());

        //client2 tries to insert same username and deck
        client2.sendMessage(new SetNicknameMessage("one"));
        received2 = client2.waitToReceiveMessage();
        assertEquals(SetNicknameAckMessage.class, received2.getClass());
        assertTrue(((SetNicknameAckMessage) received2).isUsed());

        client2.sendMessage(new SetNicknameMessage("two"));
        received2 = client2.waitToReceiveMessage();
        assertEquals(SetNicknameAckMessage.class, received2.getClass());
        assertFalse(((SetNicknameAckMessage) received2).isUsed());

        client2.sendMessage(new SetDeckMessage(DeckName.DESERT_KING));
        received2 = client2.waitToReceiveMessage();
        assertEquals(SetDeckAckMessage.class, received2.getClass());
        assertFalse(((SetDeckAckMessage) received2).isDeckValid());

        client2.sendMessage(new SetDeckMessage(DeckName.CLOUD_WITCH));
        received2 = client2.waitToReceiveMessage();
        assertEquals(SetDeckAckMessage.class, received2.getClass());
        assertTrue(((SetDeckAckMessage) received2).isDeckValid());


        received2 = client2.waitToReceiveMessage();
        assertEquals(GameStartMessage.class, received2.getClass());


        received1 = client1.waitToReceiveMessage();
        assertEquals(GameStartMessage.class, received1.getClass());


        System.out.println("Ended Lobby network test");

        assertDoesNotThrow(server::closeServer);

    }

    private Server runServerAsync() {
        Server server = null;
        int serverBindAttempts = 0;
        do {
            try {
                server = new Server();
            } catch (IOException e) {
                serverBindAttempts++;

                try {
                    Thread.sleep(10L * serverBindAttempts * serverBindAttempts);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    assert false;
                }
            }
        } while (serverBindAttempts < 10 && server == null);

        assertNotNull(server);

        Thread t = new Thread(server::runServer);
        t.start();

        return server;
    }

    private ClientStub runClientAsync() {
        ClientStub client = new ClientStub("127.0.0.1", 12345);
        Thread t = new Thread(() -> {
            try {
                client.run();
            } catch (IOException e) {
                e.printStackTrace();
                assert false;
            }
        });
        t.start();

        return client;
    }

    private static class ClientStub {
        public final List<Message> messageList = new LinkedList<>();
        private final String ip;
        private final int port;
        private final Object messageToSendLock = new Object();
        Socket socket;
        private Message message;
        private boolean messageToSend = false;
        private boolean active = true;

        public ClientStub(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        public synchronized boolean isActive() {
            return active;
        }

        public synchronized void setActive(boolean active) {
            this.active = active;
        }

        public Thread asyncReadFromSocket(final ObjectInputStream socketIn) {
            Thread t = new Thread(() -> {
                try {
                    while (isActive()) {
                        Object inputObject = socketIn.readObject();
                        if (inputObject instanceof Message message) {
                            System.out.println(message);
                            synchronized (messageList) {
                                messageList.add(message);
                                messageList.notifyAll();
                            }
                        } else if (inputObject instanceof Model) {
                            System.out.println("New Model received");
                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                } catch (Exception e) {
                    setActive(false);
                }
            });
            t.start();
            return t;
        }

        public Thread asyncWriteToSocket(ObjectOutputStream socketOut) {
            Thread t = new Thread(() -> {
                try {
                    while (isActive()) {
                        synchronized (messageToSendLock) {
                            while (!messageToSend) messageToSendLock.wait();

                            socketOut.writeObject(message);
                            socketOut.flush();
                            socketOut.reset();
                            messageToSend = false;
                        }
                    }
                } catch (Exception e) {
                    setActive(false);
                }
            });
            t.start();
            return t;
        }

        public void sendMessage(Message message) {
            setMessage(message);
            messageToSend = true;
        }

        public Message waitToReceiveMessage() {
            synchronized (messageList) {
                while (messageList.isEmpty())
                    assertDoesNotThrow(() -> messageList.wait());

                return messageList.remove(0);
            }
        }

        public void setMessage(Message message) {
            synchronized (messageToSendLock) {
                this.message = message;
                messageToSend = true;
                messageToSendLock.notifyAll();
            }
        }

        public void run() throws IOException {
            socket = new Socket(ip, port);
            System.out.println("Connection established");
            try (
                    ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream())
            ) {
                Thread t0 = asyncReadFromSocket(socketIn);
                Thread t1 = asyncWriteToSocket(socketOut);
                t0.join();
                t1.join();
            } catch (InterruptedException | NoSuchElementException e) {
                System.out.println("Connection closed from the client side");
            } finally {
                socket.close();
            }
        }
    }
}
