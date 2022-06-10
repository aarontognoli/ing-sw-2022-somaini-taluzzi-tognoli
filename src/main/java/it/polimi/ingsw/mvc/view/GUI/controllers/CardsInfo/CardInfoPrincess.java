package it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo;

import it.polimi.ingsw.cards.characters.PrincessCharacter.PrincessCharacter;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CardInfoPrincess extends CardInfoColor {
    @Override
    public void showContent() {
        PrincessCharacter pc = (PrincessCharacter) thisCc;
        super.Content.getChildren().add(new Label("Students in thi card:"));
        for (int i = 0; i < pc.getStudents().size(); i++) {
            ImageView student = new ImageView(new Image("/imgs/Students/" + pc.getStudents().get(i).getColor() + ".png"));
            super.Content.getChildren().add(student);
            ;
            student.setLayoutX(60 * i);
            student.setLayoutY(20);
        }

    }
}
