package ascii_art;

import image.Image;
import image.ProcessingImage;
import image_char_matching.SubImgCharMatcher;
import java.io.IOException;


/**
 * A class that runs the algorithm to convert an image to ASCII art.
 * The algorithm uses a SubImgCharMatcher to determine the best character to represent a sub-image.
 * The algorithm uses a ProcessingImage to convert the image to a 2D array of sub-images.
 * The algorithm then uses the SubImgCharMatcher to determine the best character to represent each sub-image.
 * The result is a 2D array of characters that represent the image in ASCII art.
 */
public class AsciiArtAlgorithm {
    private ProcessingImage processingImage;
    private SubImgCharMatcher subImgCharMatcher;

    /**
     * Constructor for AsciiArtAlgorithm
     * @param processingImage The ProcessingImage object to be used
     * @param subImgCharMatcher The SubImgCharMatcher object to be used
     * @throws IOException
     */
    public AsciiArtAlgorithm(ProcessingImage processingImage,
                             SubImgCharMatcher subImgCharMatcher) throws IOException {
        this.processingImage = processingImage;
        this.subImgCharMatcher = subImgCharMatcher;
    }

    /**
     * Runs the algorithm to convert the image to ASCII art.
     * @return A 2D array of characters that represent the image in ASCII art
     */
    public char[][] run() {
        Image[][] subImages = processingImage.subImages();
        int rows = subImages.length;
        int cols = subImages[0].length;
        char[][] result = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double grey = processingImage.grayConvert(subImages[i][j]);
                result[i][j] = subImgCharMatcher.getCharByImageBrightness(grey);
            }
        }
        return result;
    }


}


