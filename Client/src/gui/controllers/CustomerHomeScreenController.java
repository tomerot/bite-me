package gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class CustomerHomeScreenController {

    @FXML
    private Label st;

    @FXML
    private Label info;

    @FXML
    void test1(MouseEvent event) {
    	info.setText("NO TOUCH");
    }
}

