package it.polimi.ingsw.client;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.notifier.Notifier;
import it.polimi.ingsw.notifier.Subscriber;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {
    private final String ip;
    private final int port;
    final private Notifier<Model> modelNotifier;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.modelNotifier = new Notifier<>();
    }

    public void addModelSubscriber(Subscriber<Model> subscriber) {
        modelNotifier.addSubscriber(subscriber);
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
                    if (inputObject instanceof String) {
                        // TODO (view) show error message
                    } else if (inputObject instanceof Model) {
                        // TODO (view) show model
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

    // TODO GUI?
    public Thread asyncWriteToSocket(Scanner stdin, ObjectOutputStream socketOut) {
        Thread t = new Thread(() -> {
            try {
                while (isActive()) {
                    String inputLine = stdin.nextLine();
                    Message message = null;
                    // TODO create message and send it to server
                    socketOut.reset();
                    socketOut.writeObject(message);
                    socketOut.flush();
                }
            } catch (Exception e) {
                setActive(false);
            }
        });
        t.start();
        return t;
    }

    public void run() throws IOException {
        Socket socket = new Socket(ip, port);
        System.out.println("Connection established");
        ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        Scanner stdin = new Scanner(System.in);
        try {
            Thread t0 = asyncReadFromSocket(socketIn);
            Thread t1 = asyncWriteToSocket(stdin, socketOut);
            t0.join();
            t1.join();
        } catch (InterruptedException | NoSuchElementException e) {
            System.out.println("Connection closed from the client side");
        } finally {
            stdin.close();
            socketIn.close();
            socketOut.close();
            socket.close();
        }
    }
}

