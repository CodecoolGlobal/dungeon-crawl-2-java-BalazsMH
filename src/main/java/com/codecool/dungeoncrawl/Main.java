package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.dao.Converter;
import com.codecool.dungeoncrawl.logic.game.Game;
import com.codecool.dungeoncrawl.logic.ui.WindowElement;
import com.codecool.dungeoncrawl.model.GameState;
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
    private final Converter converter = new Converter();
    private final String[] developers = new String[]{"Fruzsi", "Dani", "Peti", "Balázs"};

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        pStage = primaryStage;
        primaryStage.setTitle("JavaMon");
        primaryStage.getIcons().add(new Image("file:logo.png"));

        mainMenu = mainMenu();

        primaryStage.setScene(mainMenu);
        primaryStage.show();
    }


    public Scene mainMenu() {
//        TextField nameInput = WindowElement.createNameInput();
        Button newGameButton = WindowElement.createNewGameButton();
        Button loadGameButton = WindowElement.createLoadGameButton();
        Button importGameButton = WindowElement.createImportGameButton();

        newGameButton.setOnMouseClicked((event)-> onNewGamePressed());
        loadGameButton.setOnMouseClicked((event)-> onLoadPressed());
        importGameButton.setOnMouseClicked((event) -> onImportPressed());

        VBox mainPane = WindowElement.createMainPane(newGameButton, loadGameButton, importGameButton);

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
                Game game = new Game(gameState, converter, pStage);
                pStage.setScene(game.showGameScene());
            } catch (IOException e){
                System.out.println(e.getMessage());
            }
        } else WindowElement.importErrorWindow(pStage);
    }

    private void onNewGamePressed() {
        String nameInput = WindowElement.createStartScreen(pStage);
        Game game = new Game(converter, pStage);
        game.getPlayer().setUserName(nameInput);
        if (Arrays.asList(developers).contains(nameInput)) game.getPlayer().setSuperUser(true);
        pStage.setScene(game.showGameScene());
    }


    private void onLoadPressed() {
        Scene loadGameScene = WindowElement.createLoadGameMenu(pStage, mainMenu, converter);
        pStage.setScene(loadGameScene);
    }

}
