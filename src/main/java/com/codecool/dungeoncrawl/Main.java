package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.dao.Converter;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.game.Game;
import com.codecool.dungeoncrawl.logic.ui.WindowElement;
import com.codecool.dungeoncrawl.serialization.GameSerialization;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.logging.Logger;


public class Main extends Application {
    private static Stage pStage;
    private static Scene gameScene;
    private static Scene loadGameMenu;
    private static Scene mainMenu;
    private Converter converter = new Converter();
    private GameSerialization serialization = new GameSerialization();



    String[] developers = new String[]{"Fruzsi", "Dani", "Peti", "Balázs"};

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


    public static Scene getGameScene() {
        return gameScene;
    }

    public static Scene getMainMenu() {
        return mainMenu;
    }


    public Scene mainMenu(Stage primaryStage) {
        TextField nameInput = WindowElement.createNameInput();
        Button newGameButton = WindowElement.createNewGameButton();
        Button loadGameButton = WindowElement.createLoadGameButton();
        Button importGameButton = WindowElement.createImportGameButton();

        //TODO: implement functionality for load game button.
        newGameButton.setOnMouseClicked((event)-> this.onNewGamePressed(primaryStage, nameInput));
        loadGameButton.setOnMouseClicked((event)-> this.onLoadPressed(primaryStage));
        importGameButton.setOnMouseClicked((event)-> this.serialization.onImportPressed(pStage));

        VBox mainPane = WindowElement.createMainPane(nameInput, newGameButton, loadGameButton, importGameButton);

        return new Scene(mainPane);
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
