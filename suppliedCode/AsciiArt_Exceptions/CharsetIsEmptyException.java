package AsciiArt_Exceptions;

/**
 * An exception class that is thrown when the charset is empty and the code tries to use it.
 */
public class CharsetIsEmptyException extends Exception {
    /**
     * Constructor. Calls the super constructor.
     * @param message: The message to be printed.
     */
    public CharsetIsEmptyException(String message) {
        super(message);
    }
}
