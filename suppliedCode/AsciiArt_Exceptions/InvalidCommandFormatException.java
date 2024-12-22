package AsciiArt_Exceptions;

/**
 * An exception class that is thrown when the command format is invalid.
 */
public class InvalidCommandFormatException extends Exception {
    /**
     * Constructor. Calls the super constructor.
     * @param message: The message to be printed.
     */
    public InvalidCommandFormatException(String message) {
        super(message);
    }
}
