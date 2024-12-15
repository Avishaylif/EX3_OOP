package image_char_matching;
import java.util.*;

public class SubImgCharMatcher {
    private char[] charSet;
    private HashMap<Character, Double> rawVal;
    private HashMap<Double, Set<Character>> normRawValByBrightness;
    private HashMap<Character, Double> normRawValByChar;
    private double minBrightness = Double.MAX_VALUE;
    private double maxBrightness = Double.MIN_VALUE;



    public SubImgCharMatcher(char[] charset) {
        this.charSet = charset;
        this.rawVal = new HashMap<>();
        this.normRawValByBrightness = new HashMap<>();
        this.normRawValByChar = new HashMap<>();

        for (char ch : this.charSet) {
            brightnessVal(ch);
        }

        updateNorm();
        updateNormSet();
    }


    public char getCharByImageBrightness(double brightness) {
        char closestChar = charSet[0];
        double minDifference = Double.MAX_VALUE;
        for (Character ch : normRawValByChar.keySet()) {
            double curDifference = Math.abs(brightness - normRawValByChar.get(ch));
            if (curDifference < minDifference) {
                minDifference = curDifference;
                closestChar = ch;
            }
        }
        return closestChar;
    }

     public void addChar(char ch) {
        if (!rawVal.containsKey(ch)) {
            char[] newCharSet = new char[charSet.length + 1];
            System.arraycopy(charSet, 0, newCharSet, 0, charSet.length);
            newCharSet[charSet.length] = ch;
            this.charSet = newCharSet;
            Arrays.sort(charSet);
            brightnessVal(ch);
            updateNorm();
            updateNormSet();
        }
    }


    public void removeChar(char ch) {
        if (rawVal.containsKey(ch)) {
            char[] newCharSet = new char[charSet.length - 1];
            int index = 0;
            for (char value : charSet) {
                if (value == ch) {
                    continue;
                }
                newCharSet[index] = value;
                index++;
            }
            this.charSet = newCharSet;

            // Sort the charSet in natural (ASCII) order
            Arrays.sort(charSet);

            this.rawVal.remove(ch);
            this.normRawValByChar.remove(ch);

            updateNorm();
            updateNormSet();
        }
    }

     private void updateNormSet() {
        this.normRawValByBrightness = new HashMap<>();

        for (char ch : charSet) {
            double brightness = normRawValByChar.get(ch);
            Set<Character> charSet = normRawValByBrightness.getOrDefault(brightness, new HashSet<>());
            charSet.add(ch);
            normRawValByBrightness.put(brightness, charSet);
        }
    }


    private void brightnessVal(char ch) {
        if (!this.rawVal.containsKey(ch)) {
            boolean[][] boolArr = CharConverter.convertToBoolArray(ch);
            double numWhite = 0;
            for (boolean[] booleans : boolArr) {
                for (int j = 0; j < boolArr[0].length; j++) {
                    if (booleans[j]) {
                        numWhite++;
                    }
                }
            }
            rawVal.put(ch, numWhite / (boolArr.length * boolArr[0].length));
        }
        normRawValByChar.put(ch, rawVal.get(ch));
    }

    private void updateMinMax() {
        minBrightness = Double.MAX_VALUE;
        maxBrightness = Double.MIN_VALUE;

        for (double val : rawVal.values()) {
            if (val < minBrightness) {
                minBrightness = val;
            }
            if (val > maxBrightness) {
                maxBrightness = val;
            }
        }
    }

    private void updateNorm() {
        updateMinMax();
        for (char ch : charSet) {
            double valBrightness = rawVal.get(ch);
            double newVal = (valBrightness - minBrightness) / (maxBrightness - minBrightness);
            normRawValByChar.put(ch, newVal);
        }
    updateNormSet();
    }


    public void printCharSet() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < charSet.length; i++) {
            result.append(charSet[i]);
            if (i < charSet.length - 1) {
                result.append(' ');
            }
        }
        System.out.println(result);
    }

    /**
     * Gets the size of the character set.
     *
     * @return Size of the character set.
     */
    public int getCharSetSize() {
        return this.charSet.length;
    }

}
