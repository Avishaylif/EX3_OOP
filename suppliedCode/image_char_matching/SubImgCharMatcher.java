package image_char_matching;
import java.util.*;

public class SubImgCharMatcher {
    private TreeSet<Character> charSet;
    private HashMap<Character, Double> rawVal;

    private HashMap<Character, Double> normRawValByChar;
    private double minBrightness = Double.MAX_VALUE;
    private double maxBrightness = Double.MIN_VALUE;
    private String roundingMethod = "abs"; // Default rounding method
    private TreeMap<Double, Character> brightnessMap;

    private void updateBrightnessMap() {
        brightnessMap = new TreeMap<>();
        for (char ch : charSet) {
            double brightness = normRawValByChar.get(ch);
            // Use compute to handle existing entries and keep the char with lowest ASCII
            brightnessMap.compute(brightness, (k, existingChar) -> {
                if (existingChar == null || ch < existingChar) {
                    return ch; // Keep the new char if it's the first or has a lower ASCII
                } else {
                    return existingChar; // Keep the existing char if it has a lower or equal ASCII
                }
            });
        }
    }


    public SubImgCharMatcher(char[] charset) {
        this.charSet = new TreeSet<>();
        this.rawVal = new HashMap<>();

        this.normRawValByChar = new HashMap<>();

        for (char ch : charset) {
            addChar(ch); // Use addChar to ensure proper setup
        }

        for (char ch : this.charSet) {
            brightnessVal(ch);
        }
        updateNorm();

    }




    public char getCharByImageBrightness(double brightness) {
        if (charSet.isEmpty()) {
            throw new IllegalStateException("Character set is empty. Cannot determine closest character.");
        }

        return switch (roundingMethod) {
            case "up" -> findCharRoundingUp(brightness);
            case "down" -> findCharRoundingDown(brightness);
            default -> findCharRoundingAbsolute(brightness);
        };
    }

    private char findCharRoundingUp(double brightness) {
        Double ceilingKey = brightnessMap.ceilingKey(brightness);
        if (ceilingKey != null) {
            return brightnessMap.get(ceilingKey);
        }

        return brightnessMap.get(brightnessMap.lastKey());
    }

    private char findCharRoundingDown(double brightness) {
        Double floorKey = brightnessMap.floorKey(brightness);
        if (floorKey != null) {
            return brightnessMap.get(floorKey);
        }


        return brightnessMap.get(brightnessMap.firstKey());
    }

    private char findCharRoundingAbsolute(double brightness) {
        double minDifference = Double.MAX_VALUE;
        char closestChar = brightnessMap.firstEntry().getValue();

        for (Map.Entry<Double, Character> entry : brightnessMap.entrySet()) {
            double difference = Math.abs(entry.getKey() - brightness);
            if (difference < minDifference) {
                minDifference = difference;
                closestChar = entry.getValue();
            }
        }

        return closestChar;
    }

    public void addChar(char ch) {
        if (!charSet.contains(ch)) {
            charSet.add(ch);
            brightnessVal(ch);
            updateNorm();
        }
    }

    public int getCharsetSize(){
        return charSet.size();
    }

    public void removeChar(char ch) {
        if (charSet.contains(ch)) {
            charSet.remove(ch);
            rawVal.remove(ch);
            normRawValByChar.remove(ch);
            updateNorm();
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



    public void printCharSet() {
        if (charSet.isEmpty()){
            return;
        }
        for (char ch : charSet) {
            System.out.print(ch + " ");
        }
        System.out.println();
    }

    public void clear() {
        charSet.clear();
    }

    public void setRoundingMethod(String method) {
        if (method.equals("up") || method.equals("down") || method.equals("abs")) {
            this.roundingMethod = method;
        } else {
            throw new IllegalArgumentException("Invalid rounding method.");
        }
    }

    //Call updateBrightnessMap() in updateNorm()
    private void updateNorm() {
        updateMinMax();
        for (char ch : charSet) {
            double valBrightness = rawVal.get(ch);
            double newVal = (valBrightness - minBrightness) / (maxBrightness - minBrightness);
            normRawValByChar.put(ch, newVal);
        }

        updateBrightnessMap();
    }


}
