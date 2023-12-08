package map.utils;

public class Coordinates {
    private final int x;
    private final int y;

    public Coordinates(int x, int y) {
        if (x < 0 || y < 0)
            throw new IllegalArgumentException("Position cannot be negative");

        this.x = x;
        this.y = y;
    }

    public static double distance(Coordinates x1, Coordinates x2) {
        return Math.sqrt(Math.pow(x1.getx() - x2.getx(), 2) + Math.pow(x1.gety() - x2.gety(), 2));
    }

    public int getx() {
        return x;
    }

    public int gety() {
        return y;
    }

    @Override
    public int hashCode() {
        // this will always return a unique number as x is from 0 to 16 and y is also
        // from 0 to 16
        return x + 100 * y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof Coordinates pos))
            return false;

        return pos.getx() == getx() && pos.gety() == gety();
    }

    @Override
    public String toString() {
        return super.toString() + "; x = " + x + ", y = " + y;
    }

}
