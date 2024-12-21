package image;
import java.awt.*;

/**to do:
 * In this class there is public method,check if is good.
 */

public class ProcessingImage {
    public static final int MINIMUM_RESOLUTION = 1;
    private Image image;
    private Image[][] partImage;
    private double[][] cachedBrightness;
    private int resolution;
    //constants
    private static final int defaultResolution = 2;
    private static final int WHITEVAL = 255;
    private static final double GREEN = 0.7152;
    private static final double BLUE = 0.0722;
    private static final double RED = 0.2126;
    private boolean cacheValid = false;
    private static final Color WHITE = new Color(255, 255, 255);


    /**
     * Constructor for ProcessingImage.
     * @param image The image to be processed.
     * @param resolution The resolution of the image.
     */
    public ProcessingImage(Image image, int resolution) {

        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null");
        }
        this.image = wrapImage(image);
        setResolution(resolution);
    }

    /**
     * Sets the resolution of the image.
     * @param newResolution The new resolution of the image.
     */
    public void setResolution(int newResolution) {
        int minCharsInRow = Math.max(MINIMUM_RESOLUTION, image.getWidth() / image.getHeight());
        if (newResolution >= minCharsInRow && newResolution <= image.getWidth()) {
            this.resolution = newResolution;
            cacheValid = false;
        } else {//TODO: check if to delete
            throw new IllegalArgumentException("Resolution exceeds boundaries.");
        }
    }
    /**
     * Returns the resolution of the image.
     * @return The resolution of the image.
     */
    public int getResolution() {
        return resolution;
    }

    /**
     * Wraps the image with white pixels to make it a square.
     * @param image The image to be wrapped.
     * @return The wrapped image.
     */
    private Image wrapImage(Image image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int newWidth = 1;
        int newHeight = 1;

        // Calculate the smallest power of 2 that is greater than the width and height
        while (newWidth < width || newHeight < height) {
            if (newWidth < width) {
                newWidth *= 2;
            }
            if (newHeight < height) {
                newHeight *= 2;
            }
        }

        // Calculate symmetric padding for width and height
        int horPadding = (newWidth - width) / 2;
        int paddingTop = (newHeight - height) / 2;

        // Create a new pixel matrix for the wrapped image
        Color[][] newPixels = new Color[newHeight][newWidth];

        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                int firstX = j - horPadding; // Adjust X for horizontal padding
                int firstY = i - paddingTop;  // Adjust Y for vertical padding

                if (firstX >= 0 && firstX < width && firstY >= 0 && firstY < height) {
                    newPixels[i][j] = image.getPixel(firstY, firstX);
                } else {
                    // Fill padding with white pixels
                    newPixels[i][j] = WHITE;
                }
            }
        }
        return new Image(newPixels, newWidth, newHeight);
    }



    /**
     * Returns the sub-images of the image.
     * @return The sub-images of the image.
     */
    public Image[][] subImages() {
        if (cacheValid) {
            return this.partImage;
        }
            int size = image.getWidth() / resolution; // Size of each sub-image
            int row = image.getHeight() / size;
            int col = image.getWidth() / size;
            this.partImage = new Image[row][col];
            this.cachedBrightness = new double[row][col];
            for (int k = 0; k < row; k++) {
                for (int p = 0; p < col; p++) {
                    Color[][] sunIm = new Color[size][size];
                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {
                            int x = p * size + j;
                            int y = k * size + i;
                            sunIm[i][j] = image.getPixel(y, x);
                        }
                    }
                    this.partImage[k][p] = new Image(sunIm, size, size);
                    this.cachedBrightness[k][p] = grayConvert(this.partImage[k][p]);

                }
            }
        cacheValid = true;
        return this.partImage;
    }

    /**
     * Converts the image to grayscale.
     * @param image The image to be converted.
     * @return The grayscale value of the image.
     */
    public double grayConvert(Image image) {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null");
        }
        int rows = image.getHeight();
        int cols = image.getWidth();
        double greyPixel = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Color pixel = image.getPixel(i, j);
                greyPixel += RED * pixel.getRed() + GREEN * pixel.getGreen() + BLUE * pixel.getBlue();
            }
        }
        double result = greyPixel / (rows * cols) / WHITEVAL;
        return result;
    }

}


