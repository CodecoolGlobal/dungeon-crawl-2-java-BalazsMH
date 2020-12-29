package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.dao.Converter;
import com.codecool.dungeoncrawl.logic.game.Game;
import com.codecool.dungeoncrawl.logic.ui.WindowElement;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.serialization.GameSerialization;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;


public class Main extends Application {
    private static Stage pStage;
    private static Scene mainMenu;
    private Converter converter = new Converter();
    private String[] developers = new String[]{"Fruzsi", "Dani", "Peti", "Balázs"};

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        pStage = primaryStage;
        primaryStage.setTitle("JavaMon");
        primaryStage.getIcons().add(new Image("file:logo.png"));

        mainMenu = mainMenu(primaryStage);

        primaryStage.setScene(mainMenu);
        primaryStage.show();
    }


    public Scene mainMenu(Stage primaryStage) {
        TextField nameInput = WindowElement.createNameInput();
        Button newGameButton = WindowElement.createNewGameButton();
        Button loadGameButton = WindowElement.createLoadGameButton();
        Button importGameButton = WindowElement.createImportGameButton();

        newGameButton.setOnMouseClicked((event)-> this.onNewGamePressed(primaryStage, nameInput));
        loadGameButton.setOnMouseClicked((event)-> this.onLoadPressed(primaryStage));
        importGameButton.setOnMouseClicked((event) -> onImportPressed());

        VBox mainPane = WindowElement.createMainPane(nameInput, newGameButton, loadGameButton, importGameButton);

        return new Scene(mainPane);
    }

    private void onImportPressed(){
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(pStage);
        if (selectedFile == null) return;
        String fileName = selectedFile.getName();
        if (fileName.endsWith(".json")){
            try {
                Gson gson = new Gson();
                String s = new String(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath())));
                GameState gameState = gson.fromJson(s, GameState.class);
                Game game = new Game(gameState, converter);
                pStage.setScene(game.showGameScene());
            } catch (IOException e){
                System.out.println(e.getMessage());
            }
        } else WindowElement.importErrorWindow(pStage);
    }

    private void onNewGamePressed(Stage primaryStage,TextField nameInput) {
        Game game = new Game(converter);
        String enteredName = nameInput.getText();
        game.getMap1().getPlayer().setUserName(enteredName);

        if (Arrays.asList(developers).contains(enteredName)) {
            game.getMap1().getPlayer().setSuperUser(true);
        }

        primaryStage.setScene(game.showGameScene());
    }


    private void onLoadPressed(Stage primaryStage) {
        Scene loadGameScene = WindowElement.createLoadGameMenu(primaryStage, mainMenu, converter);
        primaryStage.setScene(loadGameScene);
    }

    public static Stage getpStage() {
        return pStage;
    }

}
