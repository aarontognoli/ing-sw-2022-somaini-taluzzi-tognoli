package it.polimi.ingsw.mvc.view.GUI;

import it.polimi.ingsw.enums.DeckName;
import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.messages.ErrorMessage;
import it.polimi.ingsw.messages.ServerMessage;
import it.polimi.ingsw.messages.lobby.client.SetDeckMessage;
import it.polimi.ingsw.messages.lobby.client.SetNicknameMessage;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.CreateLobbyMessage;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.JoinLobbyMessage;
import it.polimi.ingsw.messages.lobby.client.lobbysetup.RequestLobbyNamesListMessage;
import it.polimi.ingsw.mvc.model.Model;
import it.polimi.ingsw.mvc.view.ClientView;
import it.polimi.ingsw.mvc.view.GUI.controllers.LobbyController;
import it.polimi.ingsw.notifier.Notifier;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIView extends ClientView {
    public DefaultTableModel lobbyTableModel;
    //lobby window components
    public static GUIView thisGUI;

    JFrame lobby;
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

    public LobbyFrame app;

    private void setupLobbyFrameComponents() {
        clearLobbyFrame();
        JLabel lobbyTitle = new JLabel("Choose a lobby");
        top.add(lobbyTitle);
        JButton reloadButton = new JButton("Reload");
        reloadButton.addActionListener(new ReloadLobbies(this));
        top.add(reloadButton);
        lobby.add(top, BorderLayout.NORTH);
        //Makes table cells not editable
        lobbyTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        lobbyTableModel.setColumnIdentifiers(new String[]{"Lobby Name", "Number of players", "Game Mode"});
        lobbyTable = new JTable(lobbyTableModel);
        content.setLayout(new FlowLayout());
        content.add(new JScrollPane(lobbyTable), BorderLayout.CENTER);
        lobby.add(content);
        JButton createButton = new JButton("Create");
        createButton.addActionListener(new OpenCreateLobbyFrame(this));
        JButton joinButton = new JButton("Join");
        joinButton.addActionListener(new JoinLobby(this));
        bottom.add(createButton);
        bottom.add(joinButton);
        lobby.add(bottom, BorderLayout.SOUTH);
        reloadLobbies();
    }

    public LobbyController lc;

    private void clearLobbyFrame() {
        top.removeAll();
        top.repaint();
        content.removeAll();
        content.repaint();
        bottom.removeAll();
        bottom.repaint();

    }

    private void createALobbyFrame() {
        clearLobbyFrame();
        JLabel lobbyTitle = new JLabel("Create a new Lobby");
        content.setLayout(new BorderLayout());
        JPanel labels = new JPanel();
        labels.setLayout(new GridLayout(4, 1, 10, 110));
        JPanel components = new JPanel();
        components.setLayout(new GridLayout(4, 1, 10, 110));
        JTextField lobbyName = new JTextField();
        lobbyName.setMaximumSize(new Dimension(Integer.MAX_VALUE, lobbyName.getPreferredSize().height));
        components.add(new JPanel().add(lobbyName));
        labels.add(new JLabel("Lobby Name: ", SwingConstants.RIGHT));


        JComboBox<Integer> playersNumber = new JComboBox<>(new Integer[]{2, 3, 4});
        playersNumber.setMaximumSize(new Dimension(Integer.MAX_VALUE, playersNumber.getPreferredSize().height));
        components.add(playersNumber);
        labels.add(new JLabel("Players number", SwingConstants.RIGHT));

        JRadioButton easy = new JRadioButton("Easy");
        easy.setActionCommand(GameMode.EASY_MODE.toString());
        easy.setSelected(true);

        JRadioButton expert = new JRadioButton("Expert");
        expert.setActionCommand(GameMode.EXPERT_MODE.toString());

        ButtonGroup gameModeGroup = new ButtonGroup();
        gameModeGroup.add(easy);
        gameModeGroup.add(expert);

        JPanel gameModePanel = new JPanel(new FlowLayout());
        gameModePanel.add(easy);
        gameModePanel.add(expert);
        components.add(gameModePanel);
        labels.add(new JLabel("GameMode", SwingConstants.RIGHT));

        Integer[] motherNatureStartingValues = new Integer[12];
        for (int i = 0; i < 12; i++) {
            motherNatureStartingValues[i] = i + 1;
        }
        JComboBox<Integer> motherNature = new JComboBox<>(motherNatureStartingValues);
        components.add(motherNature);
        labels.add(new JLabel("Starting MotherNature Island", SwingConstants.RIGHT));


        JButton back = new JButton("Go Back");
        back.addActionListener(new OpenLobbyFrame(this));
        JButton confirm = new JButton("Confirm");
        confirm.addActionListener(new CreateLobby(this, lobbyName, playersNumber, gameModeGroup, motherNature));

        labels.setBorder(new EmptyBorder(10, 10, 10, 10));
        components.setBorder(new EmptyBorder(10, 10, 10, 10));
        top.add(lobbyTitle);
        content.add(labels, BorderLayout.WEST);
        content.add(components, BorderLayout.CENTER);
        bottom.add(back);
        bottom.add(confirm);

    }

    private void setUsernameFrame() {
        clearLobbyFrame();
        JLabel lobbyTitle = new JLabel("Log into the game");
        content.setLayout(new BorderLayout());
        JPanel labels = new JPanel();
        labels.setLayout(new GridLayout(5, 1, 10, 90));
        JPanel components = new JPanel();
        components.setLayout(new GridLayout(5, 1, 10, 90));
        JTextField username = new JTextField();
        username.setMaximumSize(new Dimension(Integer.MAX_VALUE, username.getPreferredSize().height));
        components.add(new JPanel());
        components.add(new JPanel());
        components.add(new JPanel().add(username));
        labels.add(new JPanel());
        labels.add(new JPanel());
        labels.add(new JLabel("Username: ", SwingConstants.RIGHT));


        JButton confirm = new JButton("Confirm");
        confirm.addActionListener(new SetUsername(this, username));

        labels.setBorder(new EmptyBorder(10, 10, 10, 10));
        components.setBorder(new EmptyBorder(10, 10, 10, 10));

        top.add(lobbyTitle);
        content.add(labels, BorderLayout.WEST);
        content.add(components, BorderLayout.CENTER);
        bottom.add(confirm);

    }

    private void setDeckFrame() {
        clearLobbyFrame();
        JLabel lobbyTitle = new JLabel("Log into the game");
        content.setLayout(new BorderLayout());
        JPanel labels = new JPanel();
        labels.setLayout(new GridLayout(5, 1, 10, 90));
        JPanel components = new JPanel();
        components.setLayout(new GridLayout(5, 1, 10, 90));
        JComboBox<DeckName> deckName = new JComboBox<>(DeckName.values());
        deckName.setMaximumSize(new Dimension(Integer.MAX_VALUE, deckName.getPreferredSize().height));
        components.add(new JPanel());
        components.add(new JPanel());
        components.add(deckName);
        labels.add(new JPanel());
        labels.add(new JPanel());
        labels.add(new JLabel("Deck name", SwingConstants.RIGHT));


        JButton confirm = new JButton("Confirm");
        confirm.addActionListener(new SetDeckName(this, deckName));

        labels.setBorder(new EmptyBorder(10, 10, 10, 10));
        components.setBorder(new EmptyBorder(10, 10, 10, 10));

        top.add(lobbyTitle);
        content.add(labels, BorderLayout.WEST);
        content.add(components, BorderLayout.CENTER);
        bottom.add(confirm);

    }

    @Override
    public void run() throws InterruptedException {
        //show
        app = new LobbyFrame();
        thisGUI = this;
        app.open();
        /*lobby = new JFrame("Eriantys");
        lobby.setLayout(new BorderLayout());

        lobby.setResizable(false);
        lobby.setSize(800, 600);
        lobby.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lobby.setLocationRelativeTo(null);

        setupLobbyFrameComponents();

        //todo set game components
        game = new JFrame("TODO");


        show();*/

    }

    @Override
    public void subscribeNotification(ServerMessage newMessage) {
        newMessage.updateGUI(this);

    }

    public void showError(ErrorMessage whichone) {
        JOptionPane.showMessageDialog(null, whichone.getErrorMessageString(),
                "Warning!", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfo(String title, String infoMessage) {
        JOptionPane.showMessageDialog(null, infoMessage,
                title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void showSetUsernameFrame() {
        setUsernameFrame();
        show();
    }

    public void showSetDeckFrame() {
        setDeckFrame();
        show();
    }

    public void startGame() {
        JOptionPane.showMessageDialog(null, "Start game",
                "TODO!", JOptionPane.ERROR_MESSAGE);
    }


    public void reloadLobbies() {
        notifySubscribers(new RequestLobbyNamesListMessage());
    }

    public void showCreateLobbyFrame() {
        createALobbyFrame();
        show();
    }

    public void showLobbyFrame() {
        setupLobbyFrameComponents();
        show();
    }

    public void joinLobby() {
        String lobbyName;
        try {
            lobbyName = (String) lobbyTableModel.getValueAt(lobbyTable.getSelectedRow(), 0);
            //placeholder
            notifySubscribers(new JoinLobbyMessage(lobbyName));
        } catch (Exception e) {
            showError(new ErrorMessage("Invalid lobby"));
        }

    }

    public void createLobby(String lobbyName, int playersNumber, GameMode gamemode, int motherNatureIslandIndex) {
            notifySubscribers(new CreateLobbyMessage(lobbyName, playersNumber, gamemode, motherNatureIslandIndex));
    }

    public void setUsername(String username) {
        if (username.isEmpty())
            showError(new ErrorMessage("You need to specify a username"));
        else {
            this.setMyUsername(username);
            notifySubscribers(new SetNicknameMessage(username));

        }
    }

    public void setDeckName(DeckName deckName) {
        notifySubscribers(new SetDeckMessage(deckName));
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

class JoinLobby implements ActionListener {

    GUIView gw;

    public JoinLobby(GUIView guiView) {
        this.gw = guiView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gw.joinLobby();

    }
}

class OpenCreateLobbyFrame implements ActionListener {

    GUIView gw;

    public OpenCreateLobbyFrame(GUIView guiView) {
        this.gw = guiView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gw.showCreateLobbyFrame();

    }
}

class OpenLobbyFrame implements ActionListener {

    GUIView gw;

    public OpenLobbyFrame(GUIView guiView) {
        this.gw = guiView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gw.showLobbyFrame();

    }
}

class CreateLobby implements ActionListener {

    GUIView gw;
    JTextField lobbyName;
    JComboBox<Integer> playersNumber;
    ButtonGroup gamemode;
    JComboBox<Integer> motherNature;

    public CreateLobby(GUIView guiView, JTextField lobbyName, JComboBox<Integer> playersNumber, ButtonGroup gamemode, JComboBox<Integer> motherNature) {
        this.gw = guiView;
        this.lobbyName = lobbyName;
        this.playersNumber = playersNumber;
        this.gamemode = gamemode;
        this.motherNature = motherNature;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gw.createLobby(lobbyName.getText(),
                Integer.parseInt(playersNumber.getSelectedItem().toString()),
                GameMode.valueOf(gamemode.getSelection().getActionCommand()),
                Integer.parseInt(motherNature.getSelectedItem().toString()) - 1);

    }
}

class SetUsername implements ActionListener {

    GUIView gw;
    JTextField username;


    public SetUsername(GUIView guiView, JTextField username) {
        this.gw = guiView;
        this.username = username;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gw.setUsername(username.getText());

    }
}

class SetDeckName implements ActionListener {

    GUIView gw;
    JComboBox<DeckName> deckNameJComboBox;


    public SetDeckName(GUIView guiView, JComboBox<DeckName> deckname) {
        this.gw = guiView;
        this.deckNameJComboBox = deckname;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gw.setDeckName((DeckName) deckNameJComboBox.getSelectedItem());

    }
}
