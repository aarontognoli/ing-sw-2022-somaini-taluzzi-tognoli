package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.cards.assistant.AssistantCard;
import it.polimi.ingsw.cards.characters.CharacterCard;
import it.polimi.ingsw.cards.characters.CharacterCardWithStudents;
import it.polimi.ingsw.cloud.Cloud;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.GameMode;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.exceptions.NoTowerException;
import it.polimi.ingsw.pawn.Professor;
import it.polimi.ingsw.pawn.Student;
import it.polimi.ingsw.pawn.Tower;
import it.polimi.ingsw.places.Island;
import it.polimi.ingsw.player.Board;
import it.polimi.ingsw.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CLIModelPrinter {

    // Declaring ANSI_RESET so that we can reset the color
    public static final String ANSI_RESET = "\u001B[0m";

    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_PINK = "\u001B[35m";
    public static final String ANSI_BLUE = "\u001B[34m";

    private static String getAnsiColor(Color color) {
        String ANSI_COLOR = ANSI_RESET;
        switch (color) {
            case GREEN_FROGS -> ANSI_COLOR = ANSI_GREEN;
            case RED_DRAGONS -> ANSI_COLOR = ANSI_RED;
            case YELLOW_GNOMES -> ANSI_COLOR = ANSI_YELLOW;
            case PINK_FAIRIES -> ANSI_COLOR = ANSI_PINK;
            case BLUE_UNICORNS -> ANSI_COLOR = ANSI_BLUE;
        }
        return ANSI_COLOR;
    }

    static void printStudent(Color color) {
        System.out.print(getAnsiColor(color) + "*" + ANSI_RESET + " ");
    }

    static void printProfessor(Color color) {
        System.out.print(getAnsiColor(color) + "P" + ANSI_RESET + " ");
    }

    static void printTower(TowerColor color) {
        String towerColor = switch (color) {
            case BLACK -> "B";
            case GREY -> "G";
            case WHITE -> "W";
        };
        System.out.print(towerColor + " ");
    }

    static void printListStudent(List<Student> students) {
        List<Integer> amountsPerColor = new ArrayList<>(
                Collections.nCopies(Color.values().length, 0));

        for (Student student : students) {
            int index = student.getColor().ordinal();
            amountsPerColor.set(index, amountsPerColor.get(index) + 1);
        }

        for (int i = 0; i < Color.values().length; i++) {
            int amountPerColor = amountsPerColor.get(i);

            for (int j = 0; j < amountPerColor; j++) {
                printStudent(Color.values()[i]);
            }
        }
    }

    static void printEntrance(Board board) {
        System.out.print("Entrance: ");
        if (board.getEntrance().isEmpty()) {
            return;
        }
        printListStudent(board.getEntrance());
        System.out.print("\n");
    }

    static void printDiningRoom(Board board, Model model) {
        System.out.println("Dining room:");
        for (List<Student> students : board.getDiningRoom()) {
            if (students.isEmpty()) {
                continue;
            }
            Color color = students.get(0).getColor();
            for (int i = 0; i < students.size(); i++) {
                printStudent(color);
            }
            for (Professor professor : model.professors) {
                if (professor.getColor().equals(color) && professor.getPosition() != null) {
                    if (professor.getPosition().equals(board)) {
                        printProfessor(color);
                    }
                }
            }
            System.out.print("\n");
        }
    }

    static void printTowers(Board board) {
        System.out.print("Towers: ");
        TowerColor towerColor;
        try {
            towerColor = board.getTowerColor();
        } catch (NoTowerException e) {
            throw new RuntimeException("Impossible to have towers and no tower color");
        }
        for (int i = 0; i < board.getTowers().size(); i++) {
            printTower(towerColor);
        }
        System.out.print("\n");
    }

    static void printIslands(Model model) {
        System.out.println("\nIslands:");
        int i = 1;
        for (Island island : model.islands) {
            System.out.printf("Island %d: ", i++);
            if (island.hasNoEntryTile()) {
                System.out.print("X ");
            }
            if (island.equals(model.motherNature.getPosition())) {
                System.out.print("M ");
            }
            for (Tower tower : island.getTowers()) {
                printTower(tower.getColor());
            }
            printListStudent(island.getStudents());
            System.out.print("\n");
        }
    }

    static void printClouds(Model model) {
        System.out.println("\nClouds: ");
        int i = 1;
        for (Cloud cloud : model.clouds) {
            System.out.print("Cloud " + i++ + ": ");
            if (cloud.getStudentsWithoutEmptying() == null) {
                System.out.print("\n");
                continue;
            }
            printListStudent(cloud.getStudentsWithoutEmptying());
            System.out.print("\n");
        }
    }

    static void printCharacterCards(Model model) {
        System.out.println("\nCharacter Cards: ");
        for (CharacterCard characterCard : model.currentGameCards) {
            System.out.print(characterCard.getClass().getSimpleName() + ": cost " + characterCard.getCoinCost() + " ");
            if (characterCard instanceof CharacterCardWithStudents characterCardWithStudents) {
                printListStudent(characterCardWithStudents.getStudents());
            }
            System.out.print("\n");
        }
    }

    static void printCurrentCharacterCard(Model model) {
        if(model.publicModel.isCharacterCardPlayed()) {
            System.out.printf("\nCurrently played Character Card: %s\n",
                    model.lastPlayedCharacterCard.getClass().getSimpleName());
        }
    }

    static void printDeck(Player player) {
        System.out.print("Deck: ");
        for(AssistantCard card : player.getDeck().getHand()) {
            if (card != null) {
                System.out.print(card + " ");
            }
        }
        System.out.print("\n");
    }

    static void printAssistantCard(Player player) {
        System.out.print("Current assistant card: ");
        AssistantCard card = player.getCurrentAssistantCard();
        if (card != null) {
            System.out.printf("%s -> turn order value: %d; max mother nature movements: %d", card, card.getTurnOrderValue(), card.getMaxMotherNatureMovementValue());
        }
        System.out.print("\n");
    }

    static void printTeammate(int playerIndex, Model model) {
        Player teammate;

        teammate = switch (playerIndex) {
            case 0 -> model.players.get(1);
            case 1 -> model.players.get(0);
            case 2 -> model.players.get(3);
            case 3 -> model.players.get(2);
            default -> throw new RuntimeException("Impossible state");
        };

        System.out.printf("Teammate: %s\n", teammate.getNickname());
    }

    static void printCoins(Board board) {
        System.out.printf("Coins: %d\n", board.getCoinCount());
    }

    public static void printModel(Model model) {
        for (int i = 0; i < model.players.size(); i++) {
            Player player = model.players.get(i);

            Board board = player.getBoard();
            System.out.println("\n" + player.getNickname() + " Board");

            printEntrance(board);

            printDiningRoom(board, model);

            if (model.totalPlayerCount == 4 && i % 2 == 0)
                printTowers(board);

            printDeck(player);

            printAssistantCard(player);

            if (model.totalPlayerCount == 4) {
                printTeammate(i, model);
            }

            if (model.gameMode.equals(GameMode.EXPERT_MODE)) {
                printCoins(board);
            }
        }

        printIslands(model);

        printClouds(model);

        if (model.gameMode.equals(GameMode.EXPERT_MODE)) {
            printCharacterCards(model);
            printCurrentCharacterCard(model);
        }

        System.out.print("\n");
    }
}
