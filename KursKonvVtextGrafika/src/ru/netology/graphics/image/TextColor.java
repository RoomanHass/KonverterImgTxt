package ru.netology.graphics.image;
public class TextColor implements TextColorSchema {
    private final char[] Density;
    public TextColor(char[] Density) {
        this.Density = Density;
    }
    @Override
    public char convert(int color) {
        //return Density[color / 32];
        int charValue = (int) Math.round ((double) (color * (Density.length) / 255));
        charValue = Math.max (charValue, 0);
        charValue = Math.min (charValue, Density.length - 1);
        return Density[charValue];
    }
}