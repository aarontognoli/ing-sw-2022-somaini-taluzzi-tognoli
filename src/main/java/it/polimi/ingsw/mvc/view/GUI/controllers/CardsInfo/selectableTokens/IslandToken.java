package it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo.selectableTokens;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class IslandToken extends StackPane {
    final private static int PIC_HEIGHT = 40;
    int index;
    boolean selected;
    Circle circle;

    public IslandToken(int index, boolean selected) {
        this.selected = selected;
        this.index = index;
        //this.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));
        Text label = new Text(String.valueOf(index + 1));
        ImageView p = new ImageView();

        Image image;
        switch (index % 3) {
            case 0 -> {
                image = new Image("/imgs/Misc/island_1.png");
            }
            case 1 -> {
                image = new Image("/imgs/Misc/island_2.png");
            }
            default -> {
                image = new Image("/imgs/Misc/island_3.png");

            }
        }

        p.setImage(image);
        p.setPreserveRatio(true);
        p.setFitHeight(PIC_HEIGHT);

        getChildren().add(p);

        label.setStroke(Color.BLACK);
        label.setStrokeWidth(1);
        label.setFill(Color.WHITE);
        label.setFont(new Font(23));
        getChildren().add(label);
        circle = new Circle();
        circle.setFill(null);
        circle.setStroke(Color.GREEN);
        circle.setStrokeWidth(5);
        circle.setVisible(false);
        circle.setRadius(PIC_HEIGHT / 2);
        this.getChildren().add(circle);
        this.setAlignment(label, Pos.CENTER);
        this.setAlignment(circle, Pos.CENTER);


    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getIndex() {
        return index;
    }

    public void update() {
        if (selected) {
            circle.setVisible(true);
        } else
            circle.setVisible(false);
    }
}
