package ru.netology.graphics.image;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class TextGraphics implements TextGraphicsConverter {
    private int maxWidth = Integer.MAX_VALUE;
    private int maxHeight = Integer.MAX_VALUE;
    private double maxRatio = Double.MAX_VALUE;
    private TextColorSchema schema;
    public TextGraphics() {
        schema = new TextColor (new char[]{'#', '$', '@', '%', '*', '+', '-', '.'});
    }
    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read (new URL (url));
        int width = img.getWidth ();
        int height = img.getHeight ();
        double ratio = width > height ? (double) width / height : (double) height / width;
        if ( ratio > maxRatio ) {
            throw new BadImageSizeException (ratio, maxRatio);
        }
        double ratioNew = Math.max ((double) width / maxWidth, (double) height / maxHeight);
        int newWidth = ratioNew > 1 ? (int) (width / ratioNew) : width;
        int newHeight = ratioNew > 1 ? (int) (height / ratioNew) : height;

        Image scaledImage = img.getScaledInstance (newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage (newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics ();
        graphics.drawImage (scaledImage, 0, 0, null);
        WritableRaster bwRaster = bwImg.getRaster ();

        char[][] pixel = new char[newHeight][newWidth];
        for ( int i = 0 ; i < newHeight ; i++ ) {
            for ( int j = 0 ; j < newWidth ; j++ ) {
                int color = bwRaster.getPixel (j, i, new int[3])[0];
                char c = schema.convert (color);
                pixel[i][j] = c;
            }
        }
        StringBuilder strBld = new StringBuilder ();
        for ( int i = 0 ; i < pixel.length ; i++ ) {
            for ( int j = 0 ; j < pixel[i].length ; j++ ) {
                strBld.append (pixel[i][j]);
                strBld.append (pixel[i][j]);
            }
            strBld.append ("\n");
        }
        return strBld.toString ();
    }
    @Override
    public void setMaxWidth(int width) {
        maxWidth = width;
    }
    @Override
    public void setMaxHeight(int height) {
        maxHeight = height;
    }
    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }
    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}