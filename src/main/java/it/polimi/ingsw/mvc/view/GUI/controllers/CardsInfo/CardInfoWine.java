package it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo;

import it.polimi.ingsw.cards.characters.WineCharacter.WineCharacter;
import it.polimi.ingsw.cards.characters.WineCharacter.WineCharacterArgument;
import it.polimi.ingsw.messages.game.PlayCharacterCardMessage;
import it.polimi.ingsw.mvc.view.GUI.GUIView;
import it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo.selectableTokens.IslandToken;
import it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo.selectableTokens.StudentToken;
import it.polimi.ingsw.pawn.Student;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class CardInfoWine extends CardInfoController {
    List<StudentToken> students = new ArrayList<>();
    List<IslandToken> islands = new ArrayList<>();
    Pane cardStudents = new Pane();
    Pane cardIslands = new Pane();

    @Override
    public void playCard(MouseEvent event) {
        StudentToken selectedStudent = null;
        IslandToken selectedIsland = null;
        for (StudentToken s : students) {
            if (s.isSelected())
                selectedStudent = s;
        }
        for (IslandToken i : islands) {
            if (i.isSelected())
                selectedIsland = i;
        }

        if (selectedStudent == null) {
            GUIView.thisGUI.showInfo("Select a student.");
            return;
        }
        if (selectedIsland == null) {
            GUIView.thisGUI.showInfo("Select an Island");
            return;
        }

        WineCharacterArgument wca = new WineCharacterArgument(selectedIsland.getIndex(), selectedStudent.getColor());
        GUIView.thisGUI.sendMessage(new PlayCharacterCardMessage(index, wca));
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
        List<Student> backupCardStudents = ((WineCharacter) thisCc).getStudents();
        for (int i = 0; i < backupCardStudents.size(); i++) {
            st = new StudentToken(backupCardStudents.get(i).getColor());
            cardStudents.getChildren().add(st);
            st.setLayoutY(20);
            st.setLayoutX(37 * i);
            st.setOnMouseClicked(this::onStudentSelected);
            students.add(st);
        }
        cardIslands.setLayoutY(60);
        super.Arguments.getChildren().add(cardIslands);
        cardIslands.getChildren().add(new Label("Choose an Island"));
        IslandToken it;
        for (int i = 0; i < GUIView.thisGUI.getIslandCountFromModel(); i++) {
            it = new IslandToken(i, false);
            cardIslands.getChildren().add(it);

            it.setOnMouseClicked(this::onIslandSelected);
            it.setLayoutY(20 + (i / 6) * 50);
            it.setLayoutX(50 * (i % 6));
            islands.add(it);
        }

    }

    public void onIslandSelected(MouseEvent mouseEvent) {
        for (IslandToken it : islands) {
            if (it.equals(mouseEvent.getSource())) {
                it.setSelected(true);
            } else
                it.setSelected(false);

            it.update();
        }
    }

    public void onStudentSelected(MouseEvent mouseEvent) {
        for (StudentToken it : students) {
            if (it.equals(mouseEvent.getSource())) {
                it.setSelected(true);
            } else
                it.setSelected(false);

            it.update();
        }
    }
}