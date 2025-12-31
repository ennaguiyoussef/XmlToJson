package com.example.transformers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    private ComboBox<String> combo;

    public void initialize(){

        combo.getItems().add("JSON to XML");
        combo.getItems().add("XML TO JSON");
        combo.getSelectionModel().select(2);
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");


    }
}
