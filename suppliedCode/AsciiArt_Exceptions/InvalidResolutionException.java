package AsciiArt_Exceptions;

/**
 * An exception class that is thrown when the resolution is invalid.
 */
public class InvalidResolutionException extends Exception {
  /**
   * Constructor. Calls the super constructor.
   * @param message: The message to be printed.
   */
    public InvalidResolutionException(String message) {

      super(message);
    }
}
