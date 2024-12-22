package ascii_art;

import image.Image;
import image.ProcessingImage;
import image_char_matching.SubImgCharMatcher;
import ascii_output.*;

import java.io.IOException;
import AsciiArt_Exceptions.*;
/**
 * Shell class providing a command-line interface for ASCII art generation.
 * Supports multiple commands to configure and execute the ASCII art algorithm.
 */
public class Shell {
    /* Class constants: */
    // Commands
    /** Command string for exiting the program. */
    private static final String EXIT_COMMAND = "exit";
    /** Command string for displaying the current character set. */
    private static final String DISPLAY_CHARS_COMMAND = "chars";
    /** Command string for adding characters to the character set. */
    private static final String ADD_CHARACTER_COMMAND = "add";
    /** Command string for removing characters from the character set. */
    private static final String REMOVE_CHARACTER_COMMAND = "remove";
    /** Command string for adjusting the resolution of the ASCII art. */
    private static final String ADJUST_RESOLUTION_COMMAND = "res";
    /** Command string for switching the output method (console or HTML). */
    private static final String SWITCH_OUTPUT_COMMAND = "output";
    /** Command string for changing the rounding method used for brightness matching. */
    private static final String CHANGE_ROUNDING_COMMAND = "round";
    /** Command string for running the ASCII art generation algorithm. */
    private static final String RUN_ASCIIART_COMMAND = "asciiArt";

    //default values
    /** Numeric constant representing the number three, used for range validation. */
    private static final int THREE = 3;
    /** The minimum ASCII value for valid characters. */
    private static final int MIN_ASCII = 32;
    /** The maximum ASCII value for valid characters. */
    private static final int MAX_ASCII = 126;
    /** Default set of characters used for ASCII art generation. */
    public static final char[] DEFAULT_CHARS_SET = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    /** Default resolution value for the ASCII art generation. */
    public static final int DEFAULT_RESOLUTION = 2;
    /** The string displayed to the user to prompt for input. */
    public static final String INPUT_STRING = ">>> ";
    /** The message displayed when the resolution is successfully set. */
    public static final String RESOLUTION_SET_MESSAGE = "Resolution set to ";
    /** The default file name for saving HTML output. */
    public static final String OUT_FILE_NAME = "out.html";
    /** The font name used for generating ASCII art output. */
    public static final String FONT_NAME = "Courier New";
    /** Error message displayed when a character addition fails due to incorrect format. */
    public static final String ADD_ERROR_MESSAGE = "Did not add due to incorrect format.";
    /** Error message displayed when a character removal fails due to incorrect format. */
    public static final String REMOVE_ERROR_MESSAGE = "Did not remove due to incorrect format.";
    /** Error message displayed when the resolution command format is incorrect. */
    public static final String RESOLUTION_COMMAND_FORMAT_ERROR_MESSAGE = "Did not change resolution" +
            " due to incorrect format.";
    /** Error message displayed when the resolution is outside valid boundaries. */
    public static final String RESOLUTION_BOUNDRIES_ERROR_MESSAGE = "Did not change resolution" +
            " due to exceeding boundaries.";
    /** Error message displayed when the character set is too small to generate ASCII art. */
    public static final String CHARSET_SIZE_ERROR_MESSAGE = "Did not execute. Charset is too small.";
    /** Error message displayed when the rounding method format is incorrect. */
    public static final String INVALID_ROUNDING_FORMAT_MESSAGE = "Did not change rounding method" +
            " due to incorrect format.";
    /** Error message displayed when the output method format is incorrect. */
    public static final String INVALID_FORMAT_OUTPUT_MESSAGE = "Did not change output method" +
            " due to incorrect format.";
    /** Error message displayed when an unrecognized command is entered. */
    public static final String INVALID_COMMAND_MESSAGE = "Did not execute due to incorrect command.";


    /* Class fields: */
    private ProcessingImage processingImage;
    private SubImgCharMatcher subImgCharMatcher;
    private String outputMethod = "console"; // Default output method

    /**
     * Main method for running the shell interface.
     *
     * @param args Command-line arguments. The first argument should be the image file path.
     */
    public static void main(String[] args) {
        String imageName = args[0];
        Shell shell = new Shell(imageName);
        shell.run(imageName);

    }

    /**
     * Constructs a new Shell instance with the specified image.
     *
     * @param imageName The path to the image file to be processed.
     * @throws RuntimeException If the image cannot be loaded.
     */
    public Shell(String imageName) {
        char[] charsSet = DEFAULT_CHARS_SET;
        Image image;
        try {
            image = new Image(imageName);
        } catch (IOException e) {
            throw new RuntimeException(e);//TODO: check if needed
        }
        this.processingImage = new ProcessingImage(image, DEFAULT_RESOLUTION);
        this.subImgCharMatcher = new SubImgCharMatcher(charsSet);
    }


