package fr.octave.lifegame.controller;

import fr.octave.lifegame.Main;
import fr.octave.lifegame.array.Cell;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class MainController {

    public Main main;

    @FXML
    GridPane grid;

    @FXML
    TextField xSize;

    @FXML
    TextField ySize;

    public void clickGrid(MouseEvent mouseEvent) {
        Node clickedNode = mouseEvent.getPickResult().getIntersectedNode();
        if (clickedNode != grid) {

            Integer x = GridPane.getColumnIndex(clickedNode);
            Integer y = GridPane.getRowIndex(clickedNode);
            if(x != null && y != null) {
                System.out.println("Mouse clicked column: " + x + " row: " + y);

                if (main.getArray().getCell(x, y).getState()) {
                    main.removeCell(main.getArray().getCell(x, y));
                } else {
                    main.addCell(main.getArray().getCell(x, y));
                }
            }
        }
    }

    public void start() {
        main.getGame().resume();
    }

    public void stop() {
        main.getGame().pause();
    }

    public void reset() {
        stop();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (Cell cell : main.getArray().getAlive()) {
            ((Rectangle)main.getNode(main.getArray().getIndex(cell))).setFill(Color.WHITE);
            cell.setState(false);
        }
        main.getArray().getAlive().clear();
    }

    public void previous() {
    }

    public void next() {
        List<Cell> dyingCells = new ArrayList<>();
        List<Cell> newCells = new ArrayList<>();
        for (Cell cell : main.getArray().getAlive()) {
            int n = main.getArray().getNeighboursCount(cell);
            if (n > 3 || n < 2)
                dyingCells.add(cell);
            for (Cell newCell : main.getArray().getNeighbourhoods(cell)) {
                if (!newCells.contains(newCell)) {
                    newCells.add(newCell);
                }
            }
        }

        for (Cell cell : newCells)
            main.addCell(cell);

        for (Cell cell : dyingCells)
            main.removeCell(cell);

    }

    public void load() {
        main.removeRows(5);
    }

    public void resize() {
        int xSize = Integer.parseInt(this.xSize.getText());
        int ySize = Integer.parseInt(this.ySize.getText());

        System.out.println("x: " + xSize + "  y: " + ySize);
    }
}
