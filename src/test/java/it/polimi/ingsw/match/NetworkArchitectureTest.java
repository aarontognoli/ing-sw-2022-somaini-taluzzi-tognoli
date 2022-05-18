package it.polimi.ingsw.match;

import it.polimi.ingsw.messages.ConnectionClosedErrorMessage;
import it.polimi.ingsw.messages.ErrorMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.lobby.client.SetNicknameMessage;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.server.Server;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

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
        assertEquals(ConnectionClosedErrorMessage.class, client1.waitToRecieveMessage().getClass());
        assertDoesNotThrow(server::closeServer);

    }

    private Server runServerAsync() {
        Server server;
        try {
            server = new Server();
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
            return null;
        }

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

    private class ClientStub {
        private final String ip;
        private final int port;
        private Message message;
        private boolean messageToSend = false;
        private final Object messageToSendLock = new Object();
        public boolean recieved = false;
        private Message recievedMessage;

        public ClientStub(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        private boolean active = true;

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
                            recieved = true;
                            recievedMessage = message;
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
                            if (messageToSend) {

                                socketOut.writeObject(message);
                                socketOut.flush();
                                socketOut.reset();
                                messageToSend = false;
                            }
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

        public Message waitToRecieveMessage() {
            do {
                try {
                    TimeUnit.MILLISECONDS.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } while (!recieved);
            recieved = false;
            return recievedMessage;
        }

        public void setMessage(Message message) {
            this.message = message;
            synchronized (messageToSendLock) {
                messageToSend = true;
            }
        }

        public void run() throws IOException {
            Socket socket = new Socket(ip, port);
            System.out.println("Connection established");
            ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
            try {
                Thread t0 = asyncReadFromSocket(socketIn);
                Thread t1 = asyncWriteToSocket(socketOut);
                t0.join();
                t1.join();
            } catch (InterruptedException | NoSuchElementException e) {
                System.out.println("Connection closed from the client side");
            } finally {
                socketIn.close();
                socketOut.close();
                socket.close();
            }
        }
    }
}
