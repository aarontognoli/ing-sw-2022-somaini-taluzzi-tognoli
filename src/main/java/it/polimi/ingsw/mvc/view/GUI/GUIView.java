package it.polimi.ingsw.mvc.view.GUI;

import it.polimi.ingsw.messages.ErrorMessage;
import it.polimi.ingsw.messages.ServerMessage;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.RequestLobbyNamesListMessage;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.ClientView;
import it.polimi.ingsw.notifier.Notifier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIView extends ClientView {
    public DefaultTableModel lobbyTableModel;
    //lobby window components
    JFrame lobby;
    JLabel lobbyTitle;
    JPanel top = new JPanel(new FlowLayout());
    JPanel content = new JPanel();
    JPanel bottom = new JPanel(new FlowLayout());
    JTable lobbyTable;
    //game window components
    JFrame game;

    public GUIView(Notifier<ServerMessage> messageNotifier, Notifier<Model> modelNotifier) {
        super(messageNotifier, modelNotifier);

    }

    @Override
    public void show() {
        if (model != null) {
            showModel();
        } else {
            showLobby();
        }
    }

    private void showLobby() {
        game.setVisible(false);
        lobby.setVisible(true);
    }

    @Override
    protected void showModel() {
        lobby.setVisible(false);
        //update Model

    }

    @Override
    public void run() throws InterruptedException {
        //show
        lobby = new JFrame("Eriantys");
        lobby.setLayout(new BorderLayout());

        lobby.setResizable(false);
        lobby.setSize(800, 600);
        lobby.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lobby.setLocationRelativeTo(null);
        lobbyTable = new JTable();

        lobbyTitle = new JLabel("Choose a lobby");

        top.setLayout(new FlowLayout());
        top.add(lobbyTitle);
        JButton reloadButton = new JButton("Reload");
        reloadButton.addActionListener(new ReloadLobbies(this));
        top.add(reloadButton);
        lobby.add(top, BorderLayout.NORTH);

        lobbyTableModel = (DefaultTableModel) lobbyTable.getModel();
        lobbyTableModel.setColumnIdentifiers(new String[]{"Lobby Name", "Number of players", "Game Mode"});
        content.add(new JScrollPane(lobbyTable), BorderLayout.CENTER);
        lobby.add(content);
        JButton createButton = new JButton("Create");
        JButton joinButton = new JButton("Join");
        bottom.add(createButton);
        bottom.add(joinButton);

        //todo set game components
        game = new JFrame("TODO");

        reloadLobbies();

        show();

    }

    @Override
    public void subscribeNotification(ServerMessage newMessage) {
        newMessage.updateGUI(this);
        show();
    }

    public void showError(ErrorMessage whichone) {
        JOptionPane.showMessageDialog(null, whichone.getErrorMessageString(),
                "Warning!", JOptionPane.ERROR_MESSAGE);
    }

    public void reloadLobbies() {
        notifySubscribers(new RequestLobbyNamesListMessage());
    }


}

class ReloadLobbies implements ActionListener {

    GUIView gw;

    public ReloadLobbies(GUIView guiView) {
        this.gw = guiView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gw.reloadLobbies();

    }
}
