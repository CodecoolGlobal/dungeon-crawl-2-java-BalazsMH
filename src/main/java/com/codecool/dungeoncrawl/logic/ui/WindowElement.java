package com.codecool.dungeoncrawl.logic.ui;

import com.codecool.dungeoncrawl.dao.Converter;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.EndCondition;
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


    public static VBox createMainPane(TextField nameInput, Button newGameButton, Button loadGameButton, Button importGameButton ) {
        VBox mainPane = new VBox(20, nameInput, newGameButton, loadGameButton, importGameButton);
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

    public static Button createNewGameButton() {
        Button newGameButton = new Button("Start a new Game!");
        newGameButton.setFont(Font.loadFont("file:Pokemon_Classic.ttf", 14));
        return newGameButton;
    }

    public static Button createLoadGameButton() {
        Button loadGameButton = new Button("Load Game");
        loadGameButton.setFont(Font.loadFont("file:Pokemon_Classic.ttf", 14));
        return loadGameButton;
    }

    public static Button createImportGameButton() {
        Button loadGameButton = new Button("Import Game");
        loadGameButton.setFont(Font.loadFont("file:Pokemon_Classic.ttf", 14));
        return loadGameButton;
    }

    public static Button createExportGameButton() {
        Button loadGameButton = new Button("Export Game");
        loadGameButton.setFont(Font.loadFont("file:Pokemon_Classic.ttf", 14));
        return loadGameButton;
    }

    public static Scene createLoadGameMenu(Stage primaryStage, Scene mainMenu, Converter converter){
        VBox loadGamePane = new VBox(20);
        loadGamePane.setPrefSize(1287/1.5,797/1.5);
        Background background = new Background(new BackgroundImage(new Image("/main_menu.png"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO,
                BackgroundSize.AUTO,
                false, false, true, true)));
        loadGamePane.setBackground(background);
        loadGamePane.setAlignment(Pos.CENTER);
        loadGamePane.requestFocus();

        //Get saves and convert result to ObservableList.
        List<GameState> saves = converter.getAllSaves();
        ObservableList<GameState> saves2 = FXCollections.observableArrayList(saves);

        //Create columns
        TableColumn<GameState, Player> playerColumn = new TableColumn<>("Player");
        playerColumn.setMinWidth(300);
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));

        TableColumn<GameState, Date> dateTableColumn = new TableColumn<>("Saved on");
        dateTableColumn.setMinWidth(300);
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("savedAt"));

        TableColumn<GameState, Integer> currentMapTableColumn = new TableColumn<>("Current level");
        currentMapTableColumn.setMinWidth(200);
        currentMapTableColumn.setCellValueFactory(new PropertyValueFactory<>("currentMap"));

        //Set table properties
        TableView<GameState> table = new TableView<>();
        table.setPlaceholder(new Label("You do not have any saves yet!"));
        table.getStyleClass().add("pokeFont"); //stylesheet not added yet
        table.setItems(saves2);
        table.setEditable(false);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        //TODO: currentMap temporarily disabled, since it loaded the whole map layout. Find different solution.
        table.getColumns().addAll(playerColumn, dateTableColumn/*, currentMapTableColumn*/);

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

            Game game = new Game(selectedSave, converter);

            primaryStage.setScene(game.showGameScene());

        });
        loadSelectedButton.setFont(Font.loadFont("file:Pokemon_Classic.ttf", 14));
        loadGamePane.getChildren().addAll(navigateBackButton, table, loadSelectedButton);



        return new Scene(loadGamePane);

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

    public static void refreshInfoWindow(StringBuilder text, Label currentInfo, GameMap map) {
        text.setLength(0); //TODO deletes some important info as well, but prevents multiplying others
        Cell standingOn = map.getPlayer().getCell();
        if (standingOn.getDoor() != null){
            text.append("\nOpen door by 'O'\n");
        } else if (standingOn.getItem() != null){
            text.append(String.format("\nPick up %s by 'E'!\n", standingOn.getItem().getTileName()));
        }
        if (map.getPokemonInRange(currentInfo).isPresent()) {
            text.append("\n\nPokemon in range:\n");
            map.getPokemonInRange(currentInfo).get().forEach(p -> text.append("\n" + p.toString()));
        }
        currentInfo.setText(text.toString());
    }

    public static void refreshLevelAndInventory(Inventory inventory, Label inv, Label currentLevel, GameMap map) {
        inv.setText(inventory.toString());
        currentLevel.setText("Level " + map.getLevel());
    }
}
