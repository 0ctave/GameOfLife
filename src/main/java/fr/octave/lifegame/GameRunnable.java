package fr.octave.lifegame;

import fr.octave.lifegame.array.Cell;

import java.util.ArrayList;
import java.util.List;

public class GameRunnable implements Runnable {
    private String threadName;
    private volatile boolean running = true;
    private volatile boolean paused = false;
    private final Object pauseLock = new Object();
    private Main main;

    GameRunnable(String threadName) {
        this.threadName = threadName;
    }

    public void run() {
        System.out.println("Thread started, threadName=" + this.threadName + ", hashCode=" + this.hashCode());
        while (running) {
            synchronized (pauseLock) {
                if (!running)
                    break;
                if (paused) {
                    try {
                        synchronized (pauseLock) {
                            pauseLock.wait();
                        }
                    } catch (InterruptedException ex) {
                        break;
                    }
                    if (!running)
                        break;
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
            }
            //System.out.println("Thread looping, threadName=" + this.threadName + ", hashCode=" + this.hashCode());

            main.getController().next();
        }
    }

    public String toString() {
        return "whoAmI, threadName=" + this.threadName + ", hashCode=" + this.hashCode();
    }

    public void stop() {
        running = false;
        // you might also want to interrupt() the Thread that is
        // running this Runnable, too, or perhaps call:
        resume();
        // to unblock
    }

    public void pause() {
        // you may want to throw an IllegalStateException if !running
        paused = true;
    }

    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll(); // Unblocks thread
        }
    }

    public boolean isPaused() {
        return paused;
    }

    void setMain(Main main) {
        this.main = main;
    }
}