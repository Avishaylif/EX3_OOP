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
        processingImage.wrapImage();
        Image[][] subImages = processingImage.subImages(resolution);
        SubImgCharMatcher subImgCharMatcher = new SubImgCharMatcher(charset);
        char[][] result = new char[subImages.length][subImages[0].length];
        for (int i = 0; i < subImages.length; i++) {
            for (int j = 0; j < subImages[0].length; j++) {
                if (subImages[i] != null) {
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

// tell me you see last updated at 2024-15-12 16:43

