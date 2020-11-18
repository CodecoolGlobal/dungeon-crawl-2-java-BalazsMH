package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.map.MapGenerator;

import com.codecool.dungeoncrawl.logic.map.GameMap;
import com.codecool.dungeoncrawl.logic.map.MapChanger;
import com.codecool.dungeoncrawl.logic.map.MapLoader;
import com.codecool.dungeoncrawl.logic.ui.WindowElement;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;


public class Main extends Application {
    private static Stage pStage;
    private static Scene gameScene;
    private static Scene loadGameMenu;
    private static Scene mainMenu;



    String[] developers = new String[]{"Fruzsi", "Dani", "Peti", "Balázs"};

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        pStage = primaryStage;
        primaryStage.setTitle("JavaMon");
        primaryStage.getIcons().add(new Image("file:logo.png"));

        this.mainMenu = mainMenu(primaryStage);

        primaryStage.setScene(mainMenu);
        //refresh(map.getPlayer().getInventory()); why does it need to refresh right away?
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
        //TODO: implement functionality for load game button.
        newGameButton.setOnMouseClicked((event)-> this.onNewGamePressed(primaryStage, nameInput));
        loadGameButton.setOnMouseClicked((event)-> this.onLoadPressed(primaryStage));
        VBox mainPane = WindowElement.createMainPane(nameInput, newGameButton, loadGameButton);
        Scene mainMenu = new Scene(mainPane);

        return mainMenu;
    }


    private void onNewGamePressed(Stage primaryStage,TextField nameInput) {
        Game game = new Game();
        String enteredName = nameInput.getText();
        game.getMap1().getPlayer().setUserName(enteredName);

        if (Arrays.asList(developers).contains(enteredName)) {
            game.getMap1().getPlayer().setSuperUser(true);
        }

        primaryStage.setScene(game.showGameScene());
    }

    private void onLoadPressed(Stage primaryStage) {
        Scene loadGameScene = WindowElement.createLoadGameMenu(primaryStage);
        primaryStage.setScene(loadGameScene);
    }

    public static Stage getpStage() {
        return pStage;
    }
}
