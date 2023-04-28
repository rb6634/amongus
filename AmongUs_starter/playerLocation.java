/*
 * @Instructor: Alan Mutka
 * @author: Rinor Bugujevci, Uejs Hamja
 * Project: Among us
 * @version: 28/04/2023
 * ISTE: 121.801
 */

import java.io.Serializable;

public class playerLocation implements Serializable {
    private int X;
    private int y;
    private String name;
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public playerLocation(int x, int y, String name, int index) {
        this.X = x;
        this.y = y;
        this.name = name;
        this.index = index;
    }

    public playerLocation(int x, int y) {
        this.X = x;
        this.y = y;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
