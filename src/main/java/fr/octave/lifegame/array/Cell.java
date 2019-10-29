package fr.octave.lifegame.array;

import java.util.SplittableRandom;

public class Cell {

    int x;
    int y;
    boolean state = false;

    public Cell(int x, int y, boolean state) {
        this.x = x;
        this.y = y;
        this.state = state;
    }

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String toString() {
        return "x : " + getX() + "  y : " + getY() + "  state : " + getState();
    }
}
