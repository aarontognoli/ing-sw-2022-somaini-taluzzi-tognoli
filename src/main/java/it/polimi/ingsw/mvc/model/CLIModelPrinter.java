package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.cards.characters.CharacterCard;
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

import java.util.List;

public class CLIModelPrinter {

    // Declaring ANSI_RESET so that we can reset the color
    static final String ANSI_RESET = "\u001B[0m";

    static final String ANSI_GREEN = "\u001B[32m";
    static final String ANSI_RED = "\u001B[31m";
    static final String ANSI_YELLOW = "\u001B[33m";
    static final String ANSI_PINK = "\u001B[35m";
    static final String ANSI_BLUE = "\u001B[34m";

    private static String getAnsiColor(Color color) {
        String ANSI_COLOR = ANSI_RESET;
        switch(color) {
            case GREEN_FROGS -> ANSI_COLOR = ANSI_GREEN;
            case RED_DRAGONS -> ANSI_COLOR = ANSI_RED;
            case YELLOW_GNOMES -> ANSI_COLOR = ANSI_YELLOW;
            case PINK_FAIRIES -> ANSI_COLOR = ANSI_PINK;
            case BLUE_UNICORNS -> ANSI_COLOR = ANSI_BLUE;
        }
        return ANSI_COLOR;
    }

    private static void printStudent(Color color) {
        System.out.print(getAnsiColor(color) + "*" + ANSI_RESET + " ");
    }

    private static void printTower(TowerColor color) {
        String towerColor = switch (color) {
            case BLACK -> "B";
            case GREY -> "G";
            case WHITE -> "W";
        };
        System.out.print(towerColor + " ");
    }

    private static void printProfessor(Color color) {
        System.out.print(getAnsiColor(color) + "P" + ANSI_RESET + " ");
    }

    public static void printModel(Model model) {
        for (Player player : model.players) {

            Board board = player.getBoard();
            System.out.println("\n" + player.getNickname() + " Board");

            System.out.print("Entrance: ");
            for (Student student : board.getEntrance()) {
                printStudent(student.getColor());
            }
            System.out.print("\n");


            System.out.println("Dining room:");
            for (List<Student> students : board.getDiningRoom()) {
                if (students.isEmpty()) {
                    break;
                }
                Color color = students.get(0).getColor();
                for (Student ignored : students) {
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

            //TODO if 4 players print the player which has the towers
            System.out.print("Towers: ");
            for (Tower ignored : board.getTowers()) {
                try {
                    printTower(board.getTowerColor());
                } catch (NoTowerException e) {
                    throw new RuntimeException("Impossible to have towers and no tower color");
                }
            }
            System.out.print("\n");

            if(model.gameMode.equals(GameMode.EXPERT_MODE)) {
                System.out.println("Number of coins: "+ board.getCoinCount());
            }
        }

        System.out.println("\nIslands:");
        int i = 1;
        for (Island island : model.islands) {
            System.out.print("Island " + i++ + ": ");
            if (island.equals(model.motherNature.getPosition())) {
                System.out.print("X ");
            }
            for (Tower tower : island.getTowers()) {
                printTower(tower.getColor());
            }
            for (Student student : island.getStudents()) {
                printStudent(student.getColor());
            }
            System.out.print("\n");
        }

        System.out.println("\nClouds: ");
        i = 1;
        for (Cloud cloud : model.clouds) {
            System.out.print("Cloud " + i++ + ": ");
            for (Student student : cloud.getStudents()) {
                printStudent(student.getColor());
            }
            System.out.print("\n");
        }

        if(model.gameMode.equals(GameMode.EXPERT_MODE)) {
            System.out.println("\nCharacter Cards: ");
            for (CharacterCard characterCard : model.currentGameCards) {
                //TODO print a more pretty character card name...
                System.out.println(characterCard.getClass() + ": " + characterCard.getCoinCost());
            }
        }
    }
}
