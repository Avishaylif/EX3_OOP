package image;

import java.awt.*;

public class ProcessingImage {
    private final Image image;

    public ProcessingImage(Image image) {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null");
        }
        this.image = image;
    }


    public Image wrapImage(Image image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int newWidth = 1;
        int newHeight = 1;
        while (newWidth < width) {
            newWidth *= 2;
        }
        while (newHeight < height) {
            newHeight *= 2;
        }
        Color[][] newPixels = new Color[newHeight][newWidth];
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                if (i < height && j < width) {
                    newPixels[i][j] = image.getPixel(i, j);
                } else {
                    newPixels[i][j] = new Color(255, 255, 255);
                }
            }
        }
        return new Image(newPixels, newWidth, newHeight);
    }


    public Image[][] partImage(Image image, int resolution) {
        int rows = image.getHeight();
        int cols = image.getWidth();
        int numSubImages = (rows / resolution) * (cols / resolution);
        Image[][] result = new Image[numSubImages][numSubImages];
        for (int i = 0; i < rows / resolution; i++) {
            for (int j = 0; j < cols / resolution; j++) {
                Color[][] subMatrix = new Color[resolution][resolution];
                for (int k = 0; k < resolution; k++) {
                    for (int l = 0; l < resolution; l++) {
                        subMatrix[k][l] = image.getPixel(i * resolution + k, j * resolution + l);
                    }
                }
                result[i][j] = new Image(subMatrix, resolution, resolution);
            }
        }
        return result;
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

        double averageBrightness = greyPixel / (rows * cols);
        return averageBrightness;
    }

}


