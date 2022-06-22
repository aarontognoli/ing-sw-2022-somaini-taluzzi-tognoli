package it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo;

import it.polimi.ingsw.cards.characters.JokerCharacter.JokerCharacter;
import it.polimi.ingsw.cards.characters.JokerCharacter.JokerCharacterArgument;
import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.messages.game.PlayCharacterCardMessage;
import it.polimi.ingsw.mvc.view.GUI.GUIView;
import it.polimi.ingsw.mvc.view.GUI.LobbyFrame;
import it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo.selectableTokens.StudentToken;
import it.polimi.ingsw.pawn.Student;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class CardInfoJoker extends CardInfoController {
    List<StudentToken> entrance = new ArrayList<>();
    List<StudentToken> card = new ArrayList<>();
    Pane cardStudents = new Pane();
    Pane entranceStudents = new Pane();

    @Override
    public void playCard(MouseEvent event) {
        List<Color> cardSelected = new ArrayList<>();
        List<Color> entranceSelected = new ArrayList<>();
        for (StudentToken st : card) {
            if (st.isSelected()) {
                cardSelected.add(st.getColor());
            }
        }
        for (StudentToken st : entrance) {
            if (st.isSelected()) {
                entranceSelected.add(st.getColor());
            }
        }

        if (cardSelected.size() == 0) {
            LobbyFrame.lobbyFrame.showInfo("Select at least a student in this card.");
            return;
        }
        if (entranceSelected.size() == 0) {
            LobbyFrame.lobbyFrame.showInfo("Select at least a student in the entrance.");
            return;
        }
        if (cardSelected.size() != entranceSelected.size()) {
            LobbyFrame.lobbyFrame.showInfo("You need to select the same number of students.");
            return;
        }

        JokerCharacterArgument jca = new JokerCharacterArgument(cardSelected, entranceSelected);
        GUIView.thisGUI.sendMessage(new PlayCharacterCardMessage(index, jca));
        close();
    }

    @Override
    public void showArguments() {
        super.Arguments.setLayoutY(super.Arguments.getLayoutY() - 100);
        super.Arguments.toFront();

        StudentToken st;
        super.Arguments.getChildren().add(cardStudents);
        cardStudents.setLayoutY(0);
        cardStudents.getChildren().add(new Label("Card Students"));
        List<Student> backupCardStudents = ((JokerCharacter) thisCc).getStudents();
        for (int i = 0; i < backupCardStudents.size(); i++) {
            st = new StudentToken(backupCardStudents.get(i).getColor());
            cardStudents.getChildren().add(st);
            st.setLayoutY(20);
            st.setLayoutX(37 * i);
            st.setOnMouseClicked(this::onCLickStudentsCard);
            card.add(st);
        }


        super.Arguments.getChildren().add(entranceStudents);
        entranceStudents.setLayoutY(60);
        entranceStudents.getChildren().add(new Label("Entrance Students"));
        List<Student> backupEntrance = GUIView.thisGUI.getThisPlayerEntrance();
        for (int i = 0; i < backupEntrance.size(); i++) {
            st = new StudentToken(backupEntrance.get(i).getColor());
            entranceStudents.getChildren().add(st);
            st.setLayoutY(20);
            st.setLayoutX(37 * i);
            st.setOnMouseClicked(this::onCLickStudentsEntrance);
            entrance.add(st);
        }


    }

    private void onCLickStudentsEntrance(MouseEvent event) {
        StudentToken selected = (StudentToken) event.getSource();
        clickStudents(entrance, selected);

    }

    private void onCLickStudentsCard(MouseEvent event) {
        StudentToken selected = (StudentToken) event.getSource();
        clickStudents(card, selected);

    }

    private void clickStudents(List<StudentToken> list, StudentToken selected) {
        if (selected.isSelected()) {
            selected.setSelected(false);
        } else {
            int selectedNumber = 0;
            for (StudentToken cc : list) {
                if (cc.isSelected())
                    selectedNumber++;
            }
            if (selectedNumber < 3) {
                selected.setSelected(true);
            } else {
                GUIView.thisGUI.showInfo("You can select at most 3 students");
            }

        }
        selected.update();
    }

}