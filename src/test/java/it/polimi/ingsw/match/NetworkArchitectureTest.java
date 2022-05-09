package it.polimi.ingsw.match;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.server.*;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class NetworkArchitectureTest {
    private class ClientStub {
        private final String ip;
        private final int port;
        private Message message;
        private boolean messageToSend = false;
        private final Object messageToSendLock = new Object();

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
                                socketOut.reset();
                                socketOut.writeObject(message);
                                socketOut.flush();
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

    @Test
    void NetworkTest() {
        Server server = runServerAsync();
        ClientStub client1 = runClientAsync();
        ClientStub client2 = runClientAsync();
        assertTrue(client1.isActive());
        assertTrue(client2.isActive());
    }
}
