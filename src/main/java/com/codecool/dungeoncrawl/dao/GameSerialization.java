package com.codecool.dungeoncrawl.dao;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class GameSerialization {

    public GameSerialization() {}

    public void onExportPressed(Stage stage, String gameState) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("*.json", "*.json"));
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
