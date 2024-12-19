package image;
import java.awt.*;

/**to do:
 * In this class there is public method,check if is good.
 */

public class ProcessingImage {
    public static final int MINIMUM_RESOLUTION = 1;
    private Image image;
    private Image[][] partImage;
    private int resolution;



    public ProcessingImage(Image image, int resolution) {

        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null");
        }
        this.image = wrapImage(image);
        setResolution(resolution);
    }

    public void setResolution(int newResolution) {
        int minCharsInRow = Math.max(MINIMUM_RESOLUTION, image.getWidth() / image.getHeight());
        if (newResolution >= minCharsInRow && newResolution <= image.getWidth()) {
            this.resolution = newResolution;
        } else {
            throw new IllegalArgumentException("Resolution exceeds boundaries.");
        }
    }

    public int getResolution() {
        return resolution;
    }


    private Image wrapImage(Image image) {
        int width = image.getWidth();
        int height = image.getHeight();

        // Find the closest power of 2 greater than or equal to the current width
        int newWidth = 1;
        while (newWidth < width) {
            newWidth *= 2;
        }

        // Find the closest power of 2 greater than or equal to the current height
        int newHeight = 1;
        while (newHeight < height) {
            newHeight *= 2;
        }

        // Calculate symmetric padding for width and height
        int paddingLeft = (newWidth - width) / 2;
        int paddingTop = (newHeight - height) / 2;

        // Create a new pixel matrix for the wrapped image
        Color[][] newPixels = new Color[newHeight][newWidth];

        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                int origX = j - paddingLeft; // Adjust X for left padding
                int origY = i - paddingTop;  // Adjust Y for top padding

                if (origX >= 0 && origX < width && origY >= 0 && origY < height) {
                    // Copy original pixel to the new position
                    newPixels[i][j] = image.getPixel(origY, origX);
                } else {
                    // Fill padding with white pixels
                    newPixels[i][j] = new Color(255, 255, 255);
                }
            }
        }

        // Return the new image with updated dimensions
        return new Image(newPixels, newWidth, newHeight);
    }




    public Image[][] subImages() {
        int size = image.getWidth() / resolution; // Size of each sub-image
        int row = image.getHeight() / size;
        int col = image.getWidth() / size;
        this.partImage = new Image[row][col];
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
            }
        }
        return this.partImage;
    }


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
                greyPixel += 0.2126 * pixel.getRed() + 0.7152 * pixel.getGreen() + 0.0722 * pixel.getBlue();
            }
        }
        double result = greyPixel / (rows * cols) / 255;
        return result;
    }

}


