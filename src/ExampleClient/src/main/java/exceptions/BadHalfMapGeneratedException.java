package exceptions;

public class BadHalfMapGeneratedException extends Exception {

    public BadHalfMapGeneratedException() {
        super("HalfMapGeneration went wrong, please try again!");
    }

}
