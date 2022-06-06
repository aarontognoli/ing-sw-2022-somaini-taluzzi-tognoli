package it.polimi.ingsw.mvc.view.GUI.controllers;

import it.polimi.ingsw.pawn.Student;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CloudController extends Pane implements Initializable {
    public Pane threePlayers;
    public ImageView pic;
    public Pane defaultPlayers;

    private Pane studentsParent;
    private int index;
    private int size;

    public CloudController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/CloudTile.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        studentsParent = defaultPlayers;
    }

    public void setup(int playersNumber, int cloudIndex) {
        if (playersNumber == 3) {
            pic.setImage(new Image("/imgs/Misc/cloud3.png"));
            threePlayers.setVisible(true);
            defaultPlayers.setVisible(false);
            studentsParent = threePlayers;
        } else {
            pic.setImage(new Image("/imgs/Misc/cloud2_4.png"));
            threePlayers.setVisible(false);
            defaultPlayers.setVisible(true);
        }
        index = cloudIndex;
    }

    public void updateStudents(List<Student> cloudStudents) {
        String thisColor;
        int i;
        size = 0;
        if (cloudStudents != null) {
            size = cloudStudents.size();
        }
        for (i = 0; i < size; i++) {
            thisColor = cloudStudents.get(i).getColor().toString();
            ((ImageView) studentsParent.getChildren().get(i)).setImage(new Image("/imgs/Students/" + thisColor + ".png"));
        }
        for (; i < studentsParent.getChildren().size(); i++) {
            studentsParent.getChildren().get(i).setVisible(false);
        }
    }

    public int getIndex() {
        return index;
    }

    public int getSize() {
        return size;
    }
}
