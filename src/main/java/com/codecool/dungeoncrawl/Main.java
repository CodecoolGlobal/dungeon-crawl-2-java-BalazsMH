package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.dao.Converter;
import com.codecool.dungeoncrawl.logic.game.Game;
import com.codecool.dungeoncrawl.logic.ui.WindowElement;
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
import java.util.Arrays;


public class Main extends Application {
    private static Stage pStage;
    private static Scene gameScene;
    private static Scene loadGameMenu;
    private static Scene mainMenu;
    private Converter converter = new Converter();



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
        Button exportGameButton = WindowElement.createExportGameButton();
        Button importGameButton = WindowElement.createImportGameButton();

        //TODO: implement functionality for load game button.
        newGameButton.setOnMouseClicked((event)-> this.onNewGamePressed(primaryStage, nameInput));
        loadGameButton.setOnMouseClicked((event)-> this.onLoadPressed(primaryStage));
        importGameButton.setOnMouseClicked((event)-> this.onImportPressed());
        VBox mainPane = WindowElement.createMainPane(nameInput, newGameButton, loadGameButton, exportGameButton, importGameButton);

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
    
    private void onImportPressed() {
        String fileExtension = null;
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(pStage);
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
            okButton.setOnAction(e -> onImportPressed());

            VBox layout= new VBox(10);

            layout.getChildren().addAll(label1, cancelButton, okButton);

            layout.setAlignment(Pos.CENTER);

            Scene scene1= new Scene(layout, 300, 250);

            popupWindow.setScene(scene1);
            popupWindow.show();
        }

    }
}
