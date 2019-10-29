package fr.octave.lifegame.array;

import java.util.*;

public class CellArray {
    private int xSize;
    private int ySize;
    private Cell[] array;
    public static List<Cell> alive = new ArrayList<>();


    public CellArray(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.array = new Cell[xSize * ySize];
        initialize();
    }

    public void resize(int xSize, int ySize) {
        Cell[] tempArray = new Cell[xSize * ySize];
        int colsToCopy = Math.min(xSize, this.xSize);
        int rowsToCopy = Math.min(ySize, this.ySize);
        for (int i = 0; i < rowsToCopy; ++i) {
            int oldRowStart = getIndex(0, i, this.xSize);
            int newRowStart = getIndex(0, i, xSize);
            System.arraycopy(array, oldRowStart, tempArray, newRowStart,
                    colsToCopy
            );
        }
        this.xSize = xSize;
        this.ySize = ySize;
        array = tempArray;
    }

    private void initialize() {
        for (int i = 0; i < array.length; i++)
            array[i] = new Cell(i % xSize, i / xSize);
    }

    private int getIndex(int x, int y, int xSize) {
        return y * xSize + x;
    }

    public int getIndex(int x, int y) {
        return y * xSize + x;
    }

    public int[] getIndex(int id) {
        return new int[]{id % xSize, id / xSize};
    }


    public int getIndex(Cell cell) {
        return getIndex(cell.getX(), cell.getY());
    }

    public Cell getCell(int x, int y) {
        if(x < 0 || y < 0 || x > xSize - 1 || y > ySize - 1)
            return new Cell(0, 0, false);
        return array[getIndex(x, y)];
    }

    private void setCell(int x, int y, boolean state) {
        array[getIndex(x, y)].setState(state);
    }

    public void setCell(Cell cell, boolean state) {
        setCell(cell.getX(), cell.getY(), state);
    }

    public Cell[] getArray() {
        return array;
    }

    public List<Cell> getAlive() {
        return alive;
    }

    public int getXSize() {
        return xSize;
    }

    public int getYSize() {
        return ySize;
    }

    public int getSize() {
        return array.length;
    }

    public int getNeighboursCount(Cell cell) {
        return getNeighbours(cell).size();
    }

    public List<Cell> getNeighbours(Cell cell) {
        List<Cell> neighbours = new ArrayList<>();
        for (int i = -1; i < 2; i++)
            for (int u = -1; u < 2; u++)
                if (getCell(cell.getX() + i, cell.getY() + u).getState())
                    neighbours.add(getCell(cell.getX() + i, cell.getY() + u));

        neighbours.remove(cell);
        return neighbours;
    }

    public List<Cell> getNeighbourhoods(Cell cell) {
        List<Cell> newCells = new ArrayList<>();
        for (int f = -1; f < 2; f++)
            for (int g = -1; g < 2; g++) {
                Cell tempCell = getCell(cell.getX() + f, cell.getY() + g);
                List<Cell> neighbours = getNeighbours(tempCell);
                if (neighbours.size() == 3) {
                    newCells.add(getCell(tempCell.getX(), tempCell.getY()));
                }
            }
        return newCells;
    }
}
