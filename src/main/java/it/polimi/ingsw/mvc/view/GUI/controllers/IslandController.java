package it.polimi.ingsw.mvc.view.GUI.controllers;

import it.polimi.ingsw.enums.Color;
import it.polimi.ingsw.enums.TowerColor;
import it.polimi.ingsw.pawn.Student;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class IslandController extends Pane implements Initializable {
    @FXML
    private ImageView pic;
    @FXML
    private ImageView motherNature;
    @FXML
    private ImageView RED_DRAGONS;
    @FXML
    private ImageView GREEN_FROGS;
    @FXML
    private ImageView PINK_FAIRIES;
    @FXML
    private ImageView YELLOW_GNOMES;
    @FXML
    private ImageView BLUE_UNICORNS;
    @FXML
    private ImageView tower;
    @FXML
    private ImageView noEntry;
    @FXML
    private AnchorPane info;
    @FXML
    private Text GREEN_COUNT;
    @FXML
    private Text RED_COUNT;
    @FXML
    private Text YELLOW_COUNT;
    @FXML
    private Text PINK_COUNT;
    @FXML
    private Text BLUE_COUNT;
    @FXML
    private Text towerCount;

    private Map<Color, Text> colorCountMap;
    private Map<Color, ImageView> colorImageViewMap;
    private int index;

    public IslandController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/island.fxml"));
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

        info.setVisible(false);
        info.setDisable(true);
        colorCountMap = new HashMap<>();
        colorCountMap.put(Color.RED_DRAGONS, RED_COUNT);
        colorCountMap.put(Color.BLUE_UNICORNS, BLUE_COUNT);
        colorCountMap.put(Color.PINK_FAIRIES, PINK_COUNT);
        colorCountMap.put(Color.GREEN_FROGS, GREEN_COUNT);
        colorCountMap.put(Color.YELLOW_GNOMES, YELLOW_COUNT);

        colorImageViewMap = new HashMap<>();
        colorImageViewMap.put(Color.RED_DRAGONS, RED_DRAGONS);
        colorImageViewMap.put(Color.BLUE_UNICORNS, BLUE_UNICORNS);
        colorImageViewMap.put(Color.PINK_FAIRIES, PINK_FAIRIES);
        colorImageViewMap.put(Color.GREEN_FROGS, GREEN_FROGS);
        colorImageViewMap.put(Color.YELLOW_GNOMES, YELLOW_GNOMES);
    }

    public void setPicAndIndex(int index) {
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
        pic.setImage(image);
        this.index = index;
    }

    public void setStudents(List<Student> islandStudents) {
        Text thisText;
        resetStudents();
        for (Student s : islandStudents) {
            colorImageViewMap.get(s.getColor()).setVisible(true);
            thisText = colorCountMap.get(s.getColor());
            thisText.setText(String.valueOf(Integer.parseInt(thisText.getText()) + 1));
        }
    }

    private void resetStudents() {
        for (Color c : Color.values()) {
            colorCountMap.get(c).setText("0");
            colorImageViewMap.get(c).setVisible(false);
        }
    }

    public void setNoEntryTile(boolean doesItHave) {
        noEntry.setVisible(doesItHave);
    }

    public void setMotherNature(boolean doesItHave) {
        motherNature.setVisible(doesItHave);
    }

    public void setTower(TowerColor c, int count) {
        if (c != null) {
            tower.setVisible(true);
            tower.setImage(new Image("/imgs/Towers/" + c.toString() + ".png"));
            towerCount.setVisible(true);
            towerCount.setText(String.valueOf(count));
        } else {
            tower.setVisible(false);
            towerCount.setVisible(false);
        }
    }

    @FXML
    private void showInfo(MouseEvent e) {
        info.setVisible(true);
        info.setDisable(false);

    }

    @FXML
    private void hideInfo(MouseEvent e) {
        info.setVisible(false);
        info.setDisable(true);
    }

    public int getIndex() {
        return index;
    }

}
