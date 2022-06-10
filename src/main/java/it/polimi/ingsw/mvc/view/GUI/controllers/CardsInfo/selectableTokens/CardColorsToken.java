package it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo.selectableTokens;

import it.polimi.ingsw.enums.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class CardColorsToken extends Pane {
    ImageView color;
    boolean selected;
    Color c;
    Circle circle;

    public CardColorsToken(Color c) {

        //this.setBackground(Background.fill(javafx.scene.paint.Color.RED));
        color = new ImageView(new Image("/imgs/Professors/" + c.toString() + ".png"));
        selected = false;
        this.c = c;
        this.getChildren().add(color);
        circle = new Circle();
        this.getChildren().add(circle);
        circle.setFill(null);
        circle.setStroke(javafx.scene.paint.Color.GREEN);
        circle.setStrokeWidth(5);
        circle.setCenterX(27.5);
        circle.setCenterY(24);
        circle.setRadius(24);
        circle.setVisible(false);
    }


    public boolean getSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Color getColor() {
        return c;
    }

    public void update() {
        if (selected) {
            circle.setVisible(true);
        } else
            circle.setVisible(false);
    }
}
