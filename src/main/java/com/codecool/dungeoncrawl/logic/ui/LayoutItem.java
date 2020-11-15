package com.codecool.dungeoncrawl.logic.ui;

import com.codecool.dungeoncrawl.logic.EndCondition;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.items.Inventory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LayoutItem {

    public static VBox createMainPane(TextField nameInput, Button submitButton) {
        VBox mainPane = new VBox(nameInput, submitButton);
        mainPane.setPrefSize(1287/1.5,797/1.5);
        Background background = new Background(new BackgroundImage(new Image("/main_menu.png"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO,
                BackgroundSize.AUTO,
                false, false, true, true)));
        mainPane.setBackground(background);
        mainPane.setAlignment(Pos.CENTER);
        mainPane.requestFocus();
        return mainPane;
    }

    public static Button createSubmitButton() {
        Button submitButton = new Button("Play!");
        submitButton.setFont(Font.loadFont("file:Pokemon_Classic.ttf", 14));
        return submitButton;
    }

    public static TextField createNameInput() {
        TextField nameInput = new TextField();
        nameInput.setFont(Font.loadFont("file:Pokemon_Classic.ttf", 14));
        nameInput.setMaxSize(220,220);
        nameInput.setPromptText("Enter your name ");
        return nameInput;
    }

    public static VBox createInfoBox(Label currentInfo){

        currentInfo.setWrapText(true);
        currentInfo.setPrefWidth(300);

        VBox infoBox = new VBox(currentInfo);
        infoBox.setStyle("-fx-padding: 10px;");
        infoBox.setPrefHeight(600);
        infoBox.setPrefWidth(300);

        infoBox.setSpacing(10);
        return infoBox;
    }

    public static VBox createBottomBox() {
        Text movementInfo = new Text("Hint:\nUse the arrow keys to move the character on the map\n" +
                "Press 'A' to change active pokemon and 'H' to heal it\n" +
                "Press 'F' to fight and 'T' to catch pokemon\n" +
                "Pick things up by 'E'\n" +
                "Engage Rocket Grunt by 'R'\n");
        movementInfo.setFont(Font.loadFont("file:Pokemon_Classic.ttf", 12));
        movementInfo.setTextAlignment(TextAlignment.CENTER);
        movementInfo.setLineSpacing(1.5);

        VBox bottom = new VBox(movementInfo);
        bottom.setAlignment(Pos.CENTER);
        return bottom;
    }

    public static VBox createLevelBox(Label currentLevel) {
        VBox levelBox = new VBox(currentLevel);
        levelBox.setAlignment(Pos.CENTER);
        levelBox.setPadding(new Insets(5));
        levelBox.setMaxHeight(10);
        return levelBox;
    }

    public static void setLabels(Label currentLevel, Label nameLabel, Label currentInfo, Label inv, GameMap map) {
        currentLevel.setText(map.getLevel());
        currentLevel.setFont(Font.loadFont("file:Pokemon_Classic.ttf", 18));
        nameLabel.setFont(Font.loadFont("file:Pokemon_Classic.ttf", 18));
        currentInfo.setFont(Font.loadFont("file:Pokemon_Classic.ttf",14));
        currentInfo.setWrapText(true);
        inv.setFont(Font.loadFont("file:Pokemon_Classic.ttf",14));
    }

    public static VBox createRightPane(Inventory inventory, GameMap map, Label nameLabel, Label inv, Label currentInfo) {
        nameLabel.setText(map.getPlayer().getUserName());
        inv.setText(inventory.toString());
        inv.setWrapText(true);
        VBox inventoryBox = new VBox(nameLabel, inv);
        inventoryBox.setPrefWidth(300);
        inventoryBox.setPrefHeight(500);
        inventoryBox.setPadding(new Insets(10));

        VBox infoBox = LayoutItem.createInfoBox(currentInfo);
        VBox rightPane = new VBox(inventoryBox, infoBox);
        rightPane.setSpacing(20.00);
        return rightPane;
    }

    public static void gameEndWindow(EndCondition endCondition, Stage pStage) {
        Stage endPopup = new Stage();
        endPopup.initModality(Modality.WINDOW_MODAL);
        endPopup.initOwner(pStage);
        VBox endContent = new VBox();
        Scene endScene = new Scene(endContent);
        Text winText = new Text("Congratulations! You won!");
        Text loseText = new Text("You lost. Try again!");
        Text displayedText = endCondition == EndCondition.WIN? winText : loseText;
        Button closeWindow = new Button("Quit game");
        closeWindow.setFont(Font.loadFont("file:Pokemon_Classic.ttf", 14));
        displayedText.setFont(Font.loadFont("file:Pokemon_Classic.ttf", 22));
        endContent.setAlignment(Pos.CENTER);

        closeWindow.setOnAction((event)-> {
            try {
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        endContent.getChildren().addAll(displayedText, closeWindow);
        endContent.setPrefSize(800.0/2,761.0/2);
        Background background = new Background(new BackgroundImage(
                new Image(endCondition == EndCondition.LOSE? "/lose.png": "/win.png"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO,
                BackgroundSize.AUTO,
                false, false, true, true)));

        endPopup.setScene(endScene);
        endContent.setBackground(background);
        endPopup.show();
    }
}
