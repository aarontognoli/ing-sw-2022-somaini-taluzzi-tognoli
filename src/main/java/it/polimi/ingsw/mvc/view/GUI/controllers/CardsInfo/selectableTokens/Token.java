package it.polimi.ingsw.mvc.view.GUI.controllers.CardsInfo.selectableTokens;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class Token extends StackPane {
    protected ImageView pic;
    protected boolean selected;

    protected Circle circle;

    public Token() {
        this.setOnMouseEntered(Token::shineBack);
        this.setOnMouseExited(Token::notShineBack);
    }

    static void shineBack(MouseEvent mouseEvent) {
        ((Node) mouseEvent.getSource()).setStyle("-fx-effect: dropshadow(three-pass-box, #1E6C93, 20, 0, 0, 0);");
        ((Node) mouseEvent.getSource()).toBack();

    }

    static void notShineBack(MouseEvent mouseEvent) {
        ((Node) mouseEvent.getSource()).setStyle("");

    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    public void update() {
        if (selected) {
            circle.setVisible(true);
        } else
            circle.setVisible(false);
    }
}
