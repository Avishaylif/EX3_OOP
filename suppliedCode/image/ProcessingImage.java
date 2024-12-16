package image;
import java.awt.*;

/**to do:
 * In this class there is pbulic method,check if is good.
 */

public class ProcessingImage {
    private Image image;
    private Image[][] partImage;

    public ProcessingImage(Image image) {
        this.image = image;
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null");
        }
    }


    public Image wrapImage() {
        int width = image.getWidth();
        int height = image.getHeight();
        int newSize = 1;
        while (newSize < Math.max(width, height)) {
            newSize *= 2;
        }
        int paddingLeft = (newSize - width) / 2;
        int paddingTop = (newSize - height) / 2;
        System.out.println("width: " + width);
        System.out.println("height: " + height);
        System.out.println("newSize: " + newSize);
        System.out.println("paddingLeft: " + paddingLeft);
        System.out.println("paddingTop: " + paddingTop);

        Color[][] newPixels = new Color[newSize][newSize];
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                int origX = j - paddingLeft;
                int origY = i - paddingTop;
//                if (origX >= 0 && origX < width && origY >= 0 && origY < height) {
//                    newPixels[i][j] = image.getPixel(origX, origY);
//                } else {
//                    newPixels[i][j] = new Color(255, 255, 255);
//                }
            }
        }

        return new Image(newPixels, newSize, newSize);
    }



    public Image[][] subImages(int resolution) {
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
        return greyPixel / (rows * cols) / 255;
    }

}


