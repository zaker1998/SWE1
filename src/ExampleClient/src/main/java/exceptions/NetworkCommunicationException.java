package exceptions;

public class NetworkCommunicationException extends Exception {
    public NetworkCommunicationException(String exceptionMessage) {
        super("Error in the network communication occurred! " + exceptionMessage);
    }
}