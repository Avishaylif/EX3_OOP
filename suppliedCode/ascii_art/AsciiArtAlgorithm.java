package ascii_art;


import image.Image;
import image.ProcessingImage;
import image_char_matching.SubImgCharMatcher;
import java.io.IOException;



public class AsciiArtAlgorithm {
    private ProcessingImage processingImage;
    private SubImgCharMatcher subImgCharMatcher;



    public AsciiArtAlgorithm(ProcessingImage processingImage,
                             SubImgCharMatcher subImgCharMatcher) throws IOException {
        this.processingImage = processingImage;
        this.subImgCharMatcher = subImgCharMatcher;

    }

    public char[][] run() {
        Image[][] subImages = processingImage.subImages();
        char[][] result = new char[subImages.length][subImages[0].length];

        for (int i = 0; i < subImages.length; i++) {
            for (int j = 0; j < subImages[0].length; j++) {
                double grey = processingImage.grayConvert(subImages[i][j]);
                result[i][j] = subImgCharMatcher.getCharByImageBrightness(grey);
            }
        }
        return result;
    }


}