    /**
     * Runs the shell interface, processing commands entered by the user.
     */
    public void run(String imageName) {//todo: handle this
        boolean notExitCommand = true;
        while(notExitCommand){
            System.out.print(INPUT_STRING);
            String input = KeyboardInput.readLine(); //get user's input via KeyboardInput class
            String[] parts = input.split(" ");
            String command = parts[0];
            try {
                notExitCommand = commandsFactory(command, parts);
            }
            catch (IOException | CharsetIsEmptyException | InvalidCommandFormatException |
                   InvalidResolutionException e){
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Processes and executes commands based on user input.
     *
     * @param command The main command string.
     * @param parts   The command and its arguments.
     * @return False if the command is "exit", true otherwise.
     * @throws IOException               If an I/O error occurs.
     * @throws CharsetIsEmptyException   If the character set is empty.
     * @throws InvalidCommandFormatException If the command format is invalid.
     * @throws InvalidResolutionException if the desired resolution is oot of boundaries.
     */
    private boolean commandsFactory(String command, String[] parts) throws IOException,
            InvalidCommandFormatException,
            CharsetIsEmptyException,
            InvalidResolutionException{
        try {
            switch (command) {
                case EXIT_COMMAND:
                    return false;
                case DISPLAY_CHARS_COMMAND:
                    printCharSet();
                    break;
                case ADD_CHARACTER_COMMAND:
                    addToCharSet(parts);
                    break;
                case REMOVE_CHARACTER_COMMAND:
                    removeFromCharSet(parts);
                    break;
                case ADJUST_RESOLUTION_COMMAND:
                    adjustResolution(parts);
                    break;
                case CHANGE_ROUNDING_COMMAND:
                    changeRoundingMethod(parts);
                    break;
                case SWITCH_OUTPUT_COMMAND:
                    switchOutput(parts);
                    break;
                case RUN_ASCIIART_COMMAND:
                    runAsciiArt();
                    break;
                default:
                    System.out.println(INVALID_COMMAND_MESSAGE);
            }
        }
        catch(InvalidCommandFormatException e){
            throw new InvalidCommandFormatException(e.getMessage());
            }
        catch (CharsetIsEmptyException e){
            throw new CharsetIsEmptyException(e.getMessage());
            }
        catch (InvalidResolutionException e){
            throw new InvalidResolutionException(e.getMessage());
        }
        catch (IOException e){
            throw new IOException(e.getMessage());
        }
        return true;
    }

    /**
     * Switches the output method for the ASCII art.
     *
     * @param parts The command and its arguments. Expected values: "console" or "html".
     * @throws InvalidCommandFormatException if the command's format is not good.
     */
    private void switchOutput(String[] parts) throws InvalidCommandFormatException {
        if (parts.length > 1) {
            String method = parts[1];
            if (method.equals("console") || method.equals("html")) {
                this.outputMethod = method;
                return;
            }
        }
        throw new InvalidCommandFormatException(INVALID_FORMAT_OUTPUT_MESSAGE);

    }
    /**
     * Changes the rounding method used for matching brightness to characters.
     *
     * @param parts The command and its arguments. Expected values: "up", "down", or "abs".
     * @throws InvalidCommandFormatException if the command's format is not good.
     */
    private void changeRoundingMethod(String[] parts) throws InvalidCommandFormatException {
        if (parts.length < 2) {
            throw new InvalidCommandFormatException(INVALID_ROUNDING_FORMAT_MESSAGE);

        }
        String method = parts[1];
        try {
            subImgCharMatcher.setRoundingMethod(method);
        } catch (IllegalArgumentException e) {
            throw new InvalidCommandFormatException(INVALID_ROUNDING_FORMAT_MESSAGE);
        }
    }

    /**
     * Prints the current character set used for generating ASCII art.
     */
    private void printCharSet(){
        subImgCharMatcher.printCharSet();
    }
    /**
     * Adds characters to the character set used for generating ASCII art.
     *
     * @param parts The command and its arguments. Accepts "all", "space", or ranges (e.g., "a-d").
     * @throws IOException If the input format is incorrect or characters are out of range.
     */
    private void addToCharSet(String[] parts) throws IOException  {
        if (parts.length != 2) {
            throw new IOException(ADD_ERROR_MESSAGE);
        }
        String param = parts[1];
        switch (param) {
            case "all":
                addRange(' ', '~');
                return;

            case "space":
                subImgCharMatcher.addChar(' ');
                return;

            default:
                if (param.length() == THREE && param.charAt(1) == '-') {
                    addRange(param.charAt(0), param.charAt(2));
                } else if (param.length() == 1) {
                    addSingleChar(param.charAt(0));
                } else {
                    throw new IOException(ADD_ERROR_MESSAGE);                }
        }
    }
    /**
     * Removes characters from the character set used for generating ASCII art.
     *
     * @param parts The command and its arguments. Accepts "all", "space", or ranges (e.g., "a-d").
     * @throws IOException If the input format is incorrect or characters are out of range.
     */
    private void removeFromCharSet(String[] parts) throws IOException{
        if (parts.length != 2) {
            throw new IOException(REMOVE_ERROR_MESSAGE);
        }

        String param = parts[1];

        switch (param) {
            case "all":
                subImgCharMatcher.clear();
                return;

            case "space":
                subImgCharMatcher.removeChar(' ');
                return;

            default:
                if (param.length() == THREE && param.charAt(1) == '-') {
                    removeRange(param.charAt(0), param.charAt(2));
                } else if (param.length() == 1) {
                    removeSingleChar(param.charAt(0));
                } else {
                    throw new IOException(REMOVE_ERROR_MESSAGE);
                }
        }
    }


/**
 * Adds a range of characters to the character set.
 *
 * @param start The starting character of the range.
 * @param end   The ending character of the range.
 * @throws IOException If the range is invalid or out of ASCII bounds.
 */
    private void addRange(char start, char end) throws IOException{
        // Normalize range: Ensure start is smaller than end
        if (start > end) {
            char temp = start;
            start = end;
            end = temp;
        }
        if (start < MIN_ASCII || end > MAX_ASCII) {
            throw new IOException(ADD_ERROR_MESSAGE);
        }

        for (char c = start; c <= end; c++) {
            subImgCharMatcher.addChar(c);
        }
    }
    /**
     * Removes a range of characters from the character set.
     *
     * @param start The starting character of the range.
     * @param end   The ending character of the range.
     * @throws IOException If the range is invalid or out of ASCII bounds.
     */
    private void removeRange(char start, char end) throws IOException {
        // Normalize range: Ensure start is smaller than end
        if (start > end) {
            char temp = start;
            start = end;
            end = temp;
        }
        if (start < MIN_ASCII || end > MAX_ASCII) {
            throw new IOException(REMOVE_ERROR_MESSAGE);
        }

        for (char c = start; c <= end; c++) {
            subImgCharMatcher.removeChar(c);
        }
    }
    /**
     * Adds a single character to the character set.
     *
     * @param c The character to add.
     * @throws IOException If the character is out of ASCII bounds.
     */
    private void addSingleChar(char c) throws IOException{
        if (c < MIN_ASCII || c > MAX_ASCII) {
            throw new IOException(ADD_ERROR_MESSAGE);

        }
        subImgCharMatcher.addChar(c);
    }
    /**
     * Removes a single character from the character set.
     *
     * @param c The character to remove.
     * @throws IOException If the character is out of ASCII bounds.
     */
    private void removeSingleChar(char c) throws IOException {
        if (c < MIN_ASCII || c > MAX_ASCII) {
            throw new IOException(REMOVE_ERROR_MESSAGE);
        }
        subImgCharMatcher.removeChar(c);
    }

    /**
     * Adjusts the resolution for generating ASCII art.
     *
     * @param parts The command and its arguments. Expected values: "up" or "down".
     * @throws InvalidResolutionException If the resolution is out of valid bounds.
     * @throws InvalidCommandFormatException if the command's format is not good.
     */
    private void adjustResolution(String[] parts) throws InvalidResolutionException,
            InvalidCommandFormatException {
        try {
            if (parts.length > 1) {
                String direction = parts[1];
                if (direction.equals("up")) {
                    processingImage.setResolution(processingImage.getResolution() * 2);
                } else if (direction.equals("down")) {
                    processingImage.setResolution(processingImage.getResolution() / 2);
                } else {
                    throw new InvalidCommandFormatException(RESOLUTION_COMMAND_FORMAT_ERROR_MESSAGE);
                }
            }
            System.out.println(RESOLUTION_SET_MESSAGE + processingImage.getResolution() + ".");
        } catch (IllegalArgumentException e) {
            throw new InvalidResolutionException(RESOLUTION_BOUNDRIES_ERROR_MESSAGE);
        }
    }
    /**
     * Executes the ASCII art generation algorithm and outputs the result.
     *
     * @throws CharsetIsEmptyException If the character set contains fewer than two characters.
     */
    private void runAsciiArt()throws CharsetIsEmptyException {
        AsciiArtAlgorithm algorithm = null;
        try {
            algorithm = new AsciiArtAlgorithm(processingImage, subImgCharMatcher);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (subImgCharMatcher.getCharsetSize()<2){
            throw new CharsetIsEmptyException(CHARSET_SIZE_ERROR_MESSAGE);
        }
        char[][] asciiResult = algorithm.run();

        AsciiOutput output;
        if (outputMethod.equals("html")) {
            output = new HtmlAsciiOutput(OUT_FILE_NAME, FONT_NAME);
        } else {
            output = new ConsoleAsciiOutput();
        }
        output.out(asciiResult);
    }

}
