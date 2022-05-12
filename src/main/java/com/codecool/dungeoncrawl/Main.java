package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Player;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;

public class Main extends Application {
    GameMap map = MapLoader.loadMap("/map.txt");
    Canvas canvas = new Canvas(
            35 * Tiles.TILE_WIDTH,
            18 * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    Label healthLabel = new Label();
    Label inventoryLabel = new Label();
    Label strengthLabel = new Label();
    private Button pickUpButton = new Button("Pick up item!");
    public Alert lostGame = new Alert(Alert.AlertType.INFORMATION);
    Stage stage;
    GameDatabaseManager dbManager;

    Timeline timeline = new Timeline( new KeyFrame(Duration.millis(500), event -> refresh()));
    double contextScale = 1.2;
    int minX, minY, maxX, maxY;
    boolean isKeyPressed = false;

    public static void main(String[] args) {
        launch(args);
    }

    public void setAlert(Alert alert, String string) {
        alert.setTitle("Dungeon Crawl");
        alert.setHeaderText(string);
        alert.setX(685);
        alert.setY(350);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setupDbManager();
        this.stage = primaryStage;
        submitName(primaryStage);
    }

    public void submitName(Stage primaryStage) throws FileNotFoundException {
        Button startButton = new Button("Start game");
        startButton.setId("btn");
        HBox button = new HBox(startButton);
        Text championNameLabel = new Text("Submit your name:");
        championNameLabel.setId("text");
        TextField textField = new TextField();
        VBox nameLayout = new VBox(championNameLabel, textField, button);
        nameLayout.setAlignment(Pos.CENTER);
        startButton.addEventFilter(MouseEvent.MOUSE_CLICKED, (e) -> {
            try {
                map.getPlayer().setName(textField.getText());
                gameStart(primaryStage);
            }catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        nameLayout.setSpacing(25);
        BorderPane menuLayout = new BorderPane();
        menuLayout.setBackground(new Background(new BackgroundFill(Color.rgb(71,45,60), CornerRadii.EMPTY, Insets.EMPTY)));
        menuLayout.setPrefWidth(800);
        menuLayout.setPrefHeight(600);
        menuLayout.setCenter(nameLayout);
        HBox.setMargin(startButton, new Insets(10,10,10,10));
        button.setAlignment(Pos.CENTER);
        Scene scene = new Scene(menuLayout);
        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();
    }

    public void gameStart(Stage primaryStage) throws Exception {
        setAlert(lostGame, "Game over, " + map.getPlayer().getName()+ " has died.");
        pickUpButton.setFocusTraversable(false);

        GridPane ui = new GridPane();
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));

        ui.add(new Label("Name: "),0,0);
        ui.add(new Label(map.getPlayer().getName()), 1, 0);
        ui.add(new Label("Health: "), 0, 1);
        ui.add(healthLabel, 1, 1);
        ui.add(new Label("Inventory: "), 0, 2);
        ui.add(inventoryLabel, 1, 2);
        ui.add(new Label("Strength: "), 0, 3);
        ui.add(strengthLabel, 1, 3);
        ui.add(pickUpButton, 0, 4);


        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(canvas);
        borderPane.setRight(ui);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        enemyRefresh();
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);

        pickUpButton.addEventFilter(MouseEvent.MOUSE_CLICKED, (e) -> {
            map.getPlayer().itemPickUp();
            refresh();
        });


        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();
    }


    private void onKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:
                if (map.getPlayer().getY() != 0)
                    map.getPlayer().move(0, -1);
                isKeyPressed = true;
                refresh();
                break;
            case DOWN:
                if (map.getPlayer().getY() != map.getHeight() - 2)
                    map.getPlayer().move(0, 1);
                isKeyPressed = true;
                refresh();
                break;
            case LEFT:
                if (map.getPlayer().getX() != 0)
                    map.getPlayer().move(-1, 0);
                isKeyPressed = true;
                refresh();
                break;
            case RIGHT:
                if (map.getPlayer().getX() != map.getWidth() - 2)
                    map.getPlayer().move(1,0);
                isKeyPressed = true;
                refresh();
                break;
            case S:
                Player player = map.getPlayer();
                dbManager.savePlayer(player);
                break;
        }
        if (map.getPlayer().isNextMap()) {
            setMap(MapLoader.loadMap("/map2.txt"));
        }

        if (map.getPlayer().isDead()){
            lostGame.showAndWait();
            try {
                map = MapLoader.loadMap("/map.txt");
                submitName(stage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public void setMap(GameMap map) {
        this.map = map;
    }

    private void refresh() {
        if (isKeyPressed)
            moveEnemies();

        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        setBounds();

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x - minX, y - minY);
                } else if (cell.getItem() != null) {
                    Tiles.drawTile(context, cell.getItem(), x - minX, y - minY);

                }else{ Tiles.drawTile(context, cell, x - minX, y - minY);
                }
            }
        }
        healthLabel.setText("" + map.getPlayer().getHealth());
        strengthLabel.setText("" + map.getPlayer().getStrength());
        inventoryLabel.setText("" + map.getPlayer().inventoryToString());
    }


    private void setBounds() {
        minX = (int) (map.getPlayer().getX() - ((map.getWidth() / contextScale - 1) / 2));
        minY = (int) (map.getPlayer().getY() - ((map.getHeight() / contextScale - 1) / 2));
        maxX = (int) (map.getPlayer().getX() + ((map.getWidth() / contextScale + 1) / 2));
        maxY = (int) (map.getPlayer().getY() + ((map.getHeight() / contextScale + 2) / 2));

        if (minX < 0) {
            maxX -= minX;
            minX = 0;
        }
        if (maxX > map.getWidth() - 1) {
            maxX = map.getWidth() - 1;
            minX = (int) (map.getWidth() - 1 - map.getWidth() / contextScale);
        }
        if (minY <= 0) {
            maxY -= minY - 1;
            minY = 0;
        }
        if (maxY > map.getHeight() - 1) {
            maxY = map.getHeight() - 1;
            minY = (int) (map.getHeight() - 1 - map.getHeight() / contextScale);
        }
    }

    public void enemyRefresh(){
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void stopEnemiesRefresh() {
        timeline.stop();
    }

    public void moveEnemies() {
        if (map.getEnemies().size() == 0) {
            stopEnemiesRefresh();
            return;
        }
        try {
            for (Actor enemy : map.getEnemies()) {
                enemy.move(0,0);
            }
        } catch (Exception ignored) {
        }
    }

    private void setupDbManager() {
        dbManager = new GameDatabaseManager();
        try {
            dbManager.setup();
        } catch (SQLException ex) {
            System.out.println("Cannot connect to database.");
        }
    }
}

//ceva text
