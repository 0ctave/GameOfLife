package fr.octave.lifegame;

import fr.octave.lifegame.array.Cell;
import fr.octave.lifegame.array.CellArray;
import fr.octave.lifegame.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private static CellArray array;
    private static Scene scene;
    private GameRunnable game;
    private MainController mainController;
    private GridPane grid;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fr/octave/lifegame/fxml/main.fxml"));

        Parent root = loader.load();
        mainController = loader.getController();
        scene = new Scene(root, 1000, 800);
        primaryStage.setTitle("Game of Life");
        primaryStage.setScene(scene);
        primaryStage.show();

        mainController.main = this;
        array = new CellArray(30, 20);
        setCell(5, 5, true);
        setCell(5, 6, true);
        setCell(5, 7, true);

        ((TextField) getNode("xSize")).textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*"))
                ((TextField) getNode("xSize")).setText(newValue.replaceAll("[^\\d]", ""));
            if(newValue.length() > 0 && Integer.parseInt(newValue) > 1000)
                ((TextField) getNode("xSize")).setText(oldValue);
        });

        ((TextField) getNode("ySize")).textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*"))
                ((TextField) getNode("xSize")).setText(newValue.replaceAll("[^\\d]", ""));
            if(newValue.length() > 0 && Integer.parseInt(newValue) > 1000)
                ((TextField) getNode("xSize")).setText(oldValue);
        });

        grid = (GridPane) getNode("grid");
        initializeGrid(grid);

        game = new GameRunnable("Game Of Life");
        game.setMain(this);
        Thread thread = new Thread(game);

        thread.setDaemon(true);
        thread.start();
        game.pause();
    }

    private void initializeGrid(GridPane grid) {
        grid.setGridLinesVisible(true);
        for (int y = 0; y < array.getYSize(); y++) {
            for (int x = 0; x < array.getXSize(); x++) {
                addRectangle(x, y);
            }
        }
    }

    private static void setCell(int x, int y, boolean state) {
        Cell cell = array.getCell(x, y);
        array.setCell(cell, state);
        if (state)
            array.getAlive().add(cell);
        else
            array.getAlive().remove(cell);
    }

    public void removeCell(Cell cell) {
        ((Rectangle) getNode(array.getIndex(cell))).setFill(Color.WHITE);
        array.getAlive().remove(cell);
        cell.setState(false);
    }

    public void addCell(Cell cell) {
        ((Rectangle) getNode(array.getIndex(cell))).setFill(Color.BLACK);
        array.getAlive().add(cell);
        cell.setState(true);
    }

    public void removeRows(int rows) {
        if(array.getYSize() > rows) {
            List<Node> nodes = new ArrayList<>();
            for (int i = 0; i < array.getXSize(); i++) {
                for (int u = array.getYSize() - rows; u < array.getYSize(); u++) {
                    nodes.add(getNode(array.getIndex(i, u)));
                    System.out.println(array.getIndex(i, u));
                }
            }
            array.resize(array.getXSize(), array.getYSize() - rows);
            grid.getChildren().removeAll(nodes);
        }
    }

    public void addRows(int rows) {
        for(int i = 0; i < array.getXSize(); i++) {
            for(int u = array.getYSize(); u < array.getYSize() + rows; u++) {
                addRectangle(i, u);
            }
        }
        array.resize(array.getXSize(), array.getYSize() - rows);
    }

    private void addRectangle(int x, int y) {
        int size = 13;
        Rectangle rectangle = new Rectangle(0, 0, size, size);
        if (array.getCell(x, y).getState())
            rectangle.setFill(Color.BLACK);
        else
            rectangle.setFill(Color.WHITE);
        rectangle.setId(String.valueOf(array.getIndex(x, y)));
        grid.add(rectangle,x, y);
    }

    public Node getNode(int id) {
        return scene.lookup("#" + id);
    }

    private Node getNode(String id) {
        return scene.lookup("#" + id);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public GameRunnable getGame() {
        return game;
    }

    public CellArray getArray() {
        return array;
    }

    MainController getController() {
        return mainController;
    }
}
