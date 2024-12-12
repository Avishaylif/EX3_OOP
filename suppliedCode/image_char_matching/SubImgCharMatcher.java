package image_char_matching;

import java.util.HashMap;
import java.util.Map;

public class SubImgCharMatcher {
    private HashMap<Character, Double> charset;

    public SubImgCharMatcher(char[] charset) {
        this.charset = new HashMap<>();
        for (char c : charset) {
            this.charset.put(c, getBrightness(CharConverter.convertToBoolArray(c)));
        }
        // Normalize the brightness values
        double max = this.charset.values().stream().max(Double::compare).get();
        double min = this.charset.values().stream().min(Double::compare).get();
        for (Map.Entry<Character, Double> entry : this.charset.entrySet()) {
            entry.setValue((entry.getValue() - min) / (max - min));
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }

    public char getCharByImageBrightness(double brightness) {
        double closestDifference = Double.MAX_VALUE;
        char closestKey = ' ';

        for (char key : charset.keySet()) {
            double difference = Math.abs(charset.get(key) - brightness);

            if (difference < closestDifference) {
                closestDifference = difference;
                closestKey = key;
            }
        }
        return closestKey;
    }


    public void addChar(char c){
        charset.put(c, 0.0);
    }
    public void removeChar(char c){
        charset.remove(c);
    }

    private double getBrightness(boolean[][] img ) {
        double sum = 0;
        for (int y = 0; y < img.length; y++) {
            for (int x = 0; x < img[y].length; x++) {
                sum += img[y][x] ? 1 : 0;
            }
        }
        return sum / (img.length * img[0].length);
    }
}
