package exceptions;

import map.utils.Coordinates;

public class CoordinatesOutOfBoundsException extends IllegalArgumentException {
    public CoordinatesOutOfBoundsException(String s, Coordinates pos) {
        super(s + "; Illegal Position object: " + pos.toString());
    }
}
