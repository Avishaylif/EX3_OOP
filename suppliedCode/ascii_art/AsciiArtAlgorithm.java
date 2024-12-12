package ascii_art;

import image.Image;
import image.ProcessingImage;
import image_char_matching.SubImgCharMatcher;
import java.io.IOException;
import java.util.Arrays;
import java.util.Stack;

public class AsciiArtAlgorithm {
    private static Image image;
    private static char[] charset;
    private static int resolution;



    public AsciiArtAlgorithm(String path, int resolution, char[] charset) throws IOException {
        image = new Image(path);
        AsciiArtAlgorithm.charset = charset;
        AsciiArtAlgorithm.resolution = resolution;
    }
    public char[][] run() {
        ProcessingImage processingImage = new ProcessingImage(image);
        Image wrappedImage = processingImage.wrapImage(image);
        Image[][] subImages = processingImage.partImage(wrappedImage, resolution);
        SubImgCharMatcher subImgCharMatcher = new SubImgCharMatcher(charset);
        char[][] result = new char[resolution][resolution];
        for (int i = 0; i < resolution; i++) {
            for (int j = 0; j < resolution; j++) {
                if (subImages[i][j] != null) {
                    double grey = processingImage.grayConvert(subImages[i][j]);
                    result[i][j] = subImgCharMatcher.getCharByImageBrightness(grey);
                }
            }
        }
        return result;

    }

    public static void main(String[] args) throws IOException {
        char[] charset = {'m', 'o'};
        AsciiArtAlgorithm asciiArtAlgorithm = new AsciiArtAlgorithm("examples/board.jpeg", 2, charset);
        char[][] result = asciiArtAlgorithm.run();
        System.out.println(Arrays.deepToString(result));
        }
    }


