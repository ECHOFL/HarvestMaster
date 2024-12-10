package me.fliqq.harvestmaster.enumerate;

import net.kyori.adventure.text.format.TextColor;

public enum ColorLevel {
    // Define colors for each 10-level range
    LEVEL_1(TextColor.fromHexString("#FFFFFF")),      // Levels 1-10
    LEVEL_2(TextColor.fromHexString("#A0FF00")),      // Levels 11-20
    LEVEL_3(TextColor.fromHexString("#FFFF00")),      // Levels 21-30
    LEVEL_4(TextColor.fromHexString("#FFA500")),      // Levels 31-40
    LEVEL_5(TextColor.fromHexString("#FF0000")),      // Levels 41-50
    LEVEL_6(TextColor.fromHexString("#800080")),      // Levels 51-60
    LEVEL_7(TextColor.fromHexString("#0000FF")),      // Levels 61-70
    LEVEL_8(TextColor.fromHexString("#00FFFF")),      // Levels 71-80
    LEVEL_9(TextColor.fromHexString("#FF00FF")),      // Levels 81-90
    LEVEL_10(TextColor.fromHexString("#000000"));      // Levels 91-100

    private final TextColor color;

    ColorLevel(TextColor color) {
        this.color = color;
    }

    public TextColor getColor() {
        return color;
    }

    public static TextColor getColor(int level) {
        if (level < 1 || level > 1000) {
            throw new IllegalArgumentException("Level must be between 1 and 1000");
        }

        // Determine which color range to use based on level
        int index = (level - 1) / 10; // Each color represents a range of 10 levels

        // Ensure we don't exceed the defined colors (for levels beyond defined ranges)
        if (index >= values().length) {
            index = values().length - 1; // Cap at the last defined color
        }

        // Get the current color for the level range
        TextColor startColor = values()[index].getColor();
        
        // If we're not at the last range, get the end color for interpolation
        TextColor endColor = (index + 1 < values().length) ? values()[index + 1].getColor() : startColor;

        // Calculate the position within the current range (0 to 9)
        float positionInRange = (level % 10) / 10f; 

        return interpolateColor(startColor, endColor, positionInRange);
    }

    private static TextColor interpolateColor(TextColor start, TextColor end, float ratio) {
        int startRed = start.red();
        int startGreen = start.green();
        int startBlue = start.blue();

        int endRed = end.red();
        int endGreen = end.green();
        int endBlue = end.blue();

        // Calculate the interpolated color components
        int red = (int) (startRed + (endRed - startRed) * ratio);
        int green = (int) (startGreen + (endGreen - startGreen) * ratio);
        int blue = (int) (startBlue + (endBlue - startBlue) * ratio);

        return TextColor.color((red << 16) | (green << 8) | blue); // Combine RGB into a single integer
    }
}
