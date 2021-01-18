package com.codecool.dungeoncrawl.logic.ui;

import com.codecool.dungeoncrawl.dao.Converter;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.game.Game;
import com.codecool.dungeoncrawl.logic.map.GameMap;
import com.codecool.dungeoncrawl.logic.items.Inventory;
import com.codecool.dungeoncrawl.model.GameState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Date;
import java.util.List;

public class WindowElement {


    public static VBox createMainPane(Button newGameButton, Button loadGameButton, Button importGameButton ) {
        VBox mainPane = new VBox(20, newGameButton, loadGameButton, importGameButton);
        mainPane.setPrefSize(1287/1.5,797/1.5);
        Background background = createBackground("/images/main_menu.png");
        mainPane.setBackground(background);
        mainPane.setAlignment(Pos.CENTER);
        mainPane.requestFocus();
        return mainPane;
    }

    private static Background createBackground(String path){
        return new Background(new BackgroundImage(new Image(path),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO,
                BackgroundSize.AUTO,
                false, false, true, true)));
    }

    public static Button createButton(String text, int fontSize){
        Button loadGameButton = new Button(text);
        loadGameButton.setFont(Font.loadFont("file:Pokemon_Classic.ttf", fontSize));
        return loadGameButton;
    }

    public static Scene createLoadGameMenu(Stage primaryStage, Scene mainMenu, Converter converter){
        VBox loadGamePane = new VBox(20);
        loadGamePane.setPrefSize(1287/1.5,797/1.5);
        Background background = createBackground("/images/main_menu.png");
        loadGamePane.setBackground(background);
        loadGamePane.setAlignment(Pos.CENTER);
        loadGamePane.requestFocus();

        //Get saves and convert result to ObservableList.
        List<GameState> saves = converter.getAllSaves();
        ObservableList<GameState> saves2 = FXCollections.observableArrayList(saves);

        //Create columns
        TableColumn<GameState, Player> playerColumn = new TableColumn<>("Player");
        playerColumn.setMinWidth(200);
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));

        TableColumn<GameState, GameState> saveNameColumn = new TableColumn<>("Save");
        saveNameColumn.setMinWidth(200);
        saveNameColumn.setCellValueFactory(new PropertyValueFactory<>("saveName"));

        TableColumn<GameState, Date> dateTableColumn = new TableColumn<>("Saved on");
        dateTableColumn.setMinWidth(200);
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("savedAt"));

        //Set table properties
        TableView<GameState> table = new TableView<>();
        table.setPlaceholder(new Label("You do not have any saves yet!"));
        table.getStyleClass().add("pokeFont"); //stylesheet not added yet
        table.setItems(saves2);
        table.setEditable(false);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        table.getColumns().addAll(playerColumn, saveNameColumn, dateTableColumn);

        Button navigateBackButton = new Button("Main menu");
        navigateBackButton.setFont(Font.loadFont("file:Pokemon_Classic.ttf", 14));
        navigateBackButton.setOnMouseClicked((event)->{
            primaryStage.setScene(mainMenu);
        });

        Button loadSelectedButton = new Button("Load selected game");
        loadSelectedButton.setOnMouseClicked((event)->{
            GameState selectedSave = table.getSelectionModel().getSelectedItem();
            selectedSave = converter.returnFullGameStateOf(selectedSave.getId());
            System.out.println(selectedSave);
            Game game = new Game(selectedSave, converter, primaryStage);

            primaryStage.setScene(game.showGameScene());

        });
        loadSelectedButton.setFont(Font.loadFont("file:Pokemon_Classic.ttf", 14));
        loadGamePane.getChildren().addAll(navigateBackButton, table, loadSelectedButton);

        return new Scene(loadGamePane);

    }


    private static TextField createNameInput() {
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
        currentLevel.setText("Level " + map.getLevel());
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

        VBox infoBox = WindowElement.createInfoBox(currentInfo);
        VBox rightPane = new VBox(inventoryBox, infoBox);
        rightPane.setSpacing(20.00);
        return rightPane;
    }

    public static String saveWindow(Stage pStage, String defaultName) {
        StringBuilder inputtedName = new StringBuilder();
        Stage savePopup = new Stage();
        savePopup.initModality(Modality.APPLICATION_MODAL);
        savePopup.initOwner(pStage);

        Label nameLabel = new Label("Name: ");
        TextField nameField = new TextField();
        nameField.setText(defaultName);
        HBox nameBox = new HBox(nameLabel, nameField);

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        saveButton.setOnAction(event -> {
            inputtedName.append(nameField.getText());
            savePopup.close();
        });
        cancelButton.setOnAction(event -> savePopup.close());
        HBox buttonBox = new HBox(saveButton, cancelButton);

        VBox saveBox = new VBox(nameBox, buttonBox);

        Scene saveScene = new Scene(saveBox);
        savePopup.setScene(saveScene);
        savePopup.showAndWait();
        return (inputtedName.toString().equals(""))? null : inputtedName.toString();
    }

    public static boolean confirmSaveWindow(Stage pStage){
        final boolean[] answer = new boolean[1];
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Save name already exists.");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Do you want to overwrite previous gamestate?");
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        confirmAlert.getButtonTypes().setAll(noButton, yesButton);
        confirmAlert.showAndWait().ifPresent(type -> {
            if (type.getButtonData() == ButtonBar.ButtonData.YES) answer[0] = true;
            else answer[0] = false;
        });
        return answer[0];
    }

    public static void importErrorWindow(Stage pStage) {
        Stage importPopup = new Stage();
        importPopup.initModality(Modality.APPLICATION_MODAL);
        importPopup.initOwner(pStage);

        importPopup.setTitle("IMPORT ERROR!");
        Label label1= new Label("IMPORT ERROR! Unfortunately the given file is in wrong format. Please try another one!");
        label1.setMaxWidth(200);
        label1.setWrapText(true);

        Button cancelButton = new Button("Cancel");
        Button okButton = new Button("Ok");

        cancelButton.setOnAction(e -> importPopup.close());
        okButton.setOnAction(e -> importPopup.close());
        VBox layout= new VBox(10);
        layout.getChildren().addAll(label1, cancelButton, okButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene1= new Scene(layout, 300, 250);
        importPopup.setScene(scene1);
        importPopup.showAndWait();
    }

    public static void gameEndWindow(int endCondition, Stage pStage) {
        Stage endPopup = new Stage();
        endPopup.initModality(Modality.WINDOW_MODAL);
        endPopup.initOwner(pStage);
        VBox endContent = new VBox();
        Scene endScene = new Scene(endContent);
        Text winText = new Text("Congratulations! You won!");
        Text loseText = new Text("You lost. Try again!");
        Text displayedText = endCondition == 1? winText : loseText;
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
        Background background = createBackground(endCondition == -1? "/images/lose.png" : "/images/win.png");

        endPopup.setScene(endScene);
        endContent.setBackground(background);
        endPopup.show();
    }

    public static void refreshRangeInfo(StringBuilder rangeText, StringBuilder actionsText, Label currentInfo, GameMap map) {
        rangeText.setLength(0);
        if (map.getPokemonInRange().isPresent()) {
            rangeText.append("\n\nPokemon in range:\n");
            map.getPokemonInRange().get().forEach(p -> rangeText.append("\n" + p.toString()));
        }
        currentInfo.setText(actionsText.toString() + "\n" + rangeText.toString());
    }

    public static void refreshLevelAndInventory(Inventory inventory, Label inv, Label currentLevel, GameMap map) {
        inv.setText(inventory.toString());
        currentLevel.setText("Level " + map.getLevel());
    }

    public static String createStartScreen(Stage pStage) {
        Stage startPopup = new Stage();
        startPopup.initModality(Modality.APPLICATION_MODAL);
        startPopup.initOwner(pStage);
        startPopup.setTitle("START GAME!");

        Label instruction = new Label("Enter your name!");
        TextField inputField = createNameInput();
        Button startButton = new Button("Start");
        Button cancelButton = new Button("Cancel");
        StringBuilder name = new StringBuilder();
        startButton.setOnMouseClicked((event) -> {
            if ("".equals(inputField.getText())) emptyNameAlert();
            else {
                name.append(inputField.getText());
                startPopup.close();
            }
        });
        cancelButton.setOnMouseClicked((event) -> startPopup.close());

        VBox layout= new VBox(10, instruction, inputField, startButton, cancelButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene1= new Scene(layout, 300, 250);
        startPopup.setScene(scene1);
        startPopup.showAndWait();

        return name.toString();
    }

    public static void emptyNameAlert(){
        Alert nameAlert = new Alert(Alert.AlertType.CONFIRMATION);
        nameAlert.setTitle("Missing username");
        nameAlert.setHeaderText(null);
        nameAlert.setContentText("Username field cannot be left empty!");
        ButtonType okButton = new ButtonType("GOT IT!", ButtonBar.ButtonData.NO);
        nameAlert.getButtonTypes().setAll(okButton);
        nameAlert.showAndWait();
    }
}
