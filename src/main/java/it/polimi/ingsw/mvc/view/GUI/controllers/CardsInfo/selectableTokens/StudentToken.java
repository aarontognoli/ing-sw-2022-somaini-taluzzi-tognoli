package it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo.selectableTokens;

import it.polimi.ingsw.enums.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class StudentToken extends Token {

    Color c;

    public StudentToken(Color c) {
        pic = new ImageView(new Image("/imgs/Students/" + c.toString() + ".png"));
        selected = false;
        this.c = c;
        pic.setFitHeight(30);
        this.getChildren().add(pic);
        circle = new Circle();
        this.getChildren().add(circle);
        circle.setFill(null);
        circle.setStroke(javafx.scene.paint.Color.GREEN);
        circle.setStrokeWidth(5);
        circle.setCenterX(pic.getFitHeight() / 2 - 10);
        circle.setCenterY(pic.getFitHeight() / 2);
        circle.setRadius(pic.getFitHeight() / 2);
        circle.setVisible(false);
        pic.setPreserveRatio(true);

    }

    public Color getColor() {
        return c;
    }
}
