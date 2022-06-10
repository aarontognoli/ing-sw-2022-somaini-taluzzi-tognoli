package it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo;

import it.polimi.ingsw.cards.characters.BardCharacter.BardCharacterArgument;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.messages.game.PlayCharacterCardMessage;
import it.polimi.ingsw.mvc.view.GUI.GUIView;
import it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo.selectableTokens.CardColorsToken;
import it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo.selectableTokens.StudentToken;
import it.polimi.ingsw.pawn.Student;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class CardInfoBard extends CardInfoController {
    List<StudentToken> entrance = new ArrayList<>();
    List<CardColorsToken> first = new ArrayList<>();
    List<CardColorsToken> second = new ArrayList<>();
    Pane firstColor = new Pane();
    Pane secondColor = new Pane();

    @Override
    public void playCard(MouseEvent event) {
        int count = 0;
        List<Color> selectedStudents = new ArrayList<>();
        List<Color> selectedDiningRooms = new ArrayList<>();
        for (StudentToken st : entrance) {
            if (st.isSelected()) {
                count++;
                selectedStudents.add(st.getColor());
            }
        }
        if (count == 0) {
            GUIView.thisGUI.showInfo("Select at least a student.");
            return;
        } else if (count == 1) {
            for (CardColorsToken cct : first) {
                if (cct.isSelected())
                    selectedDiningRooms.add(cct.getColor());
            }
            if (selectedDiningRooms.size() != 1) {
                GUIView.thisGUI.showInfo("Select first dining room color.");
                return;
            }
        } else {
            for (CardColorsToken cct : first) {
                if (cct.isSelected())
                    selectedDiningRooms.add(cct.getColor());
            }
            for (CardColorsToken cct : second) {
                if (cct.isSelected())
                    selectedDiningRooms.add(cct.getColor());
            }
            if (selectedDiningRooms.size() != 2) {
                GUIView.thisGUI.showInfo("You need to select more dining room colors");
                return;
            }
        }

        BardCharacterArgument bca = new BardCharacterArgument(selectedStudents, selectedDiningRooms);
        GUIView.thisGUI.sendMessage(new PlayCharacterCardMessage(index, bca));
    }

    @Override
    public void showArguments() {
        super.Arguments.setLayoutY(super.Arguments.getLayoutY() - 100);
        super.Arguments.toFront();
        super.Arguments.getChildren().add(new Label("Entrance Students"));
        StudentToken st;
        List<Student> backupEntrance = GUIView.thisGUI.getThisPlayerEntrance();
        for (int i = 0; i < backupEntrance.size(); i++) {
            st = new StudentToken(backupEntrance.get(i).getColor());
            super.Arguments.getChildren().add(st);
            st.setLayoutY(20);
            st.setLayoutX(37 * i);
            st.setOnMouseClicked(this::onCLickStudents);
            entrance.add(st);
        }
        super.Arguments.getChildren().add(firstColor);

        firstColor.setLayoutY(60);
        firstColor.getChildren().add(new Label("First diningRoom"));
        CardColorsToken element;
        int i = 0;
        for (Color c : Color.values()) {
            element = new CardColorsToken(c);
            firstColor.getChildren().add(element);
            first.add(element);
            element.setOnMouseClicked(this::onColorSelectedFirst);
            element.setLayoutY(20);
            element.setLayoutX(60 * i);
            i++;
        }
        super.Arguments.getChildren().add(secondColor);

        secondColor.setLayoutY(140);
        secondColor.getChildren().add(new Label("Second diningRoom"));

        i = 0;
        for (Color c : Color.values()) {
            element = new CardColorsToken(c);
            secondColor.getChildren().add(element);
            second.add(element);
            element.setOnMouseClicked(this::onColorSelectedSecond);
            element.setLayoutY(20);
            element.setLayoutX(60 * i);
            i++;
        }
        hideAll();

    }

    private void onCLickStudents(MouseEvent event) {
        StudentToken selected = (StudentToken) event.getSource();

        if (selected.isSelected()) {
            selected.setSelected(false);
        } else {
            int selectedNumber = 0;
            for (StudentToken cc : entrance) {
                if (cc.isSelected())
                    selectedNumber++;
            }
            if (selectedNumber < 2) {
                selected.setSelected(true);
            } else {
                GUIView.thisGUI.showInfo("You can select at most 2 students");
            }

        }
        int selectedNumber = 0;
        for (StudentToken cc : entrance) {
            if (cc.isSelected())
                selectedNumber++;
        }
        if (selectedNumber == 1) {
            showFirstSelectColor();
        } else if (selectedNumber == 2) {
            showSecondSelectColor();
        } else {
            hideAll();
        }
        selected.update();
    }

    private void showFirstSelectColor() {
        firstColor.setVisible(true);
        firstColor.setDisable(false);
        secondColor.setVisible(false);
        secondColor.setDisable(true);
    }

    private void showSecondSelectColor() {
        secondColor.setVisible(true);
        secondColor.setDisable(false);
    }

    private void hideAll() {
        firstColor.setVisible(false);
        firstColor.setDisable(true);
        secondColor.setVisible(false);
        secondColor.setDisable(true);
    }

    public void onColorSelectedFirst(MouseEvent mouseEvent) {
        for (CardColorsToken cc : first) {
            if (cc.equals(mouseEvent.getSource())) {
                cc.setSelected(true);
            } else
                cc.setSelected(false);

            cc.update();
        }
    }

    public void onColorSelectedSecond(MouseEvent mouseEvent) {
        for (CardColorsToken cc : second) {
            if (cc.equals(mouseEvent.getSource())) {
                cc.setSelected(true);
            } else
                cc.setSelected(false);

            cc.update();
        }
    }
}