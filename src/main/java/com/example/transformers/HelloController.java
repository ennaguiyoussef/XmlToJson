package com.example.transformers;

import com.example.transformers.converter.api.JsonToXmlApi;
import com.example.transformers.converter.api.XmlToJsonApi;
import com.example.transformers.converter.natif.JsonToXmlNatif;
import com.example.transformers.converter.natif.XmlToJsonNatif;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;

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
    @FXML
    private Label errorLabel;
    @FXML
    private RadioMenuItem useApiRadio;
    @FXML
    private RadioMenuItem useNativeRadio;
    @FXML
    private ToggleGroup conversionModeGroup;

    private boolean useApi = true;

    public HelloController() {
    }

    @FXML
    public void initialize() {
        // Initialiser le ComboBox
        combo.getItems().add("JSON to XML");
        combo.getItems().add("XML TO JSON");
        combo.getSelectionModel().select(1);

        // Écouter les changements de mode (API vs Natif)
        conversionModeGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == useApiRadio) {
                useApi = true;
                errorLabel.setText("Mode: API activé");
            } else if (newValue == useNativeRadio) {
                useApi = false;
                errorLabel.setText("Mode: Code natif activé");
            }
        });
    }

    @FXML
    public void permutation(ActionEvent actionEvent) {
        String selectedItem = combo.getSelectionModel().getSelectedItem();

        if (!textArea1.getText().isEmpty() || !textArea2.getText().isEmpty()) {
            String temp = textArea1.getText();
            textArea1.setText(textArea2.getText());
            textArea2.setText(temp);
        }
    }

    @FXML
    public void onClickOpenFile() {
        FileChooser fileChooser = new FileChooser();

        // Extensions XML
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Extensions JSON
        FileChooser.ExtensionFilter extFilter2 = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter2);

        File file = fileChooser.showOpenDialog(open.getScene().getWindow());

        if (file != null) {
            try {
                String content = Files.readString(file.toPath());
                textArea1.setText(content);
                errorLabel.setText("Fichier chargé: " + file.getName());
            } catch (Exception e) {
                errorLabel.setText("Erreur lors de la lecture du fichier: " + e.getMessage());
            }
        }
    }

    @FXML
    public void onClickConvert() {
        String inputText = textArea1.getText();
        String result = "";

        try {
            if (combo.getValue().equals("XML TO JSON")) {
                if (useApi) {
                    // Utiliser l'API
                    XmlToJsonApi xmlToJsonApi = new XmlToJsonApi();
                    result = xmlToJsonApi.convert(inputText);
                } else {
                    // Utiliser le code natif
                    XmlToJsonNatif xmlToJsonNatif = new XmlToJsonNatif();
                    result = xmlToJsonNatif.convert(inputText);
                }
            } else if (combo.getValue().equals("JSON to XML")) {
                if (useApi) {
                    // Utiliser l'API
                    JsonToXmlApi jsonToXmlApi = new JsonToXmlApi();
                    result = jsonToXmlApi.convert(inputText);
                } else {
                    // Utiliser le code natif
                    JsonToXmlNatif jsonToXmlNatif = new JsonToXmlNatif();
                    result = jsonToXmlNatif.convert(inputText);
                }
            }

            textArea2.setText(result);
            errorLabel.setText("Conversion réussie (" + (useApi ? "API" : "Natif") + ")");
            errorLabel.setStyle("-fx-text-fill: green;");

        } catch (Exception e) {
            errorLabel.setText("Erreur lors de la conversion: " + e.getMessage());
            errorLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    public void onClickSave() {
        FileChooser fileChooser = new FileChooser();

        // Extensions XML
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Extensions JSON
        FileChooser.ExtensionFilter extFilter2 = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter2);

        File file = fileChooser.showSaveDialog(save.getScene().getWindow());

        if (file != null) {
            try {
                Files.writeString(file.toPath(), textArea2.getText());
                errorLabel.setText("Fichier sauvegardé: " + file.getName());
                errorLabel.setStyle("-fx-text-fill: green;");
            } catch (Exception e) {
                errorLabel.setText("Erreur lors de la sauvegarde du fichier: " + e.getMessage());
                errorLabel.setStyle("-fx-text-fill: red;");
            }
        }
    }
}
