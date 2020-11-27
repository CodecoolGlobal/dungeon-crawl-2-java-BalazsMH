package com.codecool.dungeoncrawl.serialization;

import com.codecool.dungeoncrawl.dao.Converter;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.game.Game;
import com.google.gson.Gson;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class GameSerialization {

    public GameSerialization() {}

    public void onImportPressed(Stage stage) {
        String fileExtension = null;
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {

            String fileName = selectedFile.getName();
            fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, selectedFile.getName().length());
        }

        if (!fileExtension.equals("json")) {

            Stage popupWindow=new Stage();
            popupWindow.initModality(Modality.APPLICATION_MODAL);
            popupWindow.setTitle("IMPORT ERROR! Unfortunately the given file is in wrong format. Please try another one!");
            Label label1= new Label("IMPORT ERROR!");

            Button cancelButton = new Button("Cancel");
            Button okButton = new Button("Ok");

            cancelButton.setOnAction(e -> popupWindow.close());
            okButton.setOnAction(e -> {
                popupWindow.close();
                onImportPressed(stage);
            });

            VBox layout= new VBox(10);

            layout.getChildren().addAll(label1, cancelButton, okButton);

            layout.setAlignment(Pos.CENTER);

            Scene scene1= new Scene(layout, 300, 250);

            popupWindow.setScene(scene1);
            popupWindow.show();
        }

        else {
            System.out.println("I got JSON file");
        }

    }

    public void onExportPressed(Converter converter, Stage stage, String gameState) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            saveTextToFile(gameState, file);
        }
    }

    public void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException ex) {
            System.out.println("Null");
        }
    }
}
