package com.example.transformers;

import com.example.transformers.converter.api.JsonToXmlApi;
import com.example.transformers.converter.api.XmlToJsonApi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.File;
import java.lang.classfile.Label;
import java.nio.file.Files;
import java.util.Objects;

public class HelloController {

    @FXML
    private TextArea textArea1;
    @FXML
    private TextArea textArea2;
    @FXML
    private Button open;
    @FXML
    private Button save;
    @FXML
    private Button convert;
    @FXML
    private ComboBox<String> combo;

    XmlToJsonApi xmlToJsonApi ;
    JsonToXmlApi jsonToXmlApi ;

    public HelloController(){

    }



    public void initialize(){

        combo.getItems().add("JSON to XML");
        combo.getItems().add("XML TO JSON");
        combo.getSelectionModel().select(1);
    }

    public void permutation(ActionEvent actionEvent) {
        String selectedItem = combo.getSelectionModel().getSelectedItem();

        if (!textArea1.getText().isEmpty() || !textArea2.getText().isEmpty()) {
            String temp = textArea1.getText();
            textArea1.setText(textArea2.getText());
            textArea2.setText(temp);
        }

    }

    public void onClickOpenFile() {
        FileChooser fileChooser =  new FileChooser();
        
        //xml
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        //json
        FileChooser.ExtensionFilter extFilter2 = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter2);

        File file = fileChooser.showOpenDialog(open.getScene().getWindow());

        if (file != null) {
            try {
                String content = Files.readString(file.toPath());
                textArea1.setText(content);
            } catch (Exception e) {
                textArea1.setText("Erreur lors de la lecture du fichier: " + e.getMessage());
            }
        }
        
    }

    public void onClickConvert() {
        String inputText = textArea1.getText();
        String result = "";

        if (combo.getValue().equals("XML TO JSON")) {
            XmlToJsonApi xmlToJsonApi = new XmlToJsonApi();
            result = xmlToJsonApi.convert(inputText);
        } else if (combo.getValue().equals("JSON to XML")) {
            JsonToXmlApi jsonToXmlApi = new JsonToXmlApi();
            result = jsonToXmlApi.convert(inputText);
        }

        textArea2.setText(result);
    }

    public void onClickSave() {
        FileChooser fileChooser = new FileChooser();

        //xml
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        //json
        FileChooser.ExtensionFilter extFilter2 = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter2);

        File file = fileChooser.showSaveDialog(save.getScene().getWindow());

        if (file != null) {
            try {
                Files.writeString(file.toPath(), textArea2.getText());
            } catch (Exception e) {
                textArea2.setText("Erreur lors de la sauvegarde du fichier: " + e.getMessage());
            }
        }
    }
}
